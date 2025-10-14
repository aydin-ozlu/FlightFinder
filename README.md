# Flight Finder Case Study

Bu proje, farklı sağlayıcılardan en uygun uçuşu bulmak için hazırlanmış bir **Uçuş Arama Servisi** case study’sidir. Projede üç ana bölüm bulunmaktadır:

1. **FlightProviderA** – SOAP servis sağlayıcı  
2. **FlightProviderB** – SOAP servis sağlayıcı  
3. **FlightAggregator** – FlightProviderA ve FlightProviderB’den gelen verileri tüketen ve REST servisleri sunan uygulama  

Ayrıca **Postman koleksiyonu**, SOAP ve REST servislerini test etmek için sağlanmıştır.

---

## Proje Yapısı

```
FlightFinder/
│
├─ FlightProviderA/        <-- Maven projesi, SOAP servis
├─ FlightProviderB/        <-- Maven projesi, SOAP servis
├─ FlightAggregator/       <-- Maven projesi, REST aggregator
├─ Postman/                <-- Postman koleksiyonu
│   └─ FlightAggregator.postman_collection.json
```

### FlightProviderA

- **Port:** 8080  
- **SOAP WSDL:** `http://localhost:8080/FlightSearchSOAP?wsdl`  
- **Metod:** `availabilitySearch(SearchRequest)`  
- **Girdi:** `origin`, `destination`, `departureDate`  
- **Çıktı:** `SearchResult` içinde uçuş listesi  

### FlightProviderB

- **Port:** 8081  
- **SOAP WSDL:** `http://localhost:8081/FlightSearchSOAP?wsdl`  
- **Metod:** `availabilitySearch(SearchRequest)`  
- **Girdi:** `departure`, `arrival`, `departureDate`  
- **Çıktı:** `SearchResult` içinde uçuş listesi  

### FlightAggregator

- **Port:** 8082 (application.properties içinde `server.port=8082`)  
- **REST Endpoint’ler:**  
  1. **Tüm uçuşları döndüren:**  
     ```
     GET /flights?origin=IST&destination=COV&departureDate=2025-10-29T09:00
     ```
     - FlightProviderA ve B’den gelen tüm uçuşları birleştirir ve JSON formatında döner.  
     
  2. **En ucuz uçuşları döndüren:**  
     ```
     GET /flights/cheapest?origin=IST&destination=COV&departureDate=2025-10-29T09:00
     ```
     - Uçuşları `uçuş numarası`, `kalkış yeri`, `varış yeri`, `kalkış tarih saati`, `varış tarih saati` alanlarına göre gruplar ve her gruptan en ucuz uçuşu seçerek döner.  

---

## Kurulum ve Çalıştırma

### 1. FlightProviderA ve B
- Her iki proje ayrı ayrı çalıştırılmalıdır:  
```bash
cd FlightProviderA
mvn spring-boot:run

cd FlightProviderB
mvn spring-boot:run
```

### 2. FlightAggregator
- FlightAggregator, SOAP sağlayıcılarını tüketecek şekilde ayarlanmıştır.  
```bash
cd FlightAggregator
mvn spring-boot:run
```

- Eğer port çakışması varsa `application.properties` içinde düzenleyin.  

---

## Test

### 1. SOAP Servisleri
- WSDL kontrolü:  
  - FlightProviderA: `http://localhost:8080/FlightSearchSOAP?wsdl`  
  - FlightProviderB: `http://localhost:8081/FlightSearchSOAP?wsdl`  
- Postman ile SOAP request göndererek `availabilitySearch` metodunu test edebilirsiniz.  

### 2. REST Servisleri
- Tüm uçuşları birleştiren endpoint:  
```
http://localhost:8082/flights?origin=IST&destination=COV&departureDate=2025-10-13T09:00
```
- En ucuz uçuşları döndüren endpoint:  
```
http://localhost:8082/flights/cheapest?origin=IST&destination=COV&departureDate=2025-10-13T09:00
```

### 3. Postman Koleksiyonu
- `Postman/FlightAggregator.postman_collection.json` dosyasını import ederek hem SOAP hem REST servislerini tek tıkla test edebilirsiniz.

---

## Kullanılan Teknolojiler
- Java 17  
- Spring Boot 3.5.6 
- Maven  
- SOAP (JAX-WS)  
- REST (Spring Web)  
- Postman (testler için)  

---

## Notlar
- FlightProviderA ve B servisleri SOAP olarak çalışmalıdır. Aggregator REST olarak sunmaktadır.  
- Soap servislerinin değişmesi durumunda stub'ları tekrar oluşturmak için:
```bash
wsimport.bat -keep -p com.flightprovidera.service -d src/main/java -Xnocompile http://localhost:8080/FlightSearchSOAP?wsdl
wsimport.bat -keep -p com.flightproviderb.service -d src/main/java -Xnocompile http://localhost:8081/FlightSearchSOAP?wsdl
```