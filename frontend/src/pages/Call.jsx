import React, { useEffect, useRef, useState } from "react";
import Peer from "simple-peer";
import io from "socket.io-client";

const socket = io.connect("http://localhost:5000");

function App() {
  const [me, setMe] = useState("");
  const [stream, setStream] = useState();
  const [receivingCall, setReceivingCall] = useState(false);
  const [caller, setCaller] = useState("");
  const [callerSignal, setCallerSignal] = useState();
  const [callAccepted, setCallAccepted] = useState(false);
  const [idToCall, setIdToCall] = useState("");
  const [callEnded, setCallEnded] = useState(false);
  const [name, setName] = useState("");
  const myVideo = useRef();
  const userVideo = useRef();
  const connectionRef = useRef();
  const [videoEnabled, setVideoEnabled] = useState(true);
  const [audioEnabled, setAudioEnabled] = useState(true);

  const toggleVideo = () => {
    if (stream) {
      const videoTrack = stream.getVideoTracks()[0];
      videoTrack.enabled = !videoTrack.enabled;
      setVideoEnabled(videoTrack.enabled);
    }
  };

  const toggleAudio = () => {
    if (stream) {
      const audioTrack = stream.getAudioTracks()[0];
      audioTrack.enabled = !audioTrack.enabled;
      setAudioEnabled(audioTrack.enabled);
    }
  };

  useEffect(() => {
    // Set higher-quality constraints for media devices
    const constraints = {
      video: {
        width: { ideal: 1280 },
        height: { ideal: 720 },
        frameRate: { ideal: 30 },
      },
      audio: true,
    };

    navigator.mediaDevices
      .getUserMedia(constraints)
      .then((stream) => {
        setStream(stream);
        if (myVideo.current) {
          myVideo.current.srcObject = stream;
        }
      })
      .catch((error) => {
        console.error("Error accessing media devices:", error);
      });

    socket.on("me", (id) => {
      setMe(id);
    });

    socket.on("callUser", (data) => {
      setReceivingCall(true);
      setCaller(data.from);
      setName(data.name);
      setCallerSignal(data.signal);
    });

    return () => {
      // Cleanup socket listeners on unmount
      socket.off("me");
      socket.off("callUser");
    };
  }, []);

  const configurePeer = (peer) => {
    peer.on("stream", (remoteStream) => {
      if (userVideo.current) {
        userVideo.current.srcObject = remoteStream;
      }
    });

    peer.on("close", () => {
      console.log("Peer connection closed");
    });

    peer.on("error", (error) => {
      console.error("Peer connection error:", error);
    });
  };

  const callUser = (id) => {
    const peer = new Peer({
      initiator: true,
      trickle: false,
      stream: stream,
    });

    configurePeer(peer);

    peer.on("signal", (data) => {
      socket.emit("callUser", {
        userToCall: id,
        signalData: data,
        from: me,
        name: name,
      });
    });

    socket.on("callAccepted", (signal) => {
      setCallAccepted(true);
      peer.signal(signal);
    });

    connectionRef.current = peer;
  };

  const answerCall = () => {
    setCallAccepted(true);

    const peer = new Peer({
      initiator: false,
      trickle: false,
      stream: stream,
    });

    configurePeer(peer);

    peer.on("signal", (data) => {
      socket.emit("answerCall", { signal: data, to: caller });
    });

    peer.signal(callerSignal);

    connectionRef.current = peer;
  };

  const leaveCall = () => {
    setCallEnded(true);

    // Safely close the peer connection
    if (connectionRef.current) {
      connectionRef.current.destroy();
    }

    // Notify the other user that the call has ended
    socket.emit("callEnded", { to: caller || idToCall });
  };

  return (
    <div className="p-8 bg-gray-100 min-h-screen font-merriweather_sans">
      <h1 className="text-3xl font-bold text-gray-800 mb-6 text-center">
        WebRTC
      </h1>
      <div className="grid md:grid-cols-2 gap-8">
        {/* Video Section */}
        <div className="flex flex-col items-center space-y-6">
          <div className="rounded-lg overflow-hidden shadow-lg bg-black w-full max-w-[500px] aspect-video">
            {stream && (
              <video
                playsInline
                muted
                ref={myVideo}
                autoPlay
                className="w-full h-full object-cover transform scale-x-[-1]"
              />
            )}
          </div>
          <div className="rounded-lg overflow-hidden shadow-lg bg-gray-800 w-full max-w-[500px] aspect-video">
            {callAccepted && !callEnded ? (
              <video
                playsInline
                ref={userVideo}
                autoPlay
                className="w-full h-full object-cover transform scale-x-[-1]"
              />
            ) : (
              <p className="text-gray-400 text-center p-4">No active video</p>
            )}
          </div>
        </div>

        {/* Controls Section */}
        <div className="bg-white p-6 rounded-lg shadow-lg space-y-6">
          <div className="space-y-4">
            <input
              placeholder="Enter your name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <button
              onClick={() => navigator.clipboard.writeText(me)}
              className="text-blue-600 hover:text-blue-800"
            >
              Copy ID
            </button>
          </div>

          <div className="space-y-4">
            <input
              placeholder="Enter ID to call"
              value={idToCall}
              onChange={(e) => setIdToCall(e.target.value)}
              className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <div className="flex justify-between items-center">
              {callAccepted && !callEnded ? (
                <button
                  onClick={leaveCall}
                  className="px-6 py-2 bg-red-500 text-white font-bold rounded-lg hover:bg-red-600"
                >
                  End Call
                </button>
              ) : (
                <button
                  onClick={() => callUser(idToCall)}
                  className="px-6 py-2 bg-green-500 text-white font-bold rounded-lg hover:bg-green-600"
                >
                  Call
                </button>
              )}
            </div>
          </div>

          {receivingCall && !callAccepted && (
            <div className="p-4 bg-blue-50 rounded-lg">
              <h1>{name} is calling...</h1>
              <button
                onClick={answerCall}
                className="mt-4 px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600"
              >
                Answer
              </button>
            </div>
          )}
        </div>
        <div className="flex justify-center space-x-4">
          <button
            onClick={toggleVideo}
            className={`px-4 py-2 rounded-lg font-bold material-symbols-rounded cursor-pointer ${
              videoEnabled
                ? "bg-green-500 text-white"
                : "bg-gray-400 text-black"
            }`}
          >
            {videoEnabled ? "video_camera_front" : "video_camera_front_off"}
          </button>
          <span
            onClick={toggleAudio}
            className={`px-4 py-2 rounded-lg font-bold material-symbols-rounded cursor-pointer ${
              audioEnabled
                ? "bg-green-500 text-white"
                : "bg-gray-400 text-black"
            }`}
          >
            {audioEnabled ? "mic" : "mic_off"}
          </span>
        </div>
      </div>
    </div>
  );
}

export default App;
