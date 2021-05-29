package client;

/**
 * Interface used to pass information back to the frame/form that set the listener
 * in this case MainFrame.
 */
public interface IStatusPanelListener {
    public void onRefreshButtonClick();
    public void onFilterSelect(boolean enablefilter);
}
