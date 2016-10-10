(function() {
	function setupLiveChat() {
		var ws = null;
		var chatBoxFullView = true;
		var selectors = {
			CHAT_RECORDS : "#lc-chatRecords",
			CHAT_MIN : "#liveChat-minimized",
			CHAT_FULL : "#liveChat-full",
			START_CHAT_CONTAINER : "#lc-startChat-container",
			SEND_MESSAGE_CONTAINER : "#lc-sendMessage-container",
			FULL_VIEW_BTN : "#lc-full-view-button",
			MIN_VIEW_BTN : "#lc-minimize",
			CLOSE_CHAT_BTN : "#lc-close-chat",
			START_CHAT_BTN : ".lc-startChatBtn",
			SEND_MSG_BTN : ".lc-sendMessageBtn",
			INPUT_MSG : ".lc-inputMessage",
			BODY : "body"
		};
		
		var styles = {
			VISIBILITY : "visibility",
			VISIBLE : "visible",
			HIDDEN : "hidden"
		};
		
		function startCommunication() {
			if (ws) {
				return;
			}
			try {
				ws = new WebSocket("ws://localhost:8080/Customer/events?Company=ABCLtd"); 
			} catch (e) {
				appendChatRecord(e);
			}
			
			ws.onopen = function(){
				appendChatRecord("Connected to Customer Service.");
				toggleChatContoller();
			}; 
			ws.onmessage = function(evt){
				console.log(evt.data);
				appendChatRecord(evt.data);
			}; 
			ws.onclose = function(evt){
				ws = null;
				appendChatRecord("Chat closed!");
				toggleChatContoller();
			}; 
			ws.onerror = function(evt){
				appendChatRecord("Error in communicating with Customer Service!");
			};
		}
		
		function stopCommunication() {
			if (!ws) {
				return;
			}
			ws.close();
		}
		
		function appendChatRecord(message) {
			if (message) {
				$(selectors.CHAT_RECORDS).append("<div class='lc-chatMessage'>" + message + "</div>");
				$(selectors.CHAT_RECORDS)[0].scrollTop = $(selectors.CHAT_RECORDS)[0].scrollHeight;
			}
		}
		
		function toggleWidget(fullView, minimizedView) {
			var fullViewStyle = fullView ? styles.VISIBLE : styles.HIDDEN;
			var minimizedViewStyle = minimizedView ? styles.VISIBLE : styles.HIDDEN;
			$(selectors.CHAT_MIN).css(styles.VISIBILITY, minimizedViewStyle);
			$(selectors.CHAT_FULL).css(styles.VISIBILITY, fullViewStyle);
		}
		
		function toggleChatContoller() {
			var startChatStyle = ((!ws) && chatBoxFullView) ? styles.VISIBLE : styles.HIDDEN;
			var sendMsgStyle = (ws && chatBoxFullView) ? styles.VISIBLE : styles.HIDDEN;
			$(selectors.START_CHAT_CONTAINER).css(styles.VISIBILITY, startChatStyle);
			$(selectors.SEND_MESSAGE_CONTAINER).css(styles.VISIBILITY, sendMsgStyle);
		}
		
		$(selectors.BODY).on("click", selectors.FULL_VIEW_BTN, function() {
			toggleWidget(true, false);
			chatBoxFullView = true;
			toggleChatContoller();
		});
		
		$(selectors.BODY).on("click", selectors.MIN_VIEW_BTN, function() {
			toggleWidget(false, true);
			chatBoxFullView = false;
			toggleChatContoller();
		});
		
		$(selectors.BODY).on("click", selectors.CLOSE_CHAT_BTN, function() {
			stopCommunication();
			toggleWidget(false, false);
			$(selectors.CHAT_MIN).remove();
			$(selectors.CHAT_FULL).remove();
		});
		
		$(selectors.BODY).on("click", selectors.START_CHAT_BTN, function() {
			startCommunication();
		});
		
		$(selectors.BODY).on("click", selectors.SEND_MSG_BTN, function() {
			var msg = $(selectors.INPUT_MSG)[0].value;
			if (msg) {
				ws.send(msg);
				appendChatRecord("Me: " + msg);
				$(selectors.INPUT_MSG)[0].value = "";
			}
		});
	}
	
	setupLiveChat();
})();