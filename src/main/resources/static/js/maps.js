/* ============================================
   TourFlow — Google Maps Integration
   ============================================ */

let map;
let markers = [];
let infoWindow;

function initMap() {
    const mapElement = document.getElementById('tourflowMap');
    if (!mapElement) return;

    // Center on Himachal Pradesh
    map = new google.maps.Map(mapElement, {
        center: { lat: 31.8, lng: 77.2 },
        zoom: 8,
        styles: getMapStyles(),
        mapTypeControl: false,
        streetViewControl: false,
        fullscreenControl: true
    });

    infoWindow = new google.maps.InfoWindow();

    // Fetch destinations and add markers
    fetch('/api/destinations/status')
        .then(res => res.json())
        .then(destinations => {
            destinations.forEach(dest => addMarker(dest));
        })
        .catch(err => console.error('Map data error:', err));
}

function addMarker(dest) {
    const color = dest.crowdColor === 'red' ? '#ff5252' :
                  dest.crowdColor === 'yellow' ? '#ffab00' : '#00e676';

    const marker = new google.maps.Marker({
        position: { lat: dest.lat, lng: dest.lng },
        map: map,
        title: dest.name,
        icon: {
            path: google.maps.SymbolPath.CIRCLE,
            scale: 12,
            fillColor: color,
            fillOpacity: 0.8,
            strokeColor: '#ffffff',
            strokeWeight: 2
        }
    });

    marker.addListener('click', () => {
        const content = `
            <div style="font-family: 'Inter', sans-serif; padding: 8px; color: #1a1a2e; max-width: 250px;">
                <h3 style="margin: 0 0 8px; font-size: 1.1rem;">${dest.name}</h3>
                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 6px; font-size: 0.85rem;">
                    <div>👥 Crowd: <strong style="color: ${color}">${dest.crowd}%</strong></div>
                    <div>🚗 Traffic: <strong>${dest.traffic}%</strong></div>
                    <div>🏨 Hotels: <strong>${dest.hotelAvailability}%</strong></div>
                    <div>⚠️ Pressure: <strong>${dest.tourismPressure}/100</strong></div>
                </div>
                ${dest.alertActive ? `<div style="margin-top: 8px; padding: 6px; background: #fff3e0; border-radius: 4px; font-size: 0.8rem;">🚨 ${dest.alertMessage || 'Alert active'}</div>` : ''}
                <a href="/destination/${dest.id}" style="display: block; margin-top: 10px; text-align: center; padding: 6px 12px; background: #0099ff; color: white; border-radius: 6px; text-decoration: none; font-weight: 600;">View Details</a>
            </div>
        `;
        infoWindow.setContent(content);
        infoWindow.open(map, marker);
    });

    markers.push(marker);
}

function getMapStyles() {
    return [
        { elementType: 'geometry', stylers: [{ color: '#1a1a2e' }] },
        { elementType: 'labels.text.stroke', stylers: [{ color: '#1a1a2e' }] },
        { elementType: 'labels.text.fill', stylers: [{ color: '#8892b0' }] },
        { featureType: 'water', elementType: 'geometry', stylers: [{ color: '#0d1b2a' }] },
        { featureType: 'road', elementType: 'geometry', stylers: [{ color: '#2a2a4a' }] },
        { featureType: 'road', elementType: 'geometry.stroke', stylers: [{ color: '#1a1a2e' }] },
        { featureType: 'poi', elementType: 'geometry', stylers: [{ color: '#1a1a35' }] },
        { featureType: 'landscape', elementType: 'geometry', stylers: [{ color: '#16213e' }] },
        { featureType: 'transit', stylers: [{ visibility: 'off' }] }
    ];
}

// Initialize map when Google Maps API loads
window.initMap = initMap;
