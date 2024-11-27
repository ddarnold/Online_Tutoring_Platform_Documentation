import React from "react";
import NavBar from "../components/Navbar";

function TutorCentre() {
  return <NavBar isLoggedIn={false} currentPage={document.location.pathname} />;
}

export default TutorCentre;
