import React from "react";
import NavBar from "../components/Navbar";

function Messages() {
  return <NavBar isLoggedIn={false} currentPage={document.location.pathname} />;
}

export default Messages;
