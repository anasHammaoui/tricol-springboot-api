package com.example.tricol.tricolspringbootrestapi.service.impl;

import com.example.tricol.tricolspringbootrestapi.dto.request.CreateExitSlipRequest;
import com.example.tricol.tricolspringbootrestapi.dto.request.ExitSlipItemRequest;
import com.example.tricol.tricolspringbootrestapi.dto.response.ExitSlipResponse;
import com.example.tricol.tricolspringbootrestapi.enums.ExitSlipStatus;
import com.example.tricol.tricolspringbootrestapi.mapper.ExitSlipMapper;
import com.example.tricol.tricolspringbootrestapi.model.*;
import com.example.tricol.tricolspringbootrestapi.repository.ExitSlipRepository;
import com.example.tricol.tricolspringbootrestapi.repository.ProductRepository;
import com.example.tricol.tricolspringbootrestapi.repository.StockMovementRepository;
import com.example.tricol.tricolspringbootrestapi.repository.StockSlotRepository;
import com.example.tricol.tricolspringbootrestapi.service.ExitSlipService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExitSlipServiceImpl implements ExitSlipService {
    
    private final ExitSlipRepository exitSlipRepository;
    private final ProductRepository productRepository;
    private final ExitSlipMapper exitSlipMapper;
    
    @Transactional
    public ExitSlipResponse createExitSlip(CreateExitSlipRequest request) {
        ExitSlip exitSlip = new ExitSlip();
        exitSlip.setSlipNumber(generateSlipNumber());
        exitSlip.setExitDate(request.getExitDate());
        exitSlip.setDestinationWorkshop(request.getDestinationWorkshop());
        exitSlip.setReason(request.getReason());
        exitSlip.setComment(request.getComment());
        exitSlip.setStatus(ExitSlipStatus.DRAFT);
        exitSlip.setCreatedBy("SYSTEM"); 
        
        // Add items
        for (ExitSlipItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + itemRequest.getProductId()));
            
            ExitSlipItem item = new ExitSlipItem();
            item.setExitSlip(exitSlip);
            item.setProduct(product);
            item.setRequestedQuantity(itemRequest.getQuantity());
            item.setNote(itemRequest.getNote());
            
            exitSlip.getItems().add(item);
        }
        
        ExitSlip saved = exitSlipRepository.save(exitSlip);
        return exitSlipMapper.toResponse(saved);
    }
    @Transactional
    public ExitSlipResponse cancelExitSlip(Long id) {
        ExitSlip exitSlip = exitSlipRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Exit slip not found: " + id));
        
        if (exitSlip.getStatus() == ExitSlipStatus.CANCELLED) {
            throw new RuntimeException("Exit slip is already cancelled");
        }
        
        if (exitSlip.getStatus() == ExitSlipStatus.VALIDATED) {
            throw new RuntimeException("Cannot cancel a validated exit slip (stock already consumed)");
        }
        
        exitSlip.setStatus(ExitSlipStatus.CANCELLED);
        exitSlip.setCancelledAt(LocalDateTime.now());
        exitSlip.setCancelledBy("SYSTEM"); 
        
        ExitSlip cancelled = exitSlipRepository.save(exitSlip);
        return exitSlipMapper.toResponse(cancelled);
    }
    
    public ExitSlipResponse getExitSlip(Long id) {
        ExitSlip exitSlip = exitSlipRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Exit slip not found: " + id));
        return exitSlipMapper.toResponse(exitSlip);
    }
    
    public List<ExitSlipResponse> getAllExitSlips() {
        return exitSlipMapper.toResponseList(exitSlipRepository.findAll());
    }

    public List<ExitSlipResponse> getExitSlipsByStatus(ExitSlipStatus status) {
        return exitSlipMapper.toResponseList(exitSlipRepository.findByStatus(status));
    }
    
    public List<ExitSlipResponse> getExitSlipsByWorkshop(String workshop) {
        return exitSlipMapper.toResponseList(exitSlipRepository.findByDestinationWorkshop(workshop));
    }

    private String generateSlipNumber() {
        String prefix = "BS";
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = exitSlipRepository.count() + 1;
        return String.format("%s-%s-%04d", prefix, date, count);
    }
}
