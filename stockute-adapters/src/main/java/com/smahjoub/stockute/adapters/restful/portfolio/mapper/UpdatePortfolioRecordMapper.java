package com.smahjoub.stockute.adapters.restful.portfolio.mapper;

import com.smahjoub.stockute.adapters.restful.portfolio.dto.CreatePortfolioDTO;
import com.smahjoub.stockute.adapters.restful.portfolio.dto.UpdatePortfolioDTO;
import com.smahjoub.stockute.domain.model.Portfolio;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public abstract class UpdatePortfolioRecordMapper {

    /**
     * Maps a CreatePortfolioDTO to a Portfolio entity.
     *
     * @param dto the CreatePortfolioDTO to map
     * @return the mapped Portfolio entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userRefId", ignore = true)
    @Mapping(target = "currency", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    public abstract Portfolio toPortfolio(UpdatePortfolioDTO dto);

    @BeforeMapping
    protected void setCreationAndModificationDate(UpdatePortfolioDTO dto, @MappingTarget Portfolio portfolio) {
        final LocalDateTime now = LocalDateTime.now();
        portfolio.setCreatedDate(now);
        portfolio.setLastModifiedDate(now);
    }

    public Portfolio updatePortfolioFromDto(UpdatePortfolioDTO dto, Portfolio existingPortfolio) {
        final Portfolio portfolio = toPortfolio(dto);
        portfolio.setId(existingPortfolio.getId());
        portfolio.setUserRefId(existingPortfolio.getUserRefId());
        portfolio.setCurrency(existingPortfolio.getCurrency());
        portfolio.setCreatedDate(existingPortfolio.getCreatedDate());
        portfolio.setLastModifiedDate(LocalDateTime.now());
        portfolio.setVersion(existingPortfolio.getVersion() + 1);
        return portfolio;
    }
}

