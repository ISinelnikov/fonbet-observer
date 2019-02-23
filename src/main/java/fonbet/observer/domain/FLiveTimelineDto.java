package fonbet.observer.domain;

import java.util.Objects;

public class FLiveTimelineDto {
    private final String pairName;

    private final FTimelineDto timelineDto;

    public FLiveTimelineDto(String pairName, String part, String href, String startTime,
            String cFirst, String cSecond, String fFirst,
            String cfFirst, String fSecond, String cfSecond,
            String total, String b, String m) {
        timelineDto = new FTimelineDto(part, href, startTime,
                cFirst, cSecond,
                fFirst, cfFirst,
                fSecond, cfSecond,
                total, b, m);
        this.pairName = Objects.requireNonNull(pairName, "Pair name can't be null.");
    }

    public String getPairName() {
        return pairName;
    }

    public FTimelineDto getTimelineDto() {
        return timelineDto;
    }

    @Override
    public String toString() {
        return "FLiveTimelineDto{" +
                "pairName='" + pairName + '\'' +
                ", timelineDto=" + timelineDto +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FLiveTimelineDto)) {
            return false;
        }
        FLiveTimelineDto that = (FLiveTimelineDto) o;
        return getPairName().equals(that.getPairName()) &&
                getTimelineDto().equals(that.getTimelineDto());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPairName(), getTimelineDto());
    }
}
