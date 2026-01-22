package peak.modules.settings;

public class NumberSetting extends Setting {

    public double minValue, maxValue, defaultValue, increment;

    public double cValue; // second current (local) value

    public NumberSetting(String name, boolean onArraylist, double minValue, double maxValue, double defaultValue, double increment) {
        this.name = name;
        this.onArraylist = onArraylist;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.defaultValue = defaultValue;
        this.increment = increment;
        this.current_value = String.valueOf(defaultValue);
        this.cValue = defaultValue;
    }

    public void forward() {
        if(cValue + increment <= maxValue) {
            cValue += increment;
            current_value = String.valueOf(cValue);
        }
    }

    public void backward() {
        if(minValue <= Double.parseDouble(current_value) - increment) {
            cValue -= increment;
            current_value = String.valueOf(cValue);
        }
    }

}
