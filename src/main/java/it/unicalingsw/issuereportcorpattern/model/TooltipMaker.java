package it.unicalingsw.issuereportcorpattern.model;

import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class TooltipMaker {
    public static Tooltip makeTooltip(String text) {
        Tooltip tooltip = new javafx.scene.control.Tooltip(text);
        tooltip.setShowDelay(Duration.millis(5.0));
        tooltip.setHideDelay(Duration.millis(5.0));
        return tooltip;
    }
}
