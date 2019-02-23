package fonbet.observer.domain;

import java.util.Objects;

import static fonbet.observer.util.NumberUtils.convert;

public class FTimelineDto {
    private final String pair;
    private final String href;
    private final String startTime;
    private final double cFirst;
    private final double cSecond;
    private final double fFirst;
    private final double cfFirst;
    private final double fSecond;
    private final double cfSecond;
    private final double total;
    private final double b;
    private final double m;

    public FTimelineDto(String pair, String href, String startTime, String cFirst, String cSecond, String fFirst, String cfFirst,
            String fSecond, String cfSecond, String total, String b, String m) {
        this.pair = Objects.requireNonNull(pair, "Command pair can't be null.").trim();
        this.href = Objects.requireNonNull(href, "Href can't be null.").trim();
        this.startTime = Objects.requireNonNull(startTime, "Start time can't be null.").trim();
        this.cFirst = convert(Objects.requireNonNull(cFirst).trim());
        this.cSecond = convert(Objects.requireNonNull(cSecond).trim());
        this.fFirst = convert(Objects.requireNonNull(fFirst).trim());
        this.cfFirst = convert(Objects.requireNonNull(cfFirst).trim());
        this.fSecond = convert(Objects.requireNonNull(fSecond).trim());
        this.cfSecond = convert(Objects.requireNonNull(cfSecond).trim());
        this.total = convert(Objects.requireNonNull(total).trim());
        this.b = convert(Objects.requireNonNull(b).trim());
        this.m = convert(Objects.requireNonNull(m).trim());
    }

    public String getPair() {
        return pair;
    }

    public String getHref() {
        return href;
    }

    public String getStartTime() {
        return startTime;
    }

    public double getCFirst() {
        return cFirst;
    }

    public double getCSecond() {
        return cSecond;
    }

    public double getFFirst() {
        return fFirst;
    }

    public double getCFFirst() {
        return cfFirst;
    }

    public double getFSecond() {
        return fSecond;
    }

    public double getCFSecond() {
        return cfSecond;
    }

    public double getTotal() {
        return total;
    }

    public double getB() {
        return b;
    }

    public double getM() {
        return m;
    }

    @Override
    public String toString() {
        return "Match{" +
                "pair='" + pair + '\'' +
                ", href='" + href + '\'' +
                ", startTime='" + startTime + '\'' +
                ", cFirst='" + cFirst + '\'' +
                ", cSecond='" + cSecond + '\'' +
                ", fFirst='" + fFirst + '\'' +
                ", cfFirst='" + cfFirst + '\'' +
                ", fSecond='" + fSecond + '\'' +
                ", cfSecond='" + cfSecond + '\'' +
                ", total='" + total + '\'' +
                ", b='" + b + '\'' +
                ", m='" + m + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FTimelineDto that = (FTimelineDto) o;
        return Double.compare(that.cFirst, cFirst) == 0 &&
                Double.compare(that.cSecond, cSecond) == 0 &&
                Double.compare(that.fFirst, fFirst) == 0 &&
                Double.compare(that.cfFirst, cfFirst) == 0 &&
                Double.compare(that.fSecond, fSecond) == 0 &&
                Double.compare(that.cfSecond, cfSecond) == 0 &&
                Double.compare(that.total, total) == 0 &&
                Double.compare(that.b, b) == 0 &&
                Double.compare(that.m, m) == 0 &&
                Objects.equals(pair, that.pair) &&
                Objects.equals(href, that.href) &&
                Objects.equals(startTime, that.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pair, href, startTime, cFirst, cSecond, fFirst, cfFirst, fSecond, cfSecond, total, b, m);
    }
}
