package dev.ceccon.transactionconverter.integration.ustreasuryapi;

import java.util.List;
import java.util.Map;

public class USTreasuryRatesOfExchangeAPIResponseDTO {

    private List<ExchangeRateRecordDTO> data;
    private Map<String, Object> meta;
    private Map<String, Object> link;

    public USTreasuryRatesOfExchangeAPIResponseDTO() {

    }

    public USTreasuryRatesOfExchangeAPIResponseDTO(List<ExchangeRateRecordDTO> data, Map<String, Object> meta, Map<String, Object> link) {
        this.data = data;
        this.meta = meta;
        this.link = link;
    }

    public List<ExchangeRateRecordDTO> getData() {
        return data;
    }

    public void setData(List<ExchangeRateRecordDTO> data) {
        this.data = data;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }

    public Map<String, Object> getLink() {
        return link;
    }

    public void setLink(Map<String, Object> link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "USTreasuryRatesOfExchangeAPIResponseDTO{" +
                "data=" + data +
                ", meta=" + meta +
                ", link=" + link +
                '}';
    }
}
