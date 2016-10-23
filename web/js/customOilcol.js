

	// LM Mapa Pozo
function initMap() {
	// Cordenadas de mi casa 4.604534, -74.069729
	if(document.getElementById('mapaPozo')!=null)
	{
	var uluru = {lat: 4.604534, lng: -74.069729};
	
	var map = new google.maps.Map(document.getElementById('mapaPozo'), {
	zoom: 10,
	center: uluru
	});
	
	// else if(document.getElementById('mapaCampo')!=null)
	//    {
	//    var map = new google.maps.Map(document.getElementById('mapaCampo'), {
	//      zoom: 10,
	//      center: uluru
	//    });
	// }
	var marker = new google.maps.Marker({
	position: uluru,
	map: map
	});
	}
	else if(document.getElementById('mapaCampo')!=null)
    {
	var locations = [
      ['Pozo1', 4.604534, -74.069729, 4],
      ['Pozo2', 4.414534, -74.169729, 5],
      ['Pozo3', 4.454534, -74.269729, 3],
      ['Pozo4', 4.594534, -73.969729, 2],
      ['Pozo5', 4.784534, -73.869729, 1]
    ];
    
    var map = new google.maps.Map(document.getElementById('mapaCampo'), {
      zoom: 10,
      center: new google.maps.LatLng(4.604534, -74.069729)

      // ,mapTypeId: google.maps.MapTypeId.ROADMAP
    });

    var infowindow = new google.maps.InfoWindow();

    var marker, i;

    for (i = 0; i < locations.length; i++) {  
      marker = new google.maps.Marker({
        position: new google.maps.LatLng(locations[i][1], locations[i][2]),
        map: map
      });

      google.maps.event.addListener(marker, 'click', (function(marker, i) {
        return function() {
          infowindow.setContent(locations[i][0]);
          infowindow.open(map, marker);
        }
      })(marker, i));
    }
}
}					        


