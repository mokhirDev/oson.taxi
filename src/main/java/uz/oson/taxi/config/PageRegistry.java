package uz.oson.taxi.config;

import org.springframework.stereotype.Service;
import uz.oson.taxi.commands.interfaces.BotPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PageRegistry {
    private final Map<String, BotPage> pages = new HashMap<>();

    public PageRegistry(List<BotPage> pagesList) {
        for (BotPage p : pagesList) {
            pages.put(p.getPageId(), p);
        }
    }

    public BotPage getPage(String id) {
        return pages.get(id);
    }
}
