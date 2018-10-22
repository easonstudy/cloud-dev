package com.lottery.center.socket;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
public class ServerDemo {
	
	public static void start() {
		try {
			Configuration config = new Configuration();
			config.setHostname("localhost");
			config.setPort(9092);


            if(isOSLinux()){
                //使用Linux Epoll模型
                log.info("启用Epoll");
                config.setUseLinuxNativeEpoll(true);
            }

			config.setAuthorizationListener(new AuthorizationListener() {
				@Override
				public boolean isAuthorized(HandshakeData data) {
					log.info("握手数据"+data.toString());
					return true;
				}
			});
			
			SocketIOServer server = new SocketIOServer(config);
			
			CharteventListener listner = new CharteventListener();
			listner.setServer(server);
			server.addEventListener("chatevent", ChatObject.class, listner);
			
			server.addConnectListener(new ConnectListener() {
				@Override
				public void onConnect(SocketIOClient client) {
					System.out.println("SessionId=" + client.getSessionId());
				}
			});

			server.addDisconnectListener(new DisconnectListener() {
				@Override
				public void onDisconnect(SocketIOClient client) {
					// TODO Auto-generated method stub
					System.out.println("SessionId=" + client.getSessionId());
				}
			});
			
			server.start();
			
			System.out.println("start success");
			Thread.sleep(Integer.MAX_VALUE);
			server.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public static boolean isOSLinux() {
        Properties prop = System.getProperties();
        String os = prop.getProperty("os.name");
        System.out.println(os);
        if (os != null && os.toLowerCase().indexOf("linux") > -1) {
            return true;
        } else {
            return false;
        }
    }
	
	class ChatObject {

		private String userName;

		private String message;

		public ChatObject() {
		}

		public ChatObject(String userName, String message) {
			this.userName = userName;
			this.message = message;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
	
	static class CharteventListener implements DataListener<ChatObject> {

		SocketIOServer server;

		public void setServer(SocketIOServer server) {
			this.server = server;
		}
		
		public void onData(SocketIOClient client, ChatObject data, AckRequest ackSender) throws Exception {
			// TODO Auto-generated method stub
			System.out.println("username:"+ data.getUserName() +" msg:"+data.getMessage());
			data.setMessage("server");
			this.server.getBroadcastOperations().sendEvent("broad", data);
		}

	}

}
