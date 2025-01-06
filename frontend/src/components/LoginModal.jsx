import React from "react";

function LoginModal({ open, onClose, children }) {
  return (
    <div
      onClick={onClose}
      className={`font-merriweather_sans fixed inset-0 flex justify-center items-center transition-colors 
            ${open ? "visible bg-black/20" : "invisible"}`}
    >
      <div
        onClick={(e) => e.stopPropagation()}
        className={`
        bg-white flex flex-col rounded-md items-center justify-center shadow p-10 transition-all
        ${open ? "scale-100 opacity-100" : "scale-125 opacity-0"}
      `}
      >
        {children}
      </div>
    </div>
  );
}

export default LoginModal;
