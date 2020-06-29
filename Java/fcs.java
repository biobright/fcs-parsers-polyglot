import io.kaitai.struct.ByteBufferKaitaiStream;
import io.kaitai.struct.KaitaiStruct;
import io.kaitai.struct.KaitaiStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Fcs extends KaitaiStruct {
    public static Fcs fromFile(String fileName) throws IOException {
        return new Fcs(new ByteBufferKaitaiStream(fileName));
    }

    public Fcs(KaitaiStream _io) {
        this(_io, null, null);
    }

    public Fcs(KaitaiStream _io, KaitaiStruct _parent) {
        this(_io, _parent, null);
    }

    public Fcs(KaitaiStream _io, KaitaiStruct _parent, Fcs _root) {
        super(_io);
        this._parent = _parent;
        this._root = _root == null ? this : _root;
        _read();
    }
    private void _read() {
        this.headerSection = new FcsHeader(this._io, this, _root);
        this.textSection = new FcsText(this._io, this, _root);
        this.dataSection = new FcsRawData(this._io, this, _root);
        this.analysisSection = new FcsRawAnalysis(this._io, this, _root);
        this.crc16 = this._io.readBytesFull();
    }

    /**
     * @see <a href="http://isac-net.org/Resources/Standards/FCS3-1.aspx">Source</a>
     */
    public static class FcsHeader extends KaitaiStruct {
        public static FcsHeader fromFile(String fileName) throws IOException {
            return new FcsHeader(new ByteBufferKaitaiStream(fileName));
        }

        public FcsHeader(KaitaiStream _io) {
            this(_io, null, null);
        }

        public FcsHeader(KaitaiStream _io, Fcs _parent) {
            this(_io, _parent, null);
        }

        public FcsHeader(KaitaiStream _io, Fcs _parent, Fcs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this._unnamed0 = this._io.ensureFixedContents(new byte[] { 70, 67, 83 });
            this.fcsVersion = new String(this._io.readBytes(3), Charset.forName("ASCII"));
            this._unnamed2 = this._io.ensureFixedContents(new byte[] { 32, 32, 32, 32 });
            this._raw_textStart = this._io.readBytes(8);
            bioflow.kaitai.processors.Wrapper _process__raw_textStart = new bioflow.kaitai.processors.Wrapper("builtins", "int");
            this.textStart = _process__raw_textStart.decode(this._raw_textStart);
            this._raw_textEnd = this._io.readBytes(8);
            bioflow.kaitai.processors.Wrapper _process__raw_textEnd = new bioflow.kaitai.processors.Wrapper("builtins", "int");
            this.textEnd = _process__raw_textEnd.decode(this._raw_textEnd);
            this._raw_dataStart = this._io.readBytes(8);
            bioflow.kaitai.processors.Wrapper _process__raw_dataStart = new bioflow.kaitai.processors.Wrapper("builtins", "int");
            this.dataStart = _process__raw_dataStart.decode(this._raw_dataStart);
            this._raw_dataEnd = this._io.readBytes(8);
            bioflow.kaitai.processors.Wrapper _process__raw_dataEnd = new bioflow.kaitai.processors.Wrapper("builtins", "int");
            this.dataEnd = _process__raw_dataEnd.decode(this._raw_dataEnd);
            this._raw_analysisStart = this._io.readBytes(8);
            bioflow.kaitai.processors.EmptyStringWrapper _process__raw_analysisStart = new bioflow.kaitai.processors.EmptyStringWrapper(0, "builtins", "int");
            this.analysisStart = _process__raw_analysisStart.decode(this._raw_analysisStart);
            this._raw_analysisEnd = this._io.readBytes(8);
            bioflow.kaitai.processors.EmptyStringWrapper _process__raw_analysisEnd = new bioflow.kaitai.processors.EmptyStringWrapper(0, "builtins", "int");
            this.analysisEnd = _process__raw_analysisEnd.decode(this._raw_analysisEnd);
            this._unnamed9 = this._io.readBytes((Long.parseLong(rawTextStart(), 10) - _io().pos()));
        }
        private String rawAnalysisStart;
        public String rawAnalysisStart() {
            if (this.rawAnalysisStart != null)
                return this.rawAnalysisStart;
            long _pos = this._io.pos();
            this._io.seek(42);
            this.rawAnalysisStart = new String(this._io.readBytes(8), Charset.forName("ASCII"));
            this._io.seek(_pos);
            return this.rawAnalysisStart;
        }
        private String rawTextStart;
        public String rawTextStart() {
            if (this.rawTextStart != null)
                return this.rawTextStart;
            long _pos = this._io.pos();
            this._io.seek(10);
            this.rawTextStart = new String(this._io.readBytes(8), Charset.forName("ASCII"));
            this._io.seek(_pos);
            return this.rawTextStart;
        }
        private String rawDataStart;
        public String rawDataStart() {
            if (this.rawDataStart != null)
                return this.rawDataStart;
            long _pos = this._io.pos();
            this._io.seek(26);
            this.rawDataStart = new String(this._io.readBytes(8), Charset.forName("ASCII"));
            this._io.seek(_pos);
            return this.rawDataStart;
        }
        private String rawAnalysisEnd;
        public String rawAnalysisEnd() {
            if (this.rawAnalysisEnd != null)
                return this.rawAnalysisEnd;
            long _pos = this._io.pos();
            this._io.seek(50);
            this.rawAnalysisEnd = new String(this._io.readBytes(8), Charset.forName("ASCII"));
            this._io.seek(_pos);
            return this.rawAnalysisEnd;
        }
        private String rawTextEnd;
        public String rawTextEnd() {
            if (this.rawTextEnd != null)
                return this.rawTextEnd;
            long _pos = this._io.pos();
            this._io.seek(18);
            this.rawTextEnd = new String(this._io.readBytes(8), Charset.forName("ASCII"));
            this._io.seek(_pos);
            return this.rawTextEnd;
        }
        private String rawDataEnd;
        public String rawDataEnd() {
            if (this.rawDataEnd != null)
                return this.rawDataEnd;
            long _pos = this._io.pos();
            this._io.seek(34);
            this.rawDataEnd = new String(this._io.readBytes(8), Charset.forName("ASCII"));
            this._io.seek(_pos);
            return this.rawDataEnd;
        }
        private byte[] _unnamed0;
        private String fcsVersion;
        private byte[] _unnamed2;
        private byte[] textStart;
        private byte[] textEnd;
        private byte[] dataStart;
        private byte[] dataEnd;
        private byte[] analysisStart;
        private byte[] analysisEnd;
        private byte[] _unnamed9;
        private Fcs _root;
        private Fcs _parent;
        private byte[] _raw_textStart;
        private byte[] _raw_textEnd;
        private byte[] _raw_dataStart;
        private byte[] _raw_dataEnd;
        private byte[] _raw_analysisStart;
        private byte[] _raw_analysisEnd;

        /**
         * FCS magic number (0 - 2)
         */
        public byte[] _unnamed0() { return _unnamed0; }
        public String fcsVersion() { return fcsVersion; }

        /**
         * padding
         */
        public byte[] _unnamed2() { return _unnamed2; }

        /**
         * offset to start of TEXT segment, as ASCII(UInt)
         */
        public byte[] textStart() { return textStart; }

        /**
         * offset to end of TEXT segment, as ASCII(UInt)
         */
        public byte[] textEnd() { return textEnd; }

        /**
         * offset to start of DATA segment, as ASCII(UInt)
         */
        public byte[] dataStart() { return dataStart; }

        /**
         * offset to end of DATA segment, as ASCII(UInt)
         */
        public byte[] dataEnd() { return dataEnd; }

        /**
         * offset to start of ANALYSIS segment, as ASCII(UInt). Absence is either right justified 0 or all spaces (0x20)
         */
        public byte[] analysisStart() { return analysisStart; }

        /**
         * offset to end of ANALYSIS segment, as ASCII(UInt). Absence is either right justified 0 or all spaces (0x20)
         */
        public byte[] analysisEnd() { return analysisEnd; }

        /**
         * padding
         */
        public byte[] _unnamed9() { return _unnamed9; }
        public Fcs _root() { return _root; }
        public Fcs _parent() { return _parent; }
        public byte[] _raw_textStart() { return _raw_textStart; }
        public byte[] _raw_textEnd() { return _raw_textEnd; }
        public byte[] _raw_dataStart() { return _raw_dataStart; }
        public byte[] _raw_dataEnd() { return _raw_dataEnd; }
        public byte[] _raw_analysisStart() { return _raw_analysisStart; }
        public byte[] _raw_analysisEnd() { return _raw_analysisEnd; }
    }
    public static class FcsText extends KaitaiStruct {
        public static FcsText fromFile(String fileName) throws IOException {
            return new FcsText(new ByteBufferKaitaiStream(fileName));
        }

        public FcsText(KaitaiStream _io) {
            this(_io, null, null);
        }

        public FcsText(KaitaiStream _io, Fcs _parent) {
            this(_io, _parent, null);
        }

        public FcsText(KaitaiStream _io, Fcs _parent, Fcs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.delimiter = this._io.readU1();
            this.keywords = new ArrayList<FcsKeyword>();
            {
                FcsKeyword _it;
                int i = 0;
                do {
                    _it = new FcsKeyword(this._io, this, _root);
                    this.keywords.add(_it);
                    i++;
                } while (!(_io().pos() >= (Long.parseLong(_root.headerSection().rawTextEnd(), 10) - 1)));
            }
        }

        /**
         * NB, There is a tradeoff here. Str type needs a hardcoded terminator, but omits the terminator. Repeat-until can use a dynamic terminator, but can''t omit it. We choose the latter for consistent behavior. It could maybe be done with str, terminator, parametric type, ''2 phase parsing'' as mentioned in the ''encrypted blob'' github issue Also, process() isn''t allowed. Strip the terminator in client code instead! Docs say, ''True'' array types (Arrays come from using the `repeat` syntax) and ''byte arrays'' share the same literal syntax and lots of method API, but they are actually very different types.
         */
        public static class FcsKeyword extends KaitaiStruct {
            public static FcsKeyword fromFile(String fileName) throws IOException {
                return new FcsKeyword(new ByteBufferKaitaiStream(fileName));
            }

            public FcsKeyword(KaitaiStream _io) {
                this(_io, null, null);
            }

            public FcsKeyword(KaitaiStream _io, Fcs.FcsText _parent) {
                this(_io, _parent, null);
            }

            public FcsKeyword(KaitaiStream _io, Fcs.FcsText _parent, Fcs _root) {
                super(_io);
                this._parent = _parent;
                this._root = _root;
                _read();
            }
            private void _read() {
                this.key = new ArrayList<Integer>();
                {
                    int _it;
                    int i = 0;
                    do {
                        _it = this._io.readU1();
                        this.key.add(_it);
                        i++;
                    } while (!( ((_it == _parent().delimiter()) || (_io().pos() >= (Long.parseLong(_root.headerSection().rawTextEnd(), 10) - 1))) ));
                }
                this.value = new ArrayList<Integer>();
                {
                    int _it;
                    int i = 0;
                    do {
                        _it = this._io.readU1();
                        this.value.add(_it);
                        i++;
                    } while (!( ((_it == _parent().delimiter()) || (_io().pos() >= Long.parseLong(_root.headerSection().rawTextEnd(), 10))) ));
                }
            }
            private ArrayList<Integer> key;
            private ArrayList<Integer> value;
            private Fcs _root;
            private Fcs.FcsText _parent;

            /**
             * This is a "dynamic terminator" implementation. The terminator is the last byte, WHICH MUST BE STRIPPED MANUALLY.
             */
            public ArrayList<Integer> key() { return key; }

            /**
             * This is a "dynamic terminator" implementation. The terminator is the last byte, WHICH MUST BE STRIPPED MANUALLY.
             */
            public ArrayList<Integer> value() { return value; }
            public Fcs _root() { return _root; }
            public Fcs.FcsText _parent() { return _parent; }
        }
        private int delimiter;
        private ArrayList<FcsKeyword> keywords;
        private Fcs _root;
        private Fcs _parent;
        public int delimiter() { return delimiter; }
        public ArrayList<FcsKeyword> keywords() { return keywords; }
        public Fcs _root() { return _root; }
        public Fcs _parent() { return _parent; }
    }
    public static class FcsRawData extends KaitaiStruct {
        public static FcsRawData fromFile(String fileName) throws IOException {
            return new FcsRawData(new ByteBufferKaitaiStream(fileName));
        }

        public FcsRawData(KaitaiStream _io) {
            this(_io, null, null);
        }

        public FcsRawData(KaitaiStream _io, Fcs _parent) {
            this(_io, _parent, null);
        }

        public FcsRawData(KaitaiStream _io, Fcs _parent, Fcs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this._unnamed0 = this._io.readBytes((Long.parseLong(_root.headerSection().rawDataStart(), 10) - _io().pos()));
            {
                boolean on =  (( ((Long.parseLong(_root.headerSection().rawDataStart(), 10) == 0) && (Long.parseLong(_root.headerSection().rawDataEnd(), 10) == 0)) ) || ((Long.parseLong(_root.headerSection().rawDataEnd(), 10) - Long.parseLong(_root.headerSection().rawDataStart(), 10)) < 0)) ;
                if (on == false) {
                    this.data = new Raw(this._io, this, _root);
                }
                else if (on == true) {
                    this.data = new Empty(this._io, this, _root);
                }
            }
        }
        public static class Empty extends KaitaiStruct {
            public static Empty fromFile(String fileName) throws IOException {
                return new Empty(new ByteBufferKaitaiStream(fileName));
            }

            public Empty(KaitaiStream _io) {
                this(_io, null, null);
            }

            public Empty(KaitaiStream _io, Fcs.FcsRawData _parent) {
                this(_io, _parent, null);
            }

            public Empty(KaitaiStream _io, Fcs.FcsRawData _parent, Fcs _root) {
                super(_io);
                this._parent = _parent;
                this._root = _root;
                _read();
            }
            private void _read() {
                this.raw = this._io.readBytes(0);
            }
            private byte[] raw;
            private Fcs _root;
            private Fcs.FcsRawData _parent;
            public byte[] raw() { return raw; }
            public Fcs _root() { return _root; }
            public Fcs.FcsRawData _parent() { return _parent; }
        }
        public static class Raw extends KaitaiStruct {
            public static Raw fromFile(String fileName) throws IOException {
                return new Raw(new ByteBufferKaitaiStream(fileName));
            }

            public Raw(KaitaiStream _io) {
                this(_io, null, null);
            }

            public Raw(KaitaiStream _io, Fcs.FcsRawData _parent) {
                this(_io, _parent, null);
            }

            public Raw(KaitaiStream _io, Fcs.FcsRawData _parent, Fcs _root) {
                super(_io);
                this._parent = _parent;
                this._root = _root;
                _read();
            }
            private void _read() {
                this.raw = this._io.readBytes(((Long.parseLong(_root.headerSection().rawDataEnd(), 10) - Long.parseLong(_root.headerSection().rawDataStart(), 10)) + 1));
            }
            private byte[] raw;
            private Fcs _root;
            private Fcs.FcsRawData _parent;
            public byte[] raw() { return raw; }
            public Fcs _root() { return _root; }
            public Fcs.FcsRawData _parent() { return _parent; }
        }
        private byte[] _unnamed0;
        private KaitaiStruct data;
        private Fcs _root;
        private Fcs _parent;

        /**
         * padding
         */
        public byte[] _unnamed0() { return _unnamed0; }

        /**
         * NB, this fails on data section offsets specified only in TEXT section. Also does not support DATA segments of length 1. NB2: We process + patch raw_data_* to values from the text section
         */
        public KaitaiStruct data() { return data; }
        public Fcs _root() { return _root; }
        public Fcs _parent() { return _parent; }
    }

    /**
     * NB, this fails on analysis section offsets specified only in TEXT section. It also does not allow sections of length 1!
     */
    public static class FcsRawAnalysis extends KaitaiStruct {
        public static FcsRawAnalysis fromFile(String fileName) throws IOException {
            return new FcsRawAnalysis(new ByteBufferKaitaiStream(fileName));
        }

        public FcsRawAnalysis(KaitaiStream _io) {
            this(_io, null, null);
        }

        public FcsRawAnalysis(KaitaiStream _io, Fcs _parent) {
            this(_io, _parent, null);
        }

        public FcsRawAnalysis(KaitaiStream _io, Fcs _parent, Fcs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            {
                boolean on =  (( ((_root.headerSection().rawAnalysisStart().equals(new StringBuilder(_root.headerSection().rawAnalysisStart()).reverse().toString())) && (_root.headerSection().rawAnalysisEnd().equals(new StringBuilder(_root.headerSection().rawAnalysisEnd()).reverse().toString()))) ) || ( ((Long.parseLong(_root.headerSection().rawAnalysisStart(), 10) == 0) && (Long.parseLong(_root.headerSection().rawAnalysisEnd(), 10) == 0)) )) ;
                if (on == false) {
                    this.analysis = new Raw(this._io, this, _root);
                }
                else if (on == true) {
                    this.analysis = new Empty(this._io, this, _root);
                }
            }
        }
        public static class Empty extends KaitaiStruct {
            public static Empty fromFile(String fileName) throws IOException {
                return new Empty(new ByteBufferKaitaiStream(fileName));
            }

            public Empty(KaitaiStream _io) {
                this(_io, null, null);
            }

            public Empty(KaitaiStream _io, Fcs.FcsRawAnalysis _parent) {
                this(_io, _parent, null);
            }

            public Empty(KaitaiStream _io, Fcs.FcsRawAnalysis _parent, Fcs _root) {
                super(_io);
                this._parent = _parent;
                this._root = _root;
                _read();
            }
            private void _read() {
                this.raw = this._io.readBytes(0);
            }
            private byte[] raw;
            private Fcs _root;
            private Fcs.FcsRawAnalysis _parent;
            public byte[] raw() { return raw; }
            public Fcs _root() { return _root; }
            public Fcs.FcsRawAnalysis _parent() { return _parent; }
        }
        public static class Raw extends KaitaiStruct {
            public static Raw fromFile(String fileName) throws IOException {
                return new Raw(new ByteBufferKaitaiStream(fileName));
            }

            public Raw(KaitaiStream _io) {
                this(_io, null, null);
            }

            public Raw(KaitaiStream _io, Fcs.FcsRawAnalysis _parent) {
                this(_io, _parent, null);
            }

            public Raw(KaitaiStream _io, Fcs.FcsRawAnalysis _parent, Fcs _root) {
                super(_io);
                this._parent = _parent;
                this._root = _root;
                _read();
            }
            private void _read() {
                this.raw = this._io.readBytes(((Long.parseLong(_root.headerSection().rawAnalysisEnd(), 10) - Long.parseLong(_root.headerSection().rawAnalysisStart(), 10)) + 1));
            }
            private byte[] raw;
            private Fcs _root;
            private Fcs.FcsRawAnalysis _parent;

            /**
             * NB, this does not allow sections of length 1!
             */
            public byte[] raw() { return raw; }
            public Fcs _root() { return _root; }
            public Fcs.FcsRawAnalysis _parent() { return _parent; }
        }
        private KaitaiStruct analysis;
        private Fcs _root;
        private Fcs _parent;
        public KaitaiStruct analysis() { return analysis; }
        public Fcs _root() { return _root; }
        public Fcs _parent() { return _parent; }
    }
    private FcsHeader headerSection;
    private FcsText textSection;
    private FcsRawData dataSection;
    private FcsRawAnalysis analysisSection;
    private byte[] crc16;
    private Fcs _root;
    private KaitaiStruct _parent;
    public FcsHeader headerSection() { return headerSection; }
    public FcsText textSection() { return textSection; }
    public FcsRawData dataSection() { return dataSection; }
    public FcsRawAnalysis analysisSection() { return analysisSection; }

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
    public byte[] crc16() { return crc16; }
    public Fcs _root() { return _root; }
    public KaitaiStruct _parent() { return _parent; }
}