import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import History from './pages/History';
import Home from './pages/Home';

function App() {
  const baseURL = 'http://localhost:8080';
  const [weatherRecord, setWeatherRecord] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => { 
      try {
        const response = await axios.get(`${baseURL}/api/v1/weather`);
        setWeatherRecord(response.data);
      } catch (error) {
        setError(error);
      }
    };

    fetchData();
  }, []);

  if (error) return `Error: ${error.message}`;
  if (!weatherRecord) return "Loading...";

  return (
    <BrowserRouter>
      <Routes>
        <Route exact path="/" element={<Home weatherRecord={weatherRecord} />} />
        <Route path="history/" element={<History/>} />
        <Route path="*" element={<h1>Not Found</h1>} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
