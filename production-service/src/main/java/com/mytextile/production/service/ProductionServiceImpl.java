package com.mytextile.production.service;

import com.mytextile.production.dto.*;
import com.mytextile.production.events.JobCancelledEvent;
import com.mytextile.production.events.ProductionCompletedEvent;
import com.mytextile.production.events.ProductionInputDto;
import com.mytextile.production.events.ProductionStartedEvent;
import com.mytextile.production.exception.BusinessLogicException;
import com.mytextile.production.exception.ResourceNotFoundException;
import com.mytextile.production.mapper.MachineMapper;
import com.mytextile.production.mapper.ProductionJobMapper;
import com.mytextile.production.model.*;
import com.mytextile.production.repository.MachineRepository;
import com.mytextile.production.repository.ProductionJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductionServiceImpl implements ProductionService {

    // --- Repositories ---
    private final ProductionJobRepository jobRepository;
    private final MachineRepository machineRepository;

    // --- Mappers ---
    private final ProductionJobMapper jobMapper;
    private final MachineMapper machineMapper;

    // --- Kafka Producers ---
    private final KafkaTemplate<String, ProductionStartedEvent> productionStartedKafkaTemplate;
    private final KafkaTemplate<String, ProductionCompletedEvent> productionCompletedKafkaTemplate;
    private final KafkaTemplate<String, JobCancelledEvent> jobCancelledKafkaTemplate;


    // --- Production Job Methods ---

    @Override
    public ProductionJobDto createProductionJob(CreateProductionJobRequestDto requestDto) {
        log.info("Creating new production job for order: {}", requestDto.orderId());
        
        ProductionJob job = new ProductionJob();
        job.setOrderId(requestDto.orderId());
        job.setOutputItemSku(requestDto.outputItemSku());
        job.setStatus(JobStatus.PENDING);

        List<ProductionJobInput> inputs = requestDto.inputs().stream()
                .map(jobMapper::toInputEntity)
                .toList();
        
        inputs.forEach(job::addInput);
        
        ProductionJob savedJob = jobRepository.save(job);
        
        log.info("Created new PENDING job with ID: {}", savedJob.getJobId());
        return jobMapper.toDto(savedJob);
    }

    @Override
    public ProductionJobDto startProductionJob(Long jobId) {
        log.info("Attempting to start production job: {}", jobId);

        ProductionJob job = jobRepository.findById(jobId)
            .orElseThrow(() -> new ResourceNotFoundException("ProductionJob", "id", jobId));

        if (job.getStatus() != JobStatus.PENDING) {
            throw new BusinessLogicException("Job is not in PENDING status.");
        }

        Machine assignedMachine = findAvailableMachine();
        log.info("Found machine: {}. Assigning to job {}.", assignedMachine.getName(), jobId);

        job.setMachine(assignedMachine);
        job.setStatus(JobStatus.IN_PROGRESS);
        assignedMachine.setStatus(MachineStatus.BUSY);

        machineRepository.save(assignedMachine);
        ProductionJob savedJob = jobRepository.save(job);

        ProductionStartedEvent event = new ProductionStartedEvent(
            savedJob.getJobId(),
            savedJob.getOrderId(),
            assignedMachine.getMachineId(),
            LocalDateTime.now()
        );
        productionStartedKafkaTemplate.send("production_started_topic", event);
        log.info("Published ProductionStartedEvent for order {}", event.orderId());

        return jobMapper.toDto(savedJob);
    }

    @Override
    public ProductionJobDto completeProductionJob(Long jobId, CompleteJobRequestDto requestDto) {
        log.info("Attempting to complete production job: {}", jobId);

        ProductionJob job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("ProductionJob", "id", jobId));

        if (job.getStatus() != JobStatus.IN_PROGRESS) {
            throw new BusinessLogicException("Job cannot be completed. Status is not IN_PROGRESS.");
        }
        
        Machine machine = job.getMachine();

        job.setStatus(JobStatus.COMPLETED);
        job.setActualOutputQuantity(requestDto.actualOutputQuantity());
        job.setCompletedAt(LocalDateTime.now());
        ProductionJob savedJob = jobRepository.save(job);

        if (machine != null) {
            machine.setStatus(MachineStatus.AVAILABLE);
            machineRepository.save(machine);
            log.info("Machine {} set back to AVAILABLE.", machine.getName());
        }
        
        log.info("Job {} marked as COMPLETED.", jobId);

        List<ProductionInputDto> inputsConsumed = job.getInputs().stream()
                .map(input -> new ProductionInputDto(
                        input.getInputItemSku(),
                        input.getQuantity()
                ))
                .collect(Collectors.toList());

        ProductionCompletedEvent event = new ProductionCompletedEvent(
                savedJob.getJobId(),
                savedJob.getOrderId(),
                savedJob.getOutputItemSku(),
                savedJob.getActualOutputQuantity(),
                inputsConsumed
        );

        productionCompletedKafkaTemplate.send("production_completed_topic", event);
        log.info("Published ProductionCompletedEvent for order: {}", event.orderId());

        return jobMapper.toDto(savedJob);
    }

    @Override
    public ProductionJobDto cancelProductionJob(Long jobId) {
        log.info("Attempting to cancel production job: {}", jobId);
        
        ProductionJob job = jobRepository.findById(jobId)
            .orElseThrow(() -> new ResourceNotFoundException("ProductionJob", "id", jobId));

        if (job.getStatus() != JobStatus.PENDING) {
            throw new BusinessLogicException("Cannot cancel a job that is already in " + job.getStatus());
        }

        job.setStatus(JobStatus.CANCELLED); 
        ProductionJob savedJob = jobRepository.save(job);
        
        JobCancelledEvent event = new JobCancelledEvent(jobId, job.getOrderId());
        jobCancelledKafkaTemplate.send("job_cancelled_topic", event);
        log.info("Published JobCancelledEvent for order {}", event.orderId());
        
        return jobMapper.toDto(savedJob);
    }

    
    // --- Machine Methods ---

    @Override
    public MachineDto updateMachine(Long machineId, MachineUpdateDto dto) {
        Machine machine = machineRepository.findById(machineId)
            .orElseThrow(() -> new ResourceNotFoundException("Machine", "id", machineId));

        if (machine.getStatus() == MachineStatus.BUSY && dto.status() == MachineStatus.AVAILABLE) {
            throw new BusinessLogicException("Cannot manually update a BUSY machine to AVAILABLE. Must complete its job.");
        }

        machineMapper.updateEntityFromDto(machine, dto);
        
        Machine savedMachine = machineRepository.save(machine);
        return machineMapper.toDto(savedMachine);
    }

    @Override
    public void deleteMachine(Long machineId) {
        Machine machine = machineRepository.findById(machineId)
            .orElseThrow(() -> new ResourceNotFoundException("Machine", "id", machineId));

        if (machine.getStatus() == MachineStatus.BUSY) {
            throw new BusinessLogicException("Cannot delete a machine that is currently BUSY.");
        }
        
        machineRepository.delete(machine);
    }

    // --- Private Helper Methods ---

    private Machine findAvailableMachine() {
        log.info("Searching for an available machine...");
        
        return machineRepository.findFirstByStatus(MachineStatus.AVAILABLE)
                .orElseThrow(() -> new BusinessLogicException("No available machines found. Cannot start job."));
    }
}