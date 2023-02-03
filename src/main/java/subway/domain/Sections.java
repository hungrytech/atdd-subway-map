package subway.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Sections {

	@OneToMany(
		mappedBy = "subwayLine",
		cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
		orphanRemoval = true
	)
	private final List<Section> sections = new ArrayList<>();

	public void add(Section section) {
		this.sections.add(section);
	}

}
