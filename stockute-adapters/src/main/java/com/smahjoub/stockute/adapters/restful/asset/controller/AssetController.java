package com.smahjoub.stockute.adapters.restful.asset.controller;

import com.smahjoub.stockute.adapters.restful.asset.dto.AssetDTO;
import com.smahjoub.stockute.adapters.restful.asset.mapper.AssetMapper;
import com.smahjoub.stockute.application.service.asset.AssetService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
@AllArgsConstructor
public class AssetController {
    private final AssetService assetService;
    private final AssetMapper assetMapper;

    @GetMapping("/portfolios/{portfolioId}/assets")
    public Flux<AssetDTO> getAllAssetsForPortfolio(@PathVariable("portfolioId") final Long portfolioId) {
        return assetService.getAllAssetsForPortfolio(portfolioId).map(assetMapper::toAssetDTO);
    }
}
