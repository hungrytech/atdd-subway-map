package subway.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subway.domain.Section;
import subway.domain.SectionRepository;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.domain.SubwayLine;
import subway.domain.SubwayLineRepository;
import subway.exception.NotFoundSectionException;
import subway.exception.NotFoundSubwayLineException;
import subway.exception.SectionErrorCode;
import subway.exception.SubwayLineErrorCode;
import subway.presentation.request.SectionRequest;

@Service
@RequiredArgsConstructor
public class SectionService {

	private final SectionRepository sectionRepository;

	private final SubwayLineRepository subwayLineRepository;

	private final StationRepository stationRepository;

	@Transactional
	public Long createSection(Long subwayLineId, SectionRequest.Create createRequest) {
		SubwayLine subwayLine = subwayLineRepository.findSubwayLineById(subwayLineId)
			.orElseThrow(() -> new NotFoundSubwayLineException(SubwayLineErrorCode.NOT_FOUND_SUBWAY_LINE));

		List<Station> upAndDownStations = stationRepository.findByIdIn(createRequest.getUpAndDownStationIds());

		Section section = sectionRepository.save(createRequest.toEntity(upAndDownStations));

		subwayLine.updateSection(section);
		return section.getId();
	}

	@Transactional
	public void deleteSection(Long subwayLineId, Long stationId) {
		Section section = sectionRepository.findSectionByDownStationId(stationId)
			.orElseThrow(() -> new NotFoundSectionException(SectionErrorCode.NOT_FOUND_SECTION));

		SubwayLine subwayLine = subwayLineRepository.findSubwayLineById(subwayLineId)
			.orElseThrow(() -> new NotFoundSubwayLineException(SubwayLineErrorCode.NOT_FOUND_SUBWAY_LINE));

		// subwayLine.removeSection(section);
		// subwayLine.validatePossibleRemove(section);
		//
		// Station newDownStation = stationRepository.findById(section.getUpStationId())
		// 	.orElseThrow(() -> new NotFoundStationException(SectionErrorCode.NOT_FOUND_STATION));
		// subwayLine.removeSection(section, newDownStation);
	}
}
