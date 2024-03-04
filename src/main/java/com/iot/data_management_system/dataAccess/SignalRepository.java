package com.iot.data_management_system.dataAccess;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iot.data_management_system.entities.Signal;

public interface SignalRepository extends JpaRepository<Signal, Integer> {

	List<Signal> findByDeviceId(int id);

	@Query(value = "SELECT EXISTS (SELECT * FROM signals s WHERE CAST(s.timestamp AS DATE) = :date", nativeQuery = true)
	boolean existsByLocalDate(LocalDate date);

	@Query(value = "SELECT * FROM signals signal WHERE CAST(signal.timestamp AS DATE) = :date", nativeQuery = true)
	List<Signal> findByDate(@Param("date") LocalDate date);

	@Query(value = "SELECT *\r\n" + "FROM signals\r\n" + "WHERE (:condition = 0 AND temperature = :temperature)\r\n"
			+ "OR (:condition = 1 AND temperature < :temperature)\r\n"
			+ "OR (:condition = 2 AND temperature > :temperature);", nativeQuery = true)
	List<Signal> findByTemperature(@Param("temperature") double temperature, @Param("condition") int condition);

	@Query(value = "SELECT *\r\n" + "FROM signals\r\n" + "WHERE (:condition = 0 AND humidity = :humidity)\r\n"
			+ "OR (:condition = 1 AND humidity < :humidity)\r\n"
			+ "OR (:condition = 2 AND humidity > :humidity);", nativeQuery = true)
	List<Signal> findByHumidity(@Param("humidity") double humidity, @Param("condition") int condition);

	@Query(value = "SELECT *\r\n" + "FROM signals\r\n" + "WHERE (:condition = 0 AND wind_speed = :windSpeed)\r\n"
			+ "OR (:condition = 1 AND wind_speed < :windSpeed)\r\n"
			+ "OR (:condition = 2 AND wind_speed > :windSpeed);", nativeQuery = true)
	List<Signal> findByWindSpeed(@Param("windSpeed") double windSpeed, @Param("condition") int condition);

	@Query(value = "SELECT *\r\n" + "FROM signals\r\n"
			+ "WHERE (:condition = 0 AND light_intensity = :lightIntensity)\r\n"
			+ "OR (:condition = 1 AND light_intensity < :lightIntensity)\r\n"
			+ "OR (:condition = 2 AND light_intensity > :lightIntensity);", nativeQuery = true)
	List<Signal> findByLightIntensity(@Param("lightIntensity") double lightIntensity,
			@Param("condition") int condition);
}
