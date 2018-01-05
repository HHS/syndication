<?php
namespace Drupal\syndicated_content;

class SyndicatedContentUnitTestBasicTestCase extends DrupalUnitTestCase
{

    public static function getInfo()
    {
      return array(
        'name'        => 'Rendering tests',
        'description' => 'Test escaping during display.',
        'group'       => 'Syndication',
      );
    }

    /// make sure it's safe to throw around syndication primary keys
    function testIdEscaping()
    {
        $id     = 3;
        $result = \Drupal\Component\Serialization\Json::encode(\Drupal\Component\Utility\SafeMarkup::checkPlain($id));
        $expect = '"3"';
        $this->assertEqual($result,$expect,"For inclusion as javascript string within html, integer ids should be wrapped but remain unaltered");

        $id     = '3';
        $result = \Drupal\Component\Serialization\Json::encode(\Drupal\Component\Utility\SafeMarkup::checkPlain($id));
        $expect = '"3"';
        $this->assertEqual($result,$expect,"For inclusion as javascript string within html, numeric string ids should be wrapped but remain unaltered");

        $id     = ');alert("foo");';
        $result = \Drupal\Component\Serialization\Json::encode(\Drupal\Component\Utility\SafeMarkup::checkPlain($id));
        $expect = '");alert(\u0026quot;foo\u0026quot;);"';
        $this->assertEqual($result,$expect,"For inclusion as javascript string within html, XSS ids should be safely escaped");
    }

    /// make sure it's safe to throw around arbirtrary content from syndication
    function testTextEscaping()
    {
        $text   = 3;
        $result = \Drupal\Component\Utility\SafeMarkup::checkPlain($text);
        $expect = '3';
        $this->assertEqual($result,$expect,"For inclusion as text within html, integers remain unaltered");

        $text   = "3";
        $result = \Drupal\Component\Utility\SafeMarkup::checkPlain($text);
        $expect = '3';
        $this->assertEqual($result,$expect,"For inclusion as text within html, numeric string remain unaltered");

        $text   = "<b>Bold</b>";
        $result = \Drupal\Component\Utility\SafeMarkup::checkPlain($text);
        $expect = '&lt;b&gt;Bold&lt;/b&gt;';
        $this->assertEqual($result,$expect,"For inclusion as text within html, tags are utf8 encoded");

        $text   = "<IMG SRC=&#106;&#97;&#118;&#97;&#115;&#99;&#114;&#105;&#112;&#116;&#58;&#97;&#108;&#101;&#114;&#116;&#40;&#39;&#88;&#83;&#83;&#39;&#41;>";
        $result = \Drupal\Component\Utility\SafeMarkup::checkPlain($text);
        $expect = '&lt;IMG SRC=&amp;#106;&amp;#97;&amp;#118;&amp;#97;&amp;#115;&amp;#99;&amp;#114;&amp;#105;&amp;#112;&amp;#116;&amp;#58;&amp;#97;&amp;#108;&amp;#101;&amp;#114;&amp;#116;&amp;#40;&amp;#39;&amp;#88;&amp;#83;&amp;#83;&amp;#39;&amp;#41;&gt;';
        $this->assertEqual($result,$expect,"Basic XSS test");

    }

}
