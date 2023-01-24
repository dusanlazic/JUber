import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { ReportResponse } from 'src/models/reports';
import { ReportService } from 'src/services/reports/report.service';
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);

@Component({
  selector: 'app-reports',
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.sass']
})
export class ReportsComponent implements OnInit {

  data: ReportResponse;
  chart: any;

  constructor(
    private reportsService: ReportService,
  ) {
    this.data = {
      days: [],
      sums: {
        rides: 0,
        distance: 0,
        fare: 0
      },
      averages: {
        rides: 0,
        distance: 0,
        fare: 0
      },
    }
  }

  ngOnInit() {
    this.reportsService.getReport("2023-06-01", "2023-06-30").subscribe({
      next: (res: ReportResponse) => {
        this.data = res;
        this.createChart();
      }
    })
  }

  createChart() : void {
    this.chart = new Chart("ReportChart", {
      type: 'bar',
      data: {
        labels: this.data.days.map(d => d.date),
        datasets: [{
          label: 'Rides',
          data: this.data.days.map(d => d.rides),
          borderWidth: 0,
          backgroundColor: "#48c78e",
          yAxisID: 'yRides',
        },
        {
          label: 'Spendings (RSD)',
          data: this.data.days.map(d => d.fare),
          borderWidth: 0,
          backgroundColor: "#c74848",
          yAxisID: 'ySpendingDistance',
          stack: 'Stack 0'
        },
        {
          label: 'Distance (km)',
          data: this.data.days.map(d => d.distance),
          borderWidth: 0,
          backgroundColor: "#4848c7",
          yAxisID: 'ySpendingDistance',
          stack: 'Stack 0'
        },]
      },
      options: {
        maintainAspectRatio: false,
        interaction: {
          mode: 'index',
        },
        scales: {
          yRides: {
            display: true,
            position: 'left',
            beginAtZero: true,
            ticks: {
              stepSize: 1
            },
            grid: {
              display: false
            }
          },
          ySpendingDistance: {
            display: true,
            position: 'right',
            stacked: true,
          },
          x: {
            ticks: {
              autoSkip: true,
              maxTicksLimit: 15
            },
            grid: {
              display: false
            }
          }
        },
      }
    });
  }

  round(num: number) : string {
    return parseFloat(num.toString(10)).toFixed(2);
  }
}
