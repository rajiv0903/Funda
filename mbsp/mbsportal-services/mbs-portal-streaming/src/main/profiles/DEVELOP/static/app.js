var stompClient = null;
var host = 'https://localhost:8443';
var destinationPrefix = '/mbsp/topic/';
var contextPath = '/mbsp-streaming';
var topic = "test";

var headers = {
	'x-fnma-channel':'web',
	'x-fnma-sub-channel':'MBSP',
	'x-fnma-sessionid' : '5cdb6fb4-d452-478a-bcec-39189d01e331'
};
var userName = null;
var sessionId = null;
var registered = false;
var receivedMsgCount = 0;

function setConnected(connected) {
	$("#connect").prop("disabled", connected);
	$("#disconnect").prop("disabled", !connected);
	if (connected) {
		$("#conversation").show();
		$("#disconnect").focus();
	} else {
		$("#conversation").hide();
	}
	$("#greetings").html("");
	$("#name").prop("disabled", registered);
	$("#sessionid").prop("disabled", registered);
	$("#register").prop("disabled", registered);
	if(!registered){
		$("#name").prop("disabled", registered);
		$("#sessionid").prop("disabled", registered);
		$("#register").prop("disabled", registered);
	}
}

function connect() {
	console.log('Before Connect:'+headers);
	var socket = new SockJS(host+contextPath, null, headers);
	stompClient = Stomp.over(socket);
	var destination  = destinationPrefix + topic +"/"+ userName;
	stompClient.connect(headers, function(frame) {
		setConnected(true);
		stompClient.subscribe(destinationPrefix + topic +"/"+ userName, function(greeting) {
			//console.log('greeting.body:'+greeting.body);
			//showGreeting(JSON.parse(greeting.body).content);
			showGreeting(greeting.body);
			receivedMsgCount++;
			if(receivedMsgCount % 20 == 0){
				modifyGreetingHeader();
			}
		}, headers);
	}, error_callback);
}

var error_callback = function(error) {
	showErrorAlert(error);
  };


function disconnect() {
	if (stompClient !== null) {
		stompClient.disconnect(null, headers);
	}
	unRegisterUserName();
	receivedMsgCount = 0;
	resetGreetingHeader();
	setConnected(false);
}

function unRegisterUserName() {
	var url = host+contextPath+"/unregister/" + topic +"/"+ userName;
	$.post(url, function(data, status) {
		alert("Data: " + data + "\nStatus: " + status);
	});
	registered = false;
}

function registerUserName() {
	var url = host+contextPath+"/register/" + topic +"/"+ userName;
	$.post(url, function(data, status) {
		alert("Data: " + data + "\nStatus: " + status);
	});
	registered = true;
	setConnected(false);
}

function showGreeting(message) {
	$("#greetings").append("<tr><td>"+ "["+new Date().toString()+"] Message: " + message + "</td></tr>");
}

function modifyGreetingHeader() {
	$("#theader").empty();
	$("#theader").append("<strong>Greetings!</strong>" +
			" You have received total "+ receivedMsgCount + " messages so far.., clearing the table content");
	// repeat with the interval of 2 seconds
	let timerId = setInterval(function(){blinkGreetings()}, 1000);
	// after 5 seconds stop
	setTimeout(() => { stopBlinkGreetings(timerId)}, 5000);   
	
	$("#greetings").empty();
}

function resetGreetingHeader() {
	$("#theader").empty();
	$("#theader").append("<strong>Greetings!</strong>");
	$("#greetings").empty();
}

function blinkGreetings() {
	  $("#theader").fadeTo(100, 0.1).fadeTo(200, 1.0);
	  $("#theader").css('background-color', 'orange');
	  // $("#theader").css('margin', '10px');
	  $("#theader").css('padding-bottom', '10px');
	}
function stopBlinkGreetings(timerId) {
 	clearInterval(timerId);
 	$("#theader").fadeTo(100, 0.1).fadeTo(200, 1.0);
 	$("#theader").css('background-color', 'white');
 	// $("#theader").css('margin', '10px');
 	$("#theader").css('padding-bottom', '10px');
}

$(function() {
	$("form").on('submit', function(e) {
		e.preventDefault();
	});
	$("#connect").click(function() {
		if (registered) {
			connect();
		}else{
			$("#register").focus();
			showErrorAlert("Please register first.");
		}
	});
	$("#disconnect").click(function() {
		if (registered) {
			disconnect();
		}
	});
	$("#register").click(function() {
		if (isUserNameSet() && isSessionIDSet()) {
			registerUserName();
		}
	});
	$("#host").click(function() {
		setHostUrl();
	});
	
	$("#topic").click(function() {
		setTopicName();
	});
	$(".erroralert").hide();
});

function setTopicName(){
	topic =$("#topicName").val();
}

function setHostUrl(){
	host =$("#hosturl").val();
}

function isUserNameSet() {
	userName = $("#name").val();
	sessionId  = $("#sessionid").val();
	console.log(userName);
	console.log(sessionId);
	if (userName == null || userName == '') {
		$("#name").focus();
		showErrorAlert("Please set the username.");
		return false;
	}
	if (sessionId == null || sessionId == '') {
		$("#sessionid").focus();
		showErrorAlert("Please set the session id.");
		return false;
	}
	return true;
}

function isSessionIDSet() {
	sessionId  = $("#sessionid").val();
	console.log(sessionId);
	if (sessionId == null || sessionId == '') {
		$("#sessionid").focus();
		showErrorAlert("Please set the session id.");
		return false;
	}
	headers['x-fnma-sessionid'] = sessionId;
	return true;
}


function showErrorAlert(message){
	$(".erroralert").show();
	$("#erroralertmsg").empty();
	$("#erroralertmsg").append("<strong>Oops! <strong>"+message);
	setTimeout(function(){$(".erroralert").hide()},5000);
}
