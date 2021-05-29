package client.gui.forms;

/**
 * Interface used to pass information back to the frame/form that set the listener
 * in this case ManageUser.
 */
public interface IGetDialogResponse {
    void GetResponse(char[] response, boolean cancelled);
}
