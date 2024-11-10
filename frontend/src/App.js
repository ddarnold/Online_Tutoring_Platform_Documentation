import logo from "./logo.svg";
import "./App.css";
import React, { useEffect } from "react";

function App() {
  const fetchData = async () => {
    const res = await fetch("http://localhost:8080/sample");
    console.log(res);
  };

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <div className="app">
      <header className="app-header">
        <img src={logo} className="app-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="app-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
  );
}

export default App;
