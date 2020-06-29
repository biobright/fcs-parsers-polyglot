<?php

class Fcs extends \Kaitai\Struct\Struct {
    public function __construct(\Kaitai\Struct\Stream $_io, \Kaitai\Struct\Struct $_parent = null, \Fcs $_root = null) {
        parent::__construct($_io, $_parent, $_root);
        $this->_read();
    }

    private function _read() {
        $this->_m_headerSection = new \Fcs\FcsHeader($this->_io, $this, $this->_root);
        $this->_m_textSection = new \Fcs\FcsText($this->_io, $this, $this->_root);
        $this->_m_dataSection = new \Fcs\FcsRawData($this->_io, $this, $this->_root);
        $this->_m_analysisSection = new \Fcs\FcsRawAnalysis($this->_io, $this, $this->_root);
        $this->_m_crc16 = $this->_io->readBytesFull();
    }
    protected $_m_headerSection;
    protected $_m_textSection;
    protected $_m_dataSection;
    protected $_m_analysisSection;
    protected $_m_crc16;
    public function headerSection() { return $this->_m_headerSection; }
    public function textSection() { return $this->_m_textSection; }
    public function dataSection() { return $this->_m_dataSection; }
    public function analysisSection() { return $this->_m_analysisSection; }

    /**
     * NB, seq omits OTHER sections entirely.
     * NB, CRC16 will contain the OTHER segments if they exist!
     * In practice many formats put OTHER stuff in TEXT keywords...
     * The CRC word is computed for the part of each data set
     * beginning with the first byte of the HEADER segment
     * and ending with the last byte of the final segment of the data set
     * (which could be a TEXT, DATA, ANALYSIS or OTHER segment). 
     * This CRC uses the CCITT polynomial X16 + X12 + X5
     * and requires that each input character be interpreted
     * as its bit-reversed image. These requirements are satisfied
     * by the icrc function (8) if the last two function arguments are 0 and -1, respectively.
     * The CRC value will be placed as ASCII in the 8 bytes immediately after the last segment of the data set.
     * If an implementor chooses not to compute and store a CRC word
     * then the 8 bytes immediately after the last segment of the data set
     * should be filled with ASCII '0' characters.
     * NB, it is not actually validated in this parser.
     * That would use a process() on a raw stream copy of file,
     * probably as a root level instance w/ pos+size.
     */
    public function crc16() { return $this->_m_crc16; }
}

namespace \Fcs;

class FcsHeader extends \Kaitai\Struct\Struct {
    public function __construct(\Kaitai\Struct\Stream $_io, \Fcs $_parent = null, \Fcs $_root = null) {
        parent::__construct($_io, $_parent, $_root);
        $this->_read();
    }

    private function _read() {
        $this->_m__unnamed0 = $this->_io->ensureFixedContents("\x46\x43\x53");
        $this->_m_fcsVersion = \Kaitai\Struct\Stream::bytesToStr($this->_io->readBytes(3), "ASCII");
        $this->_m__unnamed2 = $this->_io->ensureFixedContents("\x20\x20\x20\x20");
        $this->_m__raw_textStart = $this->_io->readBytes(8);
        $_process = new \Bioflow\Kaitai\Processors\Wrapper("builtins", "int");
        $this->_m_textStart = $_process->decode($this->_m__raw_textStart);
        $this->_m__raw_textEnd = $this->_io->readBytes(8);
        $_process = new \Bioflow\Kaitai\Processors\Wrapper("builtins", "int");
        $this->_m_textEnd = $_process->decode($this->_m__raw_textEnd);
        $this->_m__raw_dataStart = $this->_io->readBytes(8);
        $_process = new \Bioflow\Kaitai\Processors\Wrapper("builtins", "int");
        $this->_m_dataStart = $_process->decode($this->_m__raw_dataStart);
        $this->_m__raw_dataEnd = $this->_io->readBytes(8);
        $_process = new \Bioflow\Kaitai\Processors\Wrapper("builtins", "int");
        $this->_m_dataEnd = $_process->decode($this->_m__raw_dataEnd);
        $this->_m__raw_analysisStart = $this->_io->readBytes(8);
        $_process = new \Bioflow\Kaitai\Processors\EmptyStringWrapper(0, "builtins", "int");
        $this->_m_analysisStart = $_process->decode($this->_m__raw_analysisStart);
        $this->_m__raw_analysisEnd = $this->_io->readBytes(8);
        $_process = new \Bioflow\Kaitai\Processors\EmptyStringWrapper(0, "builtins", "int");
        $this->_m_analysisEnd = $_process->decode($this->_m__raw_analysisEnd);
        $this->_m__unnamed9 = $this->_io->readBytes((intval($this->rawTextStart(), 10) - $this->_io()->pos()));
    }
    protected $_m_rawAnalysisStart;
    public function rawAnalysisStart() {
        if ($this->_m_rawAnalysisStart !== null)
            return $this->_m_rawAnalysisStart;
        $_pos = $this->_io->pos();
        $this->_io->seek(42);
        $this->_m_rawAnalysisStart = \Kaitai\Struct\Stream::bytesToStr($this->_io->readBytes(8), "ASCII");
        $this->_io->seek($_pos);
        return $this->_m_rawAnalysisStart;
    }
    protected $_m_rawTextStart;
    public function rawTextStart() {
        if ($this->_m_rawTextStart !== null)
            return $this->_m_rawTextStart;
        $_pos = $this->_io->pos();
        $this->_io->seek(10);
        $this->_m_rawTextStart = \Kaitai\Struct\Stream::bytesToStr($this->_io->readBytes(8), "ASCII");
        $this->_io->seek($_pos);
        return $this->_m_rawTextStart;
    }
    protected $_m_rawDataStart;
    public function rawDataStart() {
        if ($this->_m_rawDataStart !== null)
            return $this->_m_rawDataStart;
        $_pos = $this->_io->pos();
        $this->_io->seek(26);
        $this->_m_rawDataStart = \Kaitai\Struct\Stream::bytesToStr($this->_io->readBytes(8), "ASCII");
        $this->_io->seek($_pos);
        return $this->_m_rawDataStart;
    }
    protected $_m_rawAnalysisEnd;
    public function rawAnalysisEnd() {
        if ($this->_m_rawAnalysisEnd !== null)
            return $this->_m_rawAnalysisEnd;
        $_pos = $this->_io->pos();
        $this->_io->seek(50);
        $this->_m_rawAnalysisEnd = \Kaitai\Struct\Stream::bytesToStr($this->_io->readBytes(8), "ASCII");
        $this->_io->seek($_pos);
        return $this->_m_rawAnalysisEnd;
    }
    protected $_m_rawTextEnd;
    public function rawTextEnd() {
        if ($this->_m_rawTextEnd !== null)
            return $this->_m_rawTextEnd;
        $_pos = $this->_io->pos();
        $this->_io->seek(18);
        $this->_m_rawTextEnd = \Kaitai\Struct\Stream::bytesToStr($this->_io->readBytes(8), "ASCII");
        $this->_io->seek($_pos);
        return $this->_m_rawTextEnd;
    }
    protected $_m_rawDataEnd;
    public function rawDataEnd() {
        if ($this->_m_rawDataEnd !== null)
            return $this->_m_rawDataEnd;
        $_pos = $this->_io->pos();
        $this->_io->seek(34);
        $this->_m_rawDataEnd = \Kaitai\Struct\Stream::bytesToStr($this->_io->readBytes(8), "ASCII");
        $this->_io->seek($_pos);
        return $this->_m_rawDataEnd;
    }
    protected $_m__unnamed0;
    protected $_m_fcsVersion;
    protected $_m__unnamed2;
    protected $_m_textStart;
    protected $_m_textEnd;
    protected $_m_dataStart;
    protected $_m_dataEnd;
    protected $_m_analysisStart;
    protected $_m_analysisEnd;
    protected $_m__unnamed9;
    protected $_m__raw_textStart;
    protected $_m__raw_textEnd;
    protected $_m__raw_dataStart;
    protected $_m__raw_dataEnd;
    protected $_m__raw_analysisStart;
    protected $_m__raw_analysisEnd;

    /**
     * FCS magic number (0 - 2)
     */
    public function _unnamed0() { return $this->_m__unnamed0; }
    public function fcsVersion() { return $this->_m_fcsVersion; }

    /**
     * padding
     */
    public function _unnamed2() { return $this->_m__unnamed2; }

    /**
     * offset to start of TEXT segment, as ASCII(UInt)
     */
    public function textStart() { return $this->_m_textStart; }

    /**
     * offset to end of TEXT segment, as ASCII(UInt)
     */
    public function textEnd() { return $this->_m_textEnd; }

    /**
     * offset to start of DATA segment, as ASCII(UInt)
     */
    public function dataStart() { return $this->_m_dataStart; }

    /**
     * offset to end of DATA segment, as ASCII(UInt)
     */
    public function dataEnd() { return $this->_m_dataEnd; }

    /**
     * offset to start of ANALYSIS segment, as ASCII(UInt). Absence is either right justified 0 or all spaces (0x20)
     */
    public function analysisStart() { return $this->_m_analysisStart; }

    /**
     * offset to end of ANALYSIS segment, as ASCII(UInt). Absence is either right justified 0 or all spaces (0x20)
     */
    public function analysisEnd() { return $this->_m_analysisEnd; }

    /**
     * padding
     */
    public function _unnamed9() { return $this->_m__unnamed9; }
    public function _raw_textStart() { return $this->_m__raw_textStart; }
    public function _raw_textEnd() { return $this->_m__raw_textEnd; }
    public function _raw_dataStart() { return $this->_m__raw_dataStart; }
    public function _raw_dataEnd() { return $this->_m__raw_dataEnd; }
    public function _raw_analysisStart() { return $this->_m__raw_analysisStart; }
    public function _raw_analysisEnd() { return $this->_m__raw_analysisEnd; }
}

namespace \Fcs;

class FcsText extends \Kaitai\Struct\Struct {
    public function __construct(\Kaitai\Struct\Stream $_io, \Fcs $_parent = null, \Fcs $_root = null) {
        parent::__construct($_io, $_parent, $_root);
        $this->_read();
    }

    private function _read() {
        $this->_m_delimiter = $this->_io->readU1();
        $this->_m_keywords = [];
        $i = 0;
        do {
            $_ = new \Fcs\FcsText\FcsKeyword($this->_io, $this, $this->_root);
            $this->_m_keywords[] = $_;
            $i++;
        } while (!($this->_io()->pos() >= (intval($this->_root()->headerSection()->rawTextEnd(), 10) - 1)));
    }
    protected $_m_delimiter;
    protected $_m_keywords;
    public function delimiter() { return $this->_m_delimiter; }
    public function keywords() { return $this->_m_keywords; }
}

/**
 * NB, There is a tradeoff here. Str type needs a hardcoded terminator, but omits the terminator. Repeat-until can use a dynamic terminator, but can''t omit it. We choose the latter for consistent behavior. It could maybe be done with str, terminator, parametric type, ''2 phase parsing'' as mentioned in the ''encrypted blob'' github issue Also, process() isn''t allowed. Strip the terminator in client code instead! Docs say, ''True'' array types (Arrays come from using the `repeat` syntax) and ''byte arrays'' share the same literal syntax and lots of method API, but they are actually very different types.
 */

namespace \Fcs\FcsText;

class FcsKeyword extends \Kaitai\Struct\Struct {
    public function __construct(\Kaitai\Struct\Stream $_io, \Fcs\FcsText $_parent = null, \Fcs $_root = null) {
        parent::__construct($_io, $_parent, $_root);
        $this->_read();
    }

    private function _read() {
        $this->_m_key = [];
        $i = 0;
        do {
            $_ = $this->_io->readU1();
            $this->_m_key[] = $_;
            $i++;
        } while (!( (($_ == $this->_parent()->delimiter()) || ($this->_io()->pos() >= (intval($this->_root()->headerSection()->rawTextEnd(), 10) - 1))) ));
        $this->_m_value = [];
        $i = 0;
        do {
            $_ = $this->_io->readU1();
            $this->_m_value[] = $_;
            $i++;
        } while (!( (($_ == $this->_parent()->delimiter()) || ($this->_io()->pos() >= intval($this->_root()->headerSection()->rawTextEnd(), 10))) ));
    }
    protected $_m_key;
    protected $_m_value;

    /**
     * This is a "dynamic terminator" implementation. The terminator is the last byte, WHICH MUST BE STRIPPED MANUALLY.
     */
    public function key() { return $this->_m_key; }

    /**
     * This is a "dynamic terminator" implementation. The terminator is the last byte, WHICH MUST BE STRIPPED MANUALLY.
     */
    public function value() { return $this->_m_value; }
}

namespace \Fcs;

class FcsRawData extends \Kaitai\Struct\Struct {
    public function __construct(\Kaitai\Struct\Stream $_io, \Fcs $_parent = null, \Fcs $_root = null) {
        parent::__construct($_io, $_parent, $_root);
        $this->_read();
    }

    private function _read() {
        $this->_m__unnamed0 = $this->_io->readBytes((intval($this->_root()->headerSection()->rawDataStart(), 10) - $this->_io()->pos()));
        switch ( (( ((intval($this->_root()->headerSection()->rawDataStart(), 10) == 0) && (intval($this->_root()->headerSection()->rawDataEnd(), 10) == 0)) ) || ((intval($this->_root()->headerSection()->rawDataEnd(), 10) - intval($this->_root()->headerSection()->rawDataStart(), 10)) < 0)) ) {
            case false:
                $this->_m_data = new \Fcs\FcsRawData\Raw($this->_io, $this, $this->_root);
                break;
            case true:
                $this->_m_data = new \Fcs\FcsRawData\Empty($this->_io, $this, $this->_root);
                break;
        }
    }
    protected $_m__unnamed0;
    protected $_m_data;

    /**
     * padding
     */
    public function _unnamed0() { return $this->_m__unnamed0; }

    /**
     * NB, this fails on data section offsets specified only in TEXT section. Also does not support DATA segments of length 1. NB2: We process + patch raw_data_* to values from the text section
     */
    public function data() { return $this->_m_data; }
}

namespace \Fcs\FcsRawData;

class Empty extends \Kaitai\Struct\Struct {
    public function __construct(\Kaitai\Struct\Stream $_io, \Fcs\FcsRawData $_parent = null, \Fcs $_root = null) {
        parent::__construct($_io, $_parent, $_root);
        $this->_read();
    }

    private function _read() {
        $this->_m_raw = $this->_io->readBytes(0);
    }
    protected $_m_raw;
    public function raw() { return $this->_m_raw; }
}

namespace \Fcs\FcsRawData;

class Raw extends \Kaitai\Struct\Struct {
    public function __construct(\Kaitai\Struct\Stream $_io, \Fcs\FcsRawData $_parent = null, \Fcs $_root = null) {
        parent::__construct($_io, $_parent, $_root);
        $this->_read();
    }

    private function _read() {
        $this->_m_raw = $this->_io->readBytes(((intval($this->_root()->headerSection()->rawDataEnd(), 10) - intval($this->_root()->headerSection()->rawDataStart(), 10)) + 1));
    }
    protected $_m_raw;
    public function raw() { return $this->_m_raw; }
}

/**
 * NB, this fails on analysis section offsets specified only in TEXT section. It also does not allow sections of length 1!
 */

namespace \Fcs;

class FcsRawAnalysis extends \Kaitai\Struct\Struct {
    public function __construct(\Kaitai\Struct\Stream $_io, \Fcs $_parent = null, \Fcs $_root = null) {
        parent::__construct($_io, $_parent, $_root);
        $this->_read();
    }

    private function _read() {
        switch ( (( (($this->_root()->headerSection()->rawAnalysisStart() == strrev($this->_root()->headerSection()->rawAnalysisStart())) && ($this->_root()->headerSection()->rawAnalysisEnd() == strrev($this->_root()->headerSection()->rawAnalysisEnd()))) ) || ( ((intval($this->_root()->headerSection()->rawAnalysisStart(), 10) == 0) && (intval($this->_root()->headerSection()->rawAnalysisEnd(), 10) == 0)) )) ) {
            case false:
                $this->_m_analysis = new \Fcs\FcsRawAnalysis\Raw($this->_io, $this, $this->_root);
                break;
            case true:
                $this->_m_analysis = new \Fcs\FcsRawAnalysis\Empty($this->_io, $this, $this->_root);
                break;
        }
    }
    protected $_m_analysis;
    public function analysis() { return $this->_m_analysis; }
}

namespace \Fcs\FcsRawAnalysis;

class Empty extends \Kaitai\Struct\Struct {
    public function __construct(\Kaitai\Struct\Stream $_io, \Fcs\FcsRawAnalysis $_parent = null, \Fcs $_root = null) {
        parent::__construct($_io, $_parent, $_root);
        $this->_read();
    }

    private function _read() {
        $this->_m_raw = $this->_io->readBytes(0);
    }
    protected $_m_raw;
    public function raw() { return $this->_m_raw; }
}

namespace \Fcs\FcsRawAnalysis;

class Raw extends \Kaitai\Struct\Struct {
    public function __construct(\Kaitai\Struct\Stream $_io, \Fcs\FcsRawAnalysis $_parent = null, \Fcs $_root = null) {
        parent::__construct($_io, $_parent, $_root);
        $this->_read();
    }

    private function _read() {
        $this->_m_raw = $this->_io->readBytes(((intval($this->_root()->headerSection()->rawAnalysisEnd(), 10) - intval($this->_root()->headerSection()->rawAnalysisStart(), 10)) + 1));
    }
    protected $_m_raw;

    /**
     * NB, this does not allow sections of length 1!
     */
    public function raw() { return $this->_m_raw; }
}
