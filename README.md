# homework_sertificates_2
Реализована функция получения всех TSL сертификатов из Keystore,   
вычисления количества дней до окончания их срока действия и передача   
этой информации в кастомную метрику Spring Actuator.  

Посмотреть сертификаты можно по запросу:
http://localhost:8080/actuator/prometheus

При запуске программы необходимо настроить данные в файле  
_resources/certStore.properties_  
Необходимо указать располажение хранилища сертификатов.
