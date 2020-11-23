//package io.xenn.hmskit;
//
//import android.app.NotificationManager;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//
//import androidx.core.app.NotificationCompat;
//
//import java.util.Map;
//
//import io.xenn.hmskit.common.Constants;
//import io.xenn.hmskit.common.PushMessageDataWrapper;
//import io.xenn.hmskit.model.FeedbackEvent;
//
//public class NotificationProcessorHandler {
//
//        public void handleHuaweiPushNotification(Context applicationContext, com.huawei.hms.push.RemoteMessage remoteMessage) {
//        try {
//            Map<String, String> data = remoteMessage.getDataOfMap();
//            PushMessageDataWrapper pushMessageDataWrapper = PushMessageDataWrapper.from(data);
//            if (pushMessageDataWrapper.getSource().equals(Constants.PUSH_CHANNEL_ID)) {
//                sessionContextHolder.updateExternalParameters(pushMessageDataWrapper.toObjectMap());
//                this.pushMessageDelivered(pushMessageDataWrapper);
//
//                if (pushMessageDataWrapper.isSilent()) {
//                    this.pushMessageOpened(pushMessageDataWrapper);
//                    return;
//                }
//
//                String notificationChannelId = pushMessageDataWrapper.buildChannelId();
//                NotificationManager notificationManager = (NotificationManager) applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager != null) {
//                    NotificationChannelBuilder.create(deviceService).withChannelId(notificationChannelId).withSound(pushMessageDataWrapper.getSound()).createIn(notificationManager);
//                }
//                NotificationCompat.Builder notificationCompatBuilder = NotificationCompatBuilder.create(applicationContext, httpService, deviceService)
//                        .withChannelId(notificationChannelId)
//                        .withApplicationLogo(pushMessageDataWrapper.getApplicationLogo())
//                        .withTitle(pushMessageDataWrapper.getTitle())
//                        .withSubtitle(pushMessageDataWrapper.getSubTitle())
//                        .withMessage(pushMessageDataWrapper.getMessage())
//                        .withBadge(pushMessageDataWrapper.getBadge())
//                        .withSound(pushMessageDataWrapper.getSound())
//                        .withImage(pushMessageDataWrapper.getImageUrl(), pushMessageDataWrapper.getMessage())
//                        .withIntent(data)
//                        .build();
//
//                notificationManager.notify(0, notificationCompatBuilder.build());
//            }
//        } catch (Exception e) {
////            XennioLogger.log("Xenn Push handle error:" + e.getMessage());
//        }
//    }
//
//    protected void pushMessageDelivered(PushMessageDataWrapper pushMessageDataWrapper) {
//        try {
//            Map<String, Object> event = new FeedbackEvent("d",
//                    pushMessageDataWrapper.getPushId(),
//                    pushMessageDataWrapper.getCampaignId(),
//                    pushMessageDataWrapper.getCampaignDate()).toMap();
//
//            String serializedEntity = entitySerializerService.serializeToJson(event);
//            httpService.postJsonEncoded(serializedEntity, Constants.PUSH_FEED_BACK_PATH);
//
//        } catch (Exception e) {
//            XennioLogger.log("Push received event error: " + e.getMessage());
//        }
//    }
//
//    public void pushMessageOpened(Intent intent) {
//        PushMessageDataWrapper pushMessageDataWrapper = PushMessageDataWrapper.from(intent);
//        pushMessageOpened(pushMessageDataWrapper);
//    }
//
//    protected void pushMessageOpened(PushMessageDataWrapper pushMessageDataWrapper) {
//        if (pushMessageDataWrapper.getSource().equals(Constants.PUSH_CHANNEL_ID)) {
//            try {
//                Map<String, Object> event = new FeedbackEvent("o",
//                        pushMessageDataWrapper.getPushId(),
//                        pushMessageDataWrapper.getCampaignId(),
//                        pushMessageDataWrapper.getCampaignDate()).toMap();
//
//                String serializedEntity = entitySerializerService.serializeToJson(event);
//                httpService.postJsonEncoded(serializedEntity, Constants.PUSH_FEED_BACK_PATH);
//
//            } catch (Exception e) {
//                XennioLogger.log("Push opened event error: " + e.getMessage());
//            }
//        }
//
//    }
//}
