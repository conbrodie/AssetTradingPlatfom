package client;

/**
 * Interface used to pass information back to the frame/form that set the listener
 * in this case MainFrame.
 */
public interface ITradePanelListener {
    public void onSubmitButtonClick(String trade_type, int asset_id, String asset_name, int quantity, int price);
    public void onAssetSelected(int asset_id, String asset_name);
}
