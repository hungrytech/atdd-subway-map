package subway.domain;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SubwayLine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String color;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UP_STATION_ID")
	private Station upStation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOWN_STATION_ID")
	private Station downStation;

	@Embedded
	private final Sections sections = new Sections();

	public SubwayLine(
		String name,
		String color,
		Long upStationId,
		Long downStationId,
		int distance,
		List<Station> stations) {
		this.name = name;
		this.color = color;

		Map<Long, Station> stationMap = toMap(stations);

		this.upStation = stationMap.get(upStationId);
		this.downStation = stationMap.get(downStationId);
		this.sections.add(createInitialSection(stationMap, distance));
	}

	public void updateInfo(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public List<Station> getUpAndDownStations() {
		return List.of(upStation, downStation);
	}

	private Map<Long, Station> toMap(List<Station> stations) {
		return stations.stream()
			.collect(Collectors.toMap(Station::getId, Function.identity()));
	}

	private Section createInitialSection(Map<Long, Station> stationMap, int distance) {
		return new Section(
			stationMap.get(this.upStation.getId()),
			stationMap.get(this.downStation.getId()),
			distance,
			this
		);
	}

	public void updateSection(Section section) {
		this.downStation = section.getDownStation();

		section.connectSubwayLine(this);
		sections.add(section);
	}
}
