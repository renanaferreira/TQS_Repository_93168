package geocoding;

import com.sun.source.tree.AssertTree;
import connection.ISimpleHttpClient;
import connection.TqsBasicHttpClient;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressResolverTest {

    // Encoded with: http://www.cheminfo.org/Utility/JSoN_text_parse_-_stringify/index.html
    private static final String mockResult = "{\"info\":{\"statuscode\":0,\"copyright\":{\"text\":\"\\u00A9 2021 MapQuest, Inc.\",\"imageUrl\":\"http://api.mqcdn.com/res/mqlogo.gif\",\"imageAltText\":\"\\u00A9 2021 MapQuest, Inc.\"},\"messages\":[]},\"options\":{\"maxResults\":1,\"thumbMaps\":true,\"ignoreLatLngInput\":false},\"results\":[{\"providedLocation\":{\"latLng\":{\"lat\":40.640661,\"lng\":-8.656688}},\"locations\":[{\"street\":\"Cais do Alboi\",\"adminArea6\":\"\",\"adminArea6Type\":\"Neighborhood\",\"adminArea5\":\"Gl\\u00F3ria e Vera Cruz\",\"adminArea5Type\":\"City\",\"adminArea4\":\"\",\"adminArea4Type\":\"County\",\"adminArea3\":\"Centro\",\"adminArea3Type\":\"State\",\"adminArea1\":\"PT\",\"adminArea1Type\":\"Country\",\"postalCode\":\"3800-246\",\"geocodeQualityCode\":\"B1AAA\",\"geocodeQuality\":\"STREET\",\"dragPoint\":false,\"sideOfStreet\":\"N\",\"linkId\":\"0\",\"unknownInput\":\"\",\"type\":\"s\",\"latLng\":{\"lat\":40.640524,\"lng\":-8.656713},\"displayLatLng\":{\"lat\":40.640524,\"lng\":-8.656713},\"mapUrl\":\"http://open.mapquestapi.com/staticmap/v5/map?key=uXSAVwYWbf9tJmsjEGHKKAo0gOjZfBLQ&type=map&size=225,160&locations=40.64052368145179,-8.656712986761146|marker-sm-50318A-1&scalebar=true&zoom=15&rand=-963779157\",\"roadMetadata\":null}]}]}";
    private static final String url = "location=40.640661%2C-8.656688";

    @Mock
    private TqsBasicHttpClient basicHttpClient;

    @InjectMocks
    private AddressResolver addrResolver;

    @Test
    void whenResolveAlboiGps_returnCaisAlboiAddress() throws ParseException, IOException, URISyntaxException {

        //todo


        when(basicHttpClient.doHttpGet(contains(url))).thenReturn(mockResult);

        Optional<Address> result = addrResolver.findAddressForLocation(40.6318, -8.658);
        assertEquals( result, new Address( "Cais do Alboi", "Glória e Vera Cruz", "Centro", "3800-246", null) );

        verify(basicHttpClient.doHttpGet(url));

    }

    @Test
    public void whenBadCoordidates_thenReturnNoValidAddress() throws IOException, URISyntaxException, ParseException {

        //todo
        String badUrl = "location=-300%-810";

        when(basicHttpClient.doHttpGet(contains(badUrl))).thenReturn("");


        Optional<Address> result = addrResolver.findAddressForLocation(-300, -810);
        assertThat(result, isNull());

        verify(basicHttpClient.doHttpGet(badUrl));

    }
}