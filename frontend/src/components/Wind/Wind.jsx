import React from 'react';

function Wind({ degrees }) {

    const compassSector = [
        "Nord", 
        "Nord-Nordøst", 
        "Nordøst", 
        "Øst-Nordøst", 
        "Øst", 
        "Øst-Sydøst", 
        "Sydøst", 
        "Syd-Sydøst", 
        "Syd", 
        "Syd-Sydvest", 
        "Sydvest", 
        "Vest-Sydvest", 
        "Vest", 
        "Vest-Nordvest", 
        "Nordvest", 
        "Nord-Nordvest", 
        "Nord"
    ];

    const windDirection = compassSector[Math.round(degrees / 22.5)];

  return (
    <div className="wind">
      {windDirection && (
        <>
          <p className='bold'>{windDirection}</p>
          <p>Vindretning</p>
        </>
      )}
    </div>
  );
}

export default Wind;