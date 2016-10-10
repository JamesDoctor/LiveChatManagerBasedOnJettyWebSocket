(function() {	
	function insertJS(src) {
	  var lc = document.createElement('script');
	  lc.type = 'text/javascript';
	  lc.async = false;
	  lc.src = src;
	  document.body.appendChild(lc);
	}

	function insertCSS(src) {
	  var lc = document.createElement('link');
	  lc.rel = 'stylesheet';
	  lc.type = 'text/css';
	  lc.href = src;
	  var head = document.getElementsByTagName("head")[0];
	  head.appendChild(lc);
	}

	insertJS('scripts/jquery-3.1.1.js');
	insertCSS('css/customerLiveChat.css');
	insertJS('scripts/insertLiveChatWidget.js');
	insertJS('scripts/clientLiveChatHandler.js');
})();