package peak.modules.settings;

public class NumberSetting extends Setting {

    public boolean dragging;

    public double minValue, maxValue, defaultValue, increment;

    public double cValue; // second current (local) value

    public NumberSetting(String name, boolean onArraylist, double minValue, double maxValue, double defaultValue, double increment) {
        this.name = name;
        this.onArraylist = onArraylist;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.defaultValue = defaultValue;
        this.increment = increment;
        this.currentValue = String.valueOf(defaultValue);
        this.cValue = defaultValue;
    }

    public NumberSetting(String name, Setting boundSetting, String[] neededValues, boolean onArraylist, double minValue, double maxValue, double defaultValue, double increment) {
        this.name = name;
        this.onArraylist = onArraylist;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.defaultValue = defaultValue;
        this.increment = increment;
        this.currentValue = String.valueOf(defaultValue);
        this.cValue = defaultValue;
        this.boundSetting = boundSetting;
        this.neededValues = neededValues;
        this.display = shouldDisplay(boundSetting, neededValues);
    }

    public void forward() {
        if(cValue + increment <= maxValue) {
            cValue += increment;
            currentValue = String.valueOf(cValue);
        }
    }

    public void backward() {
        if(minValue <= Double.parseDouble(currentValue) - increment) {
            cValue -= increment;
            currentValue = String.valueOf(cValue);
        }
    }

    public void setcValue(double value) {
        double precision = 1.0 / increment;
        this.cValue = Math.round(Math.max(minValue, Math.min(maxValue, value)) * precision) / precision;
        this.currentValue = String.valueOf(cValue);
    }

}
