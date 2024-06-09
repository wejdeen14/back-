import React from 'react';
import Header from './Header';
import TabMag from './DashMag/TabMag';
import ConsulterFacture from './ConsulterFacture';
function MagConsulterFacture() {
  return (
        <div className='flex flex-row bg-neutral-100 h-screen w-screen overflow-hidden'>
        <TabMag/>
          <div className='flex-1 flex flex-col overflow-hidden'>
       <Header/>
       <div className='flex-1 grap-12 w-full mt-2 overflow-auto'> 
           <ConsulterFacture/>
        </div>
        </div>
        </div>
      );
    }

export default MagConsulterFacture;