<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
  <link rel="import" href="bower_components/vaadin-charts/vaadin-chart.html">
	<script src="../../webcomponentsjs/webcomponents-lite.js"></script>
  <head>
  	
  </head>

 <body>
<dom-module id="chart-series-dom-repeat">
  <template>
    <vaadin-chart id="mychart">
      <template is="dom-repeat" items="{{series}}">
        <vaadin-chart-series values="{{item.data}}"></vaadin-chart-series>
      </template>
    </vaadin-chart>
  </template>

  <script>
    class ChartSeriesDomRepeatElement extends Polymer.Element {
      static get is() {
        return 'chart-series-dom-repeat';
      }

      static get properties() {
        return {
          series: {
            type: Array,
            value: () => {
              return [
                {'data': [10096761, 6990386, 9830199, 10373255, 7903685, 8713277, 10606107, 10227879, 9225719, 11987894]},
                {'data': [9545219, 9425618, 10835399, 8084422, 8541604, 10266319, 7586920, 8778721, 9379301, 10443877, 9771264, 8948669]},
                {'data': [8881128, 9330959, 9882444, 8594214, 9153243, 10109828, 9706285, 9005619, 7717883, 10727544, 9155228, 8765827]}
              ];
            }
          }
        };
      }
    }

    addEventListener('WebComponentsReady', () => {
      customElements.define(ChartSeriesDomRepeatElement.is, ChartSeriesDomRepeatElement);
    });
  </script>
</dom-module>
<chart-series-dom-repeat></chart-series-dom-repeat>
</body>
</html>

