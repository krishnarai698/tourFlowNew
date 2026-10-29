

function initDashboardCharts() {
    initPressureChart();
    initDistributionChart();
    initTrendChart();
}

function initPressureChart() {
    const canvas = document.getElementById('pressureChart');
    if (!canvas) return;

    const labels = canvas.getAttribute('data-labels')?.split(',') || [];
    const values = canvas.getAttribute('data-values')?.split(',').map(Number) || [];

    const colors = values.map(v => {
        if (v >= 75) return 'rgba(255, 82, 82, 0.8)';
        if (v >= 50) return 'rgba(255, 171, 0, 0.8)';
        return 'rgba(0, 230, 118, 0.8)';
    });

    const borderColors = values.map(v => {
        if (v >= 75) return '#ff5252';
        if (v >= 50) return '#ffab00';
        return '#00e676';
    });

    new Chart(canvas, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Tourism Pressure',
                data: values,
                backgroundColor: colors,
                borderColor: borderColors,
                borderWidth: 1,
                borderRadius: 6
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false },
                tooltip: {
                    backgroundColor: 'rgba(17, 24, 39, 0.95)',
                    titleColor: '#f1f5f9',
                    bodyColor: '#94a3b8',
                    borderColor: 'rgba(255, 255, 255, 0.1)',
                    borderWidth: 1,
                    cornerRadius: 8
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    max: 100,
                    grid: { color: 'rgba(255, 255, 255, 0.05)' },
                    ticks: { color: '#64748b' }
                },
                x: {
                    grid: { display: false },
                    ticks: { color: '#64748b', maxRotation: 45 }
                }
            }
        }
    });
}

function initDistributionChart() {
    const canvas = document.getElementById('distributionChart');
    if (!canvas) return;

    const labels = canvas.getAttribute('data-labels')?.split(',') || [];
    const values = canvas.getAttribute('data-values')?.split(',').map(Number) || [];

    new Chart(canvas, {
        type: 'doughnut',
        data: {
            labels: labels,
            datasets: [{
                data: values,
                backgroundColor: [
                    'rgba(255, 82, 82, 0.8)',
                    'rgba(255, 171, 0, 0.8)',
                    'rgba(0, 230, 118, 0.8)',
                    'rgba(0, 212, 255, 0.8)'
                ],
                borderColor: 'rgba(10, 14, 26, 1)',
                borderWidth: 3
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            cutout: '65%',
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: { color: '#94a3b8', padding: 16, font: { size: 12 } }
                }
            }
        }
    });
}

function initTrendChart() {
    const canvas = document.getElementById('trendChart');
    if (!canvas) return;

    const labels = canvas.getAttribute('data-labels')?.split(',') || [];
    const values = canvas.getAttribute('data-values')?.split(',').map(Number) || [];

    new Chart(canvas, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Predicted Crowd',
                data: values,
                borderColor: '#00d4ff',
                backgroundColor: 'rgba(0, 212, 255, 0.1)',
                fill: true,
                tension: 0.4,
                pointBackgroundColor: '#00d4ff',
                pointBorderColor: '#0a0e1a',
                pointBorderWidth: 2,
                pointRadius: 5
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false },
                tooltip: {
                    backgroundColor: 'rgba(17, 24, 39, 0.95)',
                    titleColor: '#f1f5f9',
                    bodyColor: '#94a3b8',
                    cornerRadius: 8
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    max: 100,
                    grid: { color: 'rgba(255, 255, 255, 0.05)' },
                    ticks: { color: '#64748b', callback: v => v + '%' }
                },
                x: {
                    grid: { display: false },
                    ticks: { color: '#64748b' }
                }
            }
        }
    });
}

// Initialize when DOM ready
document.addEventListener('DOMContentLoaded', initDashboardCharts);
