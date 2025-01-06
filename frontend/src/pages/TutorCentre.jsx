import React from "react";
import NavBar from "../components/Navbar";

function TutorCentre() {
  return <NavBar currentPage={document.location.pathname} />;
}

export default TutorCentre;
