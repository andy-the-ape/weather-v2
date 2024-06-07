import React from 'react';
import './TopButtons.css';
import { Link } from 'react-router-dom';
import { UilTemperatureThreeQuarter, UilChartLine } from '@iconscout/react-unicons';

function TopButtons() {

    const pages = [
        {id: 1, name: 'I dag', url: '/', icon: <UilTemperatureThreeQuarter />},
        {id: 2, name: 'Historik', url: '/history/', icon: <UilChartLine />}
    ]

  return (
    <div className='topbuttons'>{pages.map((page) => (
        <Link key={page.id} className='link' to={page.url}>
            <button>
              <p className='buttonIcon'>{page.icon}</p>{page.name}
            </button>
        </Link>
    ))}</div>
  )
}

export default TopButtons;