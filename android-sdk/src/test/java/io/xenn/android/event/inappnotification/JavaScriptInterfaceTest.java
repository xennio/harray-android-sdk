package io.xenn.android.event.inappnotification;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@RunWith(MockitoJUnitRunner.class)
public class JavaScriptInterfaceTest {

    @Mock
    private InAppNotificationViewManager inAppNotificationViewManager;

    @Test
    public void it_should_dismiss_in_app_notification_view_for_close_event() {
        JavaScriptInterface javaScriptInterface = new JavaScriptInterface(inAppNotificationViewManager);

        javaScriptInterface.postMessage("{\"eventType\": \"close\"}");

        verify(inAppNotificationViewManager).dismiss();
    }

    @Test
    public void it_should_adjust_popup_view_height_on_render_complete() {
        JavaScriptInterface javaScriptInterface = new JavaScriptInterface(inAppNotificationViewManager);

        javaScriptInterface.postMessage("{\"eventType\": \"renderCompleted\"}");

        verify(inAppNotificationViewManager).adjustHeight();
    }

    @Test
    public void it_should_trigger_user_defined_link_click_handler_on_linkClicked_event() {
        JavaScriptInterface javaScriptInterface = new JavaScriptInterface(inAppNotificationViewManager);

        javaScriptInterface.postMessage("{\"eventType\": \"linkClicked\", \"link\": \"https://xenn.io\"}");

        verify(inAppNotificationViewManager).triggerUserDefinedLinkClickHandler("https://xenn.io");
    }

    @Test
    public void it_should_not_do_anything_if_eventType_is_unknown() {
        JavaScriptInterface javaScriptInterface = new JavaScriptInterface(inAppNotificationViewManager);

        javaScriptInterface.postMessage("{\"eventType\": \"unknownEvent\"}");

        verifyNoInteractions(inAppNotificationViewManager);
    }

    @Test
    public void it_should_not_do_anything_if_input_json_is_invalid() {
        JavaScriptInterface javaScriptInterface = new JavaScriptInterface(inAppNotificationViewManager);

        javaScriptInterface.postMessage("111");

        verifyNoInteractions(inAppNotificationViewManager);
    }
}