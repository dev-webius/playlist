package net.webius.playlist.util;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
public class HostConnectionUtil {
    public Connection.Response get(String url) throws HostConnectionException {
        try {
            return Jsoup.connect(url)
                    .method(Connection.Method.GET)
                    .ignoreContentType(true)
                    .execute();
        } catch (IOException e) {
            throw new HostConnectionException("Host GET (No Params, No Headers) Error");
        }
    }

    public Connection.Response get(String url, HashMap<String, String> params) throws HostConnectionException {
        Set<Map.Entry<String, String>> entryParams = params.entrySet();
        try {
            Connection connection = Jsoup.connect(url)
                    .method(Connection.Method.GET)
                    .ignoreContentType(true);

            for (Map.Entry<String, String> entry : entryParams) {
                connection = connection.data(entry.getKey(), entry.getValue());
            }

            return connection.execute();
        } catch (IOException e) {
            throw new HostConnectionException("Host GET (Params, No Headers) Error");
        }
    }

    public Connection.Response get(String url, HashMap<String, String> params, HashMap<String, String> headers) throws HostConnectionException {
        Set<Map.Entry<String, String>> entryParams = params.entrySet();
        Set<Map.Entry<String, String>> entryHeaders = headers.entrySet();
        try {
            Connection connection = Jsoup.connect(url)
                    .method(Connection.Method.GET)
                    .ignoreContentType(true);

            for (Map.Entry<String, String> entry : entryParams) {
                connection = connection.data(entry.getKey(), entry.getValue());
            }

            for (Map.Entry<String, String> entry : entryHeaders) {
                connection = connection.header(entry.getKey(), entry.getValue());
            }

            return connection.execute();
        } catch (IOException e) {
            throw new HostConnectionException("Host GET (Params, Headers) Error");
        }
    }

    public Connection.Response post(String url) throws HostConnectionException {
        try {
            return Jsoup.connect(url)
                    .method(Connection.Method.POST)
                    .ignoreContentType(true)
                    .execute();
        } catch (IOException e) {
            throw new HostConnectionException("Host POST (No Params, No Headers) Error");
        }
    }

    public Connection.Response post(String url, HashMap<String, String> params) throws HostConnectionException {
        Set<Map.Entry<String, String>> entryParams = params.entrySet();
        try {
            Connection connection = Jsoup.connect(url)
                    .method(Connection.Method.POST)
                    .ignoreContentType(true);

            for (Map.Entry<String, String> entry : entryParams) {
                connection = connection.data(entry.getKey(), entry.getValue());
            }

            return connection.execute();
        } catch (IOException e) {
            throw new HostConnectionException("Host POST (Params, No Headers) Error");
        }
    }

    public Connection.Response post(String url, HashMap<String, String> params, HashMap<String, String> headers) throws HostConnectionException {
        Set<Map.Entry<String, String>> entryParams = params.entrySet();
        Set<Map.Entry<String, String>> entryHeaders = headers.entrySet();
        try {
            Connection connection = Jsoup.connect(url)
                    .method(Connection.Method.POST)
                    .ignoreContentType(true);

            for (Map.Entry<String, String> entry : entryParams) {
                connection = connection.data(entry.getKey(), entry.getValue());
            }

            for (Map.Entry<String, String> entry : entryHeaders) {
                connection = connection.header(entry.getKey(), entry.getValue());
            }

            return connection.execute();
        } catch (IOException e) {
            throw new HostConnectionException("Host POST (Params, Headers) Error");
        }
    }

    public String getJsValue(Document document, String varName) {
        Elements script = find(document, "script");
        for (Element element : script) {
            if (element.data().contains(varName)) {
                Pattern pattern = Pattern.compile(".*" + varName + " = ([^;]*);");
                Matcher matcher = pattern.matcher(element.data());

                if (matcher.find())
                    return matcher.group(1);

                break;
            }
        }
        return null;
    }

    public String getMetaValue(Document document, String varName) {
        Elements meta = find(document, "meta");
        for (Element element : meta) {
            if (element.attr("property").equals((varName))) {
                return element.attr("content");
            }
        }
        return null;
    }

    public JsonNode getNode(String content) {
        try {
            return new ObjectMapper().readTree(content);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Elements find(Document document, String selector) {
        return document.select(selector);
    }
}
