package com.aggregator.client;

import com.aggregator.model.FlightDTO;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class FlightAggregatorClient implements FlightClient {
        private final ExecutorService executor;
        private final List<FlightProvider> providers;

        public FlightAggregatorClient(ExecutorService executor, ProviderA providerA, ProviderB providerB) {
                this.executor = executor;
                // Provider listesi - yeni sağlayıcılar buraya eklenebilir
                // Spring dependency injection ile provider'lar inject ediliyor
                this.providers = Arrays.asList(providerA, providerB);
        }

        public List<FlightDTO> getAllFlights(String origin, String destination, LocalDateTime departureDate)
                        throws Exception {
                // Tüm sağlayıcılara paralel istek gönder
                List<CompletableFuture<List<FlightDTO>>> futures = providers.stream()
                                .map(provider -> CompletableFuture.supplyAsync(
                                                () -> provider.searchFlights(origin, destination, departureDate),
                                                executor))
                                .collect(Collectors.toList());

                // Tüm isteklerin tamamlanmasını bekle
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

                // Tüm sonuçları birleştir
                return futures.stream()
                                .map(CompletableFuture::join)
                                .flatMap(List::stream)
                                .collect(Collectors.toList());
        }

}
