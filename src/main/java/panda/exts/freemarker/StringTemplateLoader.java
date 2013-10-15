package panda.exts.freemarker;

import java.io.Reader;
import java.io.StringReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import freemarker.cache.TemplateLoader;

/**
 * string template loader
 */
public class StringTemplateLoader implements TemplateLoader {
    protected final Map<String, StringTemplateSource> templates = new ConcurrentHashMap<String, StringTemplateSource>();
    
    /**
     * Puts a template into the loader. A call to this method is identical to 
     * the call to the three-arg {@link #putTemplate(String, String, long)} 
     * passing <tt>System.currentTimeMillis()</tt> as the third argument.
     * @param name the name of the template.
     * @param templateSource the source code of the template.
     */
    public void putTemplate(String name, String templateSource) {
        putTemplate(name, templateSource, System.currentTimeMillis());
    }
    
    /**
     * Puts a template into the loader. The name can contain slashes to denote
     * logical directory structure, but must not start with a slash. If the 
     * method is called multiple times for the same name and with different
     * last modified time, the configuration's template cache will reload the 
     * template according to its own refresh settings (note that if the refresh 
     * is disabled in the template cache, the template will not be reloaded).
     * Also, since the cache uses lastModified to trigger reloads, calling the
     * method with different source and identical timestamp won't trigger
     * reloading.
     * @param name the name of the template.
     * @param templateSource the source code of the template.
     * @param lastModified the time of last modification of the template in 
     * terms of <tt>System.currentTimeMillis()</tt>
     */
    public void putTemplate(String name, String templateSource, long lastModified) {
    	if (templateSource == null) {
    		templates.remove(name);
    	}
    	else {
    		templates.put(name, new StringTemplateSource(name, templateSource, lastModified));
    	}
    }
    
    /**
     * @see freemarker.cache.TemplateLoader#closeTemplateSource(java.lang.Object)
     */
    public void closeTemplateSource(Object templateSource) {
    }
    
    /**
     * @see freemarker.cache.TemplateLoader#findTemplateSource(java.lang.String)
     */
    public Object findTemplateSource(String name) {
        return templates.get(name);
    }
    
    /**
     * @see freemarker.cache.TemplateLoader#getLastModified(java.lang.Object)
     */
    public long getLastModified(Object templateSource) {
        return ((StringTemplateSource)templateSource).lastModified;
    }
    
    /**
     * @see freemarker.cache.TemplateLoader#getReader(java.lang.Object, java.lang.String)
     */
    public Reader getReader(Object templateSource, String encoding) {
        return new StringReader(((StringTemplateSource)templateSource).source);
    }
    
    private static class StringTemplateSource {
        private final String name;
        private final String source;
        private final long lastModified;
        
        StringTemplateSource(String name, String source, long lastModified) {
            if(name == null) {
                throw new IllegalArgumentException("name == null");
            }
            if(source == null) {
                throw new IllegalArgumentException("source == null");
            }
            if(lastModified < -1L) {
                throw new IllegalArgumentException("lastModified < -1L");
            }
            this.name = name;
            this.source = source;
            this.lastModified = lastModified;
        }
        
        public boolean equals(Object obj) {
            if(obj instanceof StringTemplateSource) {
                return name.equals(((StringTemplateSource)obj).name);
            }
            return false;
        }
        
        public int hashCode() {
            return name.hashCode();
        }
    }
}
