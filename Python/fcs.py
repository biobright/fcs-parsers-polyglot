from pkg_resources import parse_version
from kaitaistruct import __version__ as ks_version, KaitaiStruct, KaitaiStream, BytesIO
import bioflow.kaitai.processors


if parse_version(ks_version) < parse_version('0.7'):
    raise Exception("Incompatible Kaitai Struct Python API: 0.7 or later is required, but you have %s" % (ks_version))

class Fcs(KaitaiStruct):
    def __init__(self, _io, _parent=None, _root=None):
        self._io = _io
        self._parent = _parent
        self._root = _root if _root else self
        self._read()

    def _read(self):
        self.header_section = self._root.FcsHeader(self._io, self, self._root)
        self.text_section = self._root.FcsText(self._io, self, self._root)
        self.data_section = self._root.FcsRawData(self._io, self, self._root)
        self.analysis_section = self._root.FcsRawAnalysis(self._io, self, self._root)
        self.crc16 = self._io.read_bytes_full()

    class FcsHeader(KaitaiStruct):
        """
        .. seealso::
           Source - http://isac-net.org/Resources/Standards/FCS3-1.aspx
        """
        def __init__(self, _io, _parent=None, _root=None):
            self._io = _io
            self._parent = _parent
            self._root = _root if _root else self
            self._read()

        def _read(self):
            self._unnamed0 = self._io.ensure_fixed_contents(b"\x46\x43\x53")
            self.fcs_version = (self._io.read_bytes(3)).decode(u"ASCII")
            self._unnamed2 = self._io.ensure_fixed_contents(b"\x20\x20\x20\x20")
            self._raw_text_start = self._io.read_bytes(8)
            _process = bioflow.kaitai.processors.Wrapper(u"builtins", u"int")
            self.text_start = _process.decode(self._raw_text_start)
            self._raw_text_end = self._io.read_bytes(8)
            _process = bioflow.kaitai.processors.Wrapper(u"builtins", u"int")
            self.text_end = _process.decode(self._raw_text_end)
            self._raw_data_start = self._io.read_bytes(8)
            _process = bioflow.kaitai.processors.Wrapper(u"builtins", u"int")
            self.data_start = _process.decode(self._raw_data_start)
            self._raw_data_end = self._io.read_bytes(8)
            _process = bioflow.kaitai.processors.Wrapper(u"builtins", u"int")
            self.data_end = _process.decode(self._raw_data_end)
            self._raw_analysis_start = self._io.read_bytes(8)
            _process = bioflow.kaitai.processors.EmptyStringWrapper(0, u"builtins", u"int")
            self.analysis_start = _process.decode(self._raw_analysis_start)
            self._raw_analysis_end = self._io.read_bytes(8)
            _process = bioflow.kaitai.processors.EmptyStringWrapper(0, u"builtins", u"int")
            self.analysis_end = _process.decode(self._raw_analysis_end)
            self._unnamed9 = self._io.read_bytes((int(self.raw_text_start) - self._io.pos()))

        @property
        def raw_analysis_start(self):
            if hasattr(self, '_m_raw_analysis_start'):
                return self._m_raw_analysis_start if hasattr(self, '_m_raw_analysis_start') else None

            _pos = self._io.pos()
            self._io.seek(42)
            self._m_raw_analysis_start = (self._io.read_bytes(8)).decode(u"ASCII")
            self._io.seek(_pos)
            return self._m_raw_analysis_start if hasattr(self, '_m_raw_analysis_start') else None

        @property
        def raw_text_start(self):
            if hasattr(self, '_m_raw_text_start'):
                return self._m_raw_text_start if hasattr(self, '_m_raw_text_start') else None

            _pos = self._io.pos()
            self._io.seek(10)
            self._m_raw_text_start = (self._io.read_bytes(8)).decode(u"ASCII")
            self._io.seek(_pos)
            return self._m_raw_text_start if hasattr(self, '_m_raw_text_start') else None

        @property
        def raw_data_start(self):
            if hasattr(self, '_m_raw_data_start'):
                return self._m_raw_data_start if hasattr(self, '_m_raw_data_start') else None

            _pos = self._io.pos()
            self._io.seek(26)
            self._m_raw_data_start = (self._io.read_bytes(8)).decode(u"ASCII")
            self._io.seek(_pos)
            return self._m_raw_data_start if hasattr(self, '_m_raw_data_start') else None

        @property
        def raw_analysis_end(self):
            if hasattr(self, '_m_raw_analysis_end'):
                return self._m_raw_analysis_end if hasattr(self, '_m_raw_analysis_end') else None

            _pos = self._io.pos()
            self._io.seek(50)
            self._m_raw_analysis_end = (self._io.read_bytes(8)).decode(u"ASCII")
            self._io.seek(_pos)
            return self._m_raw_analysis_end if hasattr(self, '_m_raw_analysis_end') else None

        @property
        def raw_text_end(self):
            if hasattr(self, '_m_raw_text_end'):
                return self._m_raw_text_end if hasattr(self, '_m_raw_text_end') else None

            _pos = self._io.pos()
            self._io.seek(18)
            self._m_raw_text_end = (self._io.read_bytes(8)).decode(u"ASCII")
            self._io.seek(_pos)
            return self._m_raw_text_end if hasattr(self, '_m_raw_text_end') else None

        @property
        def raw_data_end(self):
            if hasattr(self, '_m_raw_data_end'):
                return self._m_raw_data_end if hasattr(self, '_m_raw_data_end') else None

            _pos = self._io.pos()
            self._io.seek(34)
            self._m_raw_data_end = (self._io.read_bytes(8)).decode(u"ASCII")
            self._io.seek(_pos)
            return self._m_raw_data_end if hasattr(self, '_m_raw_data_end') else None


    class FcsText(KaitaiStruct):
        def __init__(self, _io, _parent=None, _root=None):
            self._io = _io
            self._parent = _parent
            self._root = _root if _root else self
            self._read()

        def _read(self):
            self.delimiter = self._io.read_u1()
            self.keywords = []
            i = 0
            while True:
                _ = self._root.FcsText.FcsKeyword(self._io, self, self._root)
                self.keywords.append(_)
                if self._io.pos() >= (int(self._root.header_section.raw_text_end) - 1):
                    break
                i += 1

        class FcsKeyword(KaitaiStruct):
            """NB, There is a tradeoff here. Str type needs a hardcoded terminator, but omits the terminator. Repeat-until can use a dynamic terminator, but can''t omit it. We choose the latter for consistent behavior. It could maybe be done with str, terminator, parametric type, ''2 phase parsing'' as mentioned in the ''encrypted blob'' github issue Also, process() isn''t allowed. Strip the terminator in client code instead! Docs say, ''True'' array types (Arrays come from using the `repeat` syntax) and ''byte arrays'' share the same literal syntax and lots of method API, but they are actually very different types.
            """
            def __init__(self, _io, _parent=None, _root=None):
                self._io = _io
                self._parent = _parent
                self._root = _root if _root else self
                self._read()

            def _read(self):
                self.key = []
                i = 0
                while True:
                    _ = self._io.read_u1()
                    self.key.append(_)
                    if  ((_ == self._parent.delimiter) or (self._io.pos() >= (int(self._root.header_section.raw_text_end) - 1))) :
                        break
                    i += 1
                self.value = []
                i = 0
                while True:
                    _ = self._io.read_u1()
                    self.value.append(_)
                    if  ((_ == self._parent.delimiter) or (self._io.pos() >= int(self._root.header_section.raw_text_end))) :
                        break
                    i += 1



    class FcsRawData(KaitaiStruct):
        def __init__(self, _io, _parent=None, _root=None):
            self._io = _io
            self._parent = _parent
            self._root = _root if _root else self
            self._read()

        def _read(self):
            self._unnamed0 = self._io.read_bytes((int(self._root.header_section.raw_data_start) - self._io.pos()))
            _on =  (( ((int(self._root.header_section.raw_data_start) == 0) and (int(self._root.header_section.raw_data_end) == 0)) ) or ((int(self._root.header_section.raw_data_end) - int(self._root.header_section.raw_data_start)) < 0)) 
            if _on == False:
                self.data = self._root.FcsRawData.Raw(self._io, self, self._root)
            elif _on == True:
                self.data = self._root.FcsRawData.Empty(self._io, self, self._root)

        class Empty(KaitaiStruct):
            def __init__(self, _io, _parent=None, _root=None):
                self._io = _io
                self._parent = _parent
                self._root = _root if _root else self
                self._read()

            def _read(self):
                self.raw = self._io.read_bytes(0)


        class Raw(KaitaiStruct):
            def __init__(self, _io, _parent=None, _root=None):
                self._io = _io
                self._parent = _parent
                self._root = _root if _root else self
                self._read()

            def _read(self):
                self.raw = self._io.read_bytes(((int(self._root.header_section.raw_data_end) - int(self._root.header_section.raw_data_start)) + 1))



    class FcsRawAnalysis(KaitaiStruct):
        """NB, this fails on analysis section offsets specified only in TEXT section. It also does not allow sections of length 1!."""
        def __init__(self, _io, _parent=None, _root=None):
            self._io = _io
            self._parent = _parent
            self._root = _root if _root else self
            self._read()

        def _read(self):
            _on =  (( ((self._root.header_section.raw_analysis_start == self._root.header_section.raw_analysis_start[::-1]) and (self._root.header_section.raw_analysis_end == self._root.header_section.raw_analysis_end[::-1])) ) or ( ((int(self._root.header_section.raw_analysis_start) == 0) and (int(self._root.header_section.raw_analysis_end) == 0)) )) 
            if _on == False:
                self.analysis = self._root.FcsRawAnalysis.Raw(self._io, self, self._root)
            elif _on == True:
                self.analysis = self._root.FcsRawAnalysis.Empty(self._io, self, self._root)

        class Empty(KaitaiStruct):
            def __init__(self, _io, _parent=None, _root=None):
                self._io = _io
                self._parent = _parent
                self._root = _root if _root else self
                self._read()

            def _read(self):
                self.raw = self._io.read_bytes(0)


        class Raw(KaitaiStruct):
            def __init__(self, _io, _parent=None, _root=None):
                self._io = _io
                self._parent = _parent
                self._root = _root if _root else self
                self._read()

            def _read(self):
                self.raw = self._io.read_bytes(((int(self._root.header_section.raw_analysis_end) - int(self._root.header_section.raw_analysis_start)) + 1))