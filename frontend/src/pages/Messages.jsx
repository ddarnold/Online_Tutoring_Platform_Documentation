import React, { useEffect, useRef, useState } from "react";
import NavBar from "../components/Navbar";
import ChatHistoryItem from "../components/ChatHistoryItem";
import io from "socket.io-client";
import { getUserFromToken } from "../services/AuthService";
import MessageItem from "../components/MessageItem";
import apiClient from "../services/AxiosConfig";
import { Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import FormatDate from "../helpers/FormatDate";

//const socket = io("http://localhost:5000");

function Messages() {
  const [chats, setChats] = useState();
  const [selectedChatId, setSelectedChatId] = useState("");
  const [selectedChatObject, setSelectedChatObject] = useState(null);
  const [ongoingCall, setOngoingCall] = useState(null); // Holds call data (type, user)
  const [userStatus, setUserStatus] = useState({}); // Tracks online status of users
  const [searchQuery, setSearchQuery] = useState("");
  const [messages, setMessages] = useState({});
  const [typedMessage, setTypedMessage] = useState("");
  const [currentUserId, setCurrentUserId] = useState();
  const [rows, setRows] = useState(1);
  const [filteredChats, setFilteredChats] = useState();
  const [stompClient, setStompClient] = useState(null);
  const maxRows = 5;
  const messagesEndRef = useRef(null);

  const scrollToBottom = () => {
    if (messagesEndRef.current) {
      const container = messagesEndRef.current.parentElement;
      if (container.scrollHeight > container.clientHeight) {
        messagesEndRef.current.scrollIntoView({ behavior: "smooth" });
      }
    }
  };

  // Call scrollToBottom whenever messages[selectedChatId] changes
  useEffect(() => {
    if (selectedChatId && messages[selectedChatId]) {
      scrollToBottom();
    }
  }, [messages[selectedChatId]]);

  const readChat = async (chatId) => {
    try {
      const { data } = await apiClient.put("/message/" + chatId + "/read");
      console.log("After Read: \n" + data);
      setChats((prevChats) =>
        prevChats.map((chat) =>
          chat.chatId === chatId
            ? { ...chat, unreadMessages: 0, read: true }
            : chat,
        ),
      );
    } catch (error) {
      console.error("Error reading messages:", error);
    }
  };

  useEffect(() => {
    const socket = new SockJS("http://localhost:8080/chat");
    const stompClient = Stomp.over(socket);
    setStompClient(stompClient);
  }, []);

  useEffect(() => {
    if (stompClient) {
      stompClient.connect({}, onConnected, onError);
      stompClient.onStompError = (frame) => {
        console.error("Stomp Error:", frame.headers["message"]);
        console.error("Additional details:", frame.body);
      };
    }
  }, [stompClient]);

  function onConnected() {
    console.log("Connected to WebSocket");

    // Subscribe to message topic
    stompClient.subscribe("/topic/messages", onMessageReceived);
  }

  function onMessageReceived(payload) {
    // console.log(payload);
    // const message = JSON.parse(payload._body);
    // console.log(message);
    const binaryBody = payload._binaryBody; // The Uint8Array payload
    const jsonString = new TextDecoder("utf-8").decode(binaryBody);
    const message = JSON.parse(jsonString);

    if (message.receiverId === currentUserId) {
      setMessages((prevMessages) => ({
        ...prevMessages,
        [message.chatId]: [...(prevMessages[message.chatId] || []), message],
      }));
    }
  }

  function onError(error) {
    console.log("Web Socket Error: " + error);
  }

  useEffect(() => {
    // Load messages initially
    const user = getUserFromToken();
    if (user) {
      setCurrentUserId(user.id);
    }

    // // Listen for user status updates
    // socket.on("userStatusUpdate", ({ userId, isOnline }) => {
    //   setUserStatus((prev) => ({
    //     ...prev,
    //     [userId]: isOnline,
    //   }));
    // });
    //
    // // Listen for call events (e.g., call ended by the other user)
    // socket.on("callEnded", () => {
    //   setOngoingCall(null);
    // });
    //
    // return () => {
    //   socket.disconnect();
    // };
  }, []);

  useEffect(() => {
    if (currentUserId) {
      loadChats();
    }
  }, [currentUserId]);

  useEffect(() => {
    if (chats) {
      // Function to handle filtering
      loadMessages();
      setFilteredChats(
        chats.filter((chat) => {
          const { firstName, lastName } = chat.receiver;
          const fullName = `${firstName} ${lastName}`.toLowerCase();
          return fullName.includes(searchQuery.toLowerCase());
        }),
      );
    }
  }, [chats]);

  useEffect(() => {
    if (chats) {
      setFilteredChats(
        chats.filter((chat) => {
          const { firstName, lastName } = chat.receiver;
          const fullName = `${firstName} ${lastName}`.toLowerCase();
          return fullName.includes(searchQuery.toLowerCase());
        }),
      );
    }
  }, [searchQuery]);

  useEffect(() => {
    if (messages) {
      console.log(messages);
    }
  }, [messages]);

  const loadMessages = async () => {
    for (const chat of chats) {
      const chatId = chat.chatId;
      try {
        const { data } = await apiClient.get(
          "/user/get-messages-chat?chatId=" + chatId,
        );
        console.log(data);
        setMessages((prevMessages) => ({ ...prevMessages, [chatId]: data }));
      } catch (error) {
        console.error("Error loading messages:", error);
      }
    }
  };

  const loadChats = async () => {
    try {
      const { data } = await apiClient.get(
        "/user/get-chat-summaries?userId=" + currentUserId,
      );
      setChats(data);
    } catch (error) {
      console.error("Error loading chats:", error);
    }
  };

  const startCall = (type, userId) => {
    // Check if the user is online
    // if (userStatus[userId]) {
    //   setOngoingCall({ type, userId });
    //   // Emit call initiation to the server
    //   socket.emit("callUser", {
    //     userToCall: userId,
    //     from: socket.id, // Your socket ID
    //     type,
    //   });
    // } else {
    //   alert("User is offline. You cannot call them.");
    // }
  };

  const endCall = () => {
    // setOngoingCall(null);
    // socket.emit("endCall", { to: ongoingCall.userId });
  };

  const handleSendMessage = (e) => {
    const messageContent = typedMessage.trim();
    if (messageContent && stompClient) {
      const currentDate = new Date();

      // Get the local date and time
      const formattedDate = currentDate
        .toISOString()
        .replace("Z", "")
        .slice(0, -4);
      const message = {
        senderId: currentUserId,
        receiverId: selectedChatObject.receiver.id,
        chatId: selectedChatId,
        messageContent: messageContent,
        sendAt: formattedDate,
        isRead: false,
        readAt: null,
      };

      setMessages((prevMessages) => ({
        ...prevMessages,
        [selectedChatId]: [...(prevMessages[selectedChatId] || []), message],
      }));

      try {
        stompClient.send("/app/sendMessage", {}, JSON.stringify(message));
        console.log("Message sent successfully.");
      } catch (error) {
        console.error("Error sending message:", error);
      }

      setTypedMessage(""); // Clear the input field after sending
      setRows(1);
    }
    e.preventDefault();
  };

  const handleKeyPress = (e) => {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault(); // Prevent the default behavior (creating a new line)
      handleSendMessage(e); // Send the message when Enter is pressed
    }
  };

  const handleInputChange = (e) => {
    setTypedMessage(e.target.value);

    // Calculate the new number of rows based on the content
    const lineCount = e.target.value.split("\n").length;
    setRows(Math.min(lineCount, maxRows)); // Limit the rows to maxRows
  };
  return (
    <div className="flex flex-col w-full h-screen overflow-hidden font-merriweather_sans">
      <NavBar currentPage={document.location.pathname} />

      <div className="flex flex-1 bg-gray-50" style={{ paddingTop: "70px" }}>
        {/* Sidebar */}
        <div className="w-2/5 bg-white border-r border-gray-200">
          <div className="p-4">
            <div className="relative">
              <span className="absolute left-3 top-3.5 text-gray-500">
                <i className="material-symbols-rounded text-md">search</i>
              </span>
              <input
                type="text"
                placeholder="Search"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="w-full px-10 py-4 rounded-2xl bg-gray-100 text-sm text-gray-600 outline-none"
              />
            </div>
          </div>
          <div className="overflow-y-auto">
            {filteredChats &&
              chats &&
              messages &&
              filteredChats.map((chat, index) => (
                <ChatHistoryItem
                  key={index}
                  chat={chat}
                  currentUserId={currentUserId}
                  messages={messages[chat.chatId]}
                  selectedChatId={selectedChatId}
                  setSelectedChatId={setSelectedChatId}
                  setSelectedChatObject={setSelectedChatObject}
                  readChat={readChat}
                />
              ))}
          </div>
        </div>

        {/* Chat Section */}
        {selectedChatId !== "" ? (
          <div className="w-3/5 flex flex-col h-screen relative">
            {/* Chat content */}
            <header
              style={{ left: "40%" }}
              className="px-4 py-2 fixed top-0 right-0 z-10 space-x-10 bg-blue-900 text-white shadow-sm flex items-center justify-between rounded-2xl mx-4 mt-[80px]"
            >
              <h2 className="text-lg">
                {selectedChatObject.receiver.firstName}{" "}
                {selectedChatObject.receiver.lastName}
              </h2>
              <div className="flex items-center space-x-3 mt-1.5">
                <button
                  className="text-white hover:text-gray-300"
                  onClick={() =>
                    startCall(
                      "audio",
                      chats.find((c) => c.chatId === selectedChatId)?.senderId,
                    )
                  }
                >
                  <span className="material-symbols-rounded">call</span>
                </button>
                <button
                  className="text-white hover:text-gray-300"
                  onClick={() =>
                    startCall(
                      "video",
                      chats.find((c) => c.chatId === selectedChatId)?.senderId,
                    )
                  }
                >
                  <span className="material-symbols-rounded">videocam</span>
                </button>
              </div>
            </header>

            <div className="px-4 bg-gray-50 overflow-y-auto flex-grow flex-col space-y-2 pb-20 pt-36">
              {messages[selectedChatId]?.length > 0 ? (
                messages[selectedChatId].map((message, index) => {
                  const currentDate = FormatDate(
                    new Date(message.sendAt).toDateString(),
                  );
                  const prevDate =
                    index > 0
                      ? FormatDate(
                          new Date(
                            messages[selectedChatId][index - 1].sendAt,
                          ).toDateString(),
                        )
                      : null;

                  return (
                    <React.Fragment key={index}>
                      {/* Add a date separator if this is the first message of a new day */}
                      {currentDate !== prevDate && (
                        <div className="text-center text-gray-500 text-xs my-2">
                          {currentDate}
                        </div>
                      )}
                      <MessageItem
                        message={message}
                        currentUserId={currentUserId}
                      />
                    </React.Fragment>
                  );
                })
              ) : (
                <p className="text-gray-500 text-center">No messages yet</p>
              )}
              <div ref={messagesEndRef}></div>
            </div>

            {/* Fixed Footer */}
            <footer
              style={{ left: "40%" }}
              className="flex fixed bottom-0 right-0 p-4 bg-white items-center space-x-3"
            >
              <textarea
                rows={rows}
                placeholder="Your message"
                value={typedMessage}
                onKeyDown={handleKeyPress}
                onChange={handleInputChange}
                className="flex-1 px-4 py-2 w-auto bg-gray-200 rounded-xl text-sm text-gray-800 outline-none resize-none"
              />
              <button
                className="bg-blue-900 text-white px-4 py-2 rounded-full"
                onClick={handleSendMessage}
              >
                Send
              </button>
            </footer>
          </div>
        ) : (
          <div className="flex-1 flex flex-col bg-white border-l border-gray-200 p-4 justify-center items-center">
            <h3 className="text-gray-800 font-medium">Select a Chat</h3>
          </div>
        )}
      </div>

      {/* Call Interface */}
      {ongoingCall && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
          <div className="bg-white rounded-lg p-6 shadow-lg w-1/3">
            <h2 className="text-gray-800 font-medium">
              {ongoingCall.type === "audio" ? "Audio Call" : "Video Call"} with{" "}
              {ongoingCall.userId}
            </h2>
            <button
              className="bg-red-500 text-white px-4 py-2 rounded-full mt-4"
              onClick={endCall}
            >
              End Call
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

export default Messages;
