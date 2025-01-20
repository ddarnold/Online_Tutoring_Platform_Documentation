import { useSocket } from "../services/SocketContext";

const CallNotification = () => {
  const { incomingCall, incomingUserName, acceptCall, rejectCall } =
    useSocket();

  if (!incomingCall) return null;

  return (
    <div className="fixed bottom-5 right-5 bg-gray-900 text-white p-6 rounded-3xl shadow-lg font-merriweather_sans">
      <div className="flex space-x-2">
        <span className="text-xl text-white material-symbols-rounded">
          call
        </span>
        <p className="text-lg text-center w-full">
          Incoming Call from{" "}
          <i>
            <strong>{incomingUserName}</strong>
          </i>
        </p>
      </div>

      <div className="flex space-x-4 mt-4">
        <button
          onClick={acceptCall}
          className="bg-green-500 px-4 py-2 rounded-xl hover:bg-green-600 w-full"
        >
          Accept
        </button>
        <button
          onClick={rejectCall}
          className="bg-red-500 px-4 py-2 rounded-xl hover:bg-red-600 w-full"
        >
          Reject
        </button>
      </div>
    </div>
  );
};

export default CallNotification;
