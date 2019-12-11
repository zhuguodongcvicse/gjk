;(function (factory, window, document, undefined) {
  if (typeof define === "function" && (define.amd || define.cmd)) {
    define(function () {
      return factory(window, document, undefined);
    });
  } else {
    window.Q = factory(window, document, undefined);
  }
})(function (t, i, n) {
  "use strict";

  function e(t, i, n) {
    if (t[eh]()) {
      var s = t._fi || t[sh]();
      if (s) {
        s = s._je || s;
        for (var r = 0, h = s[rh]; h > r; r++) if (i.call(n, s[r]) === !1 || e(s[r], i, n) === !1) return !1;
        return !0
      }
    }
  }

  function s(t) {
    if (!t.hasChildren()) return t instanceof Tq ? t : null;
    for (var i, n = t._fi._je, e = n[rh] - 1; e >= 0;) {
      if (i = n[e], i = s(i)) return i;
      e--
    }
    return null
  }

  function r(t, i, n, e) {
    return e ? h(t, i, n) : a(t, i, n)
  }

  function h(t, i, n) {
    t = t._je || t;
    for (var e, s = 0, r = t.length; r > s; s++) if (e = t[s], e.hasChildren() && !h(e[hh], i, n) || i[ah](n, e) === !1) return !1;
    return !0
  }

  function a(t, i, n) {
    t = t._je || t;
    for (var e, s = 0, r = t[rh]; r > s; s++) if (e = t[s], i[ah](n, e) === !1 || e[eh]() && !a(e[hh], i, n)) return !1;
    return !0
  }

  function o(t, i, n, e) {
    return e ? f(t, i, n) : c(t, i, n)
  }

  function f(t, i, n) {
    t = t._je || t;
    for (var e, s = t.length, r = s - 1; r >= 0; r--) if (e = t[r], e[eh]() && !f(e[hh], i, n) || i[ah](n, e) === !1) return !1;
    return !0
  }

  function c(t, i, n) {
    t = t._je || t;
    for (var e, s = t[rh], r = s - 1; r >= 0; r--) if (e = t[r], i.call(n, e) === !1 || e[eh]() && !c(e[hh], i, n)) return !1;
    return !0
  }

  function u(t, i, n) {
    for (var e, s = (t._je || t).slice(0); s[rh];) {
      e = s[0], s = s[oh](1);
      var r = i[ah](n, e);
      if (r === !1) return !1;
      if (e.hasChildren()) {
        var h = e.children;
        h = h._je || h, s = s[fh](h)
      }
    }
    return !0
  }

  function _(t, i, n) {
    for (var e, s = (t._je || t)[ch](0); s[rh];) {
      e = s[s.length - 1], s = s[oh](0, s[rh] - 1);
      var r = i[ah](n, e);
      if (r === !1) return !1;
      if (e[eh]()) {
        var h = e.children;
        h = h._je || h, s = s[fh](h)
      }
    }
    return !0
  }

  function d(t, i) {
    function n(t, n) {
      for (var e = t[rh], s = n[rh], r = e + s, h = new Array(r), a = 0, o = 0, f = 0; r > f;) h[f++] = a === e ? n[o++] : o === s || i(t[a], n[o]) <= 0 ? t[a++] : n[o++];
      return h
    }

    function e(t) {
      var i = t[rh], s = Math[uh](i / 2);
      return 1 >= i ? t : n(e(t[ch](0, s)), e(t[ch](s)))
    }

    return e(t)
  }

  function l(t, i, n, e) {
    t instanceof sY && (t = t._je);
    for (var s = 0, r = (t._je || t).length; r > s; s++) {
      var h = i[ah](n, t[s], s, e);
      if (h === !1) return !1
    }
    return !0
  }

  function v(t, i, n) {
    for (var e = t instanceof sY, s = t._je || t, r = 0, h = s[rh]; h > r; r++) {
      var a = s[r];
      i[ah](n, a) && (e ? t[_h](a) : t[oh](r, 1), r--, h--)
    }
  }

  function b(t, i, n, e) {
    t instanceof sY && (t = t._je);
    for (var s = (t._je || t)[rh] - 1; s >= 0; s--) {
      var r = i.call(n, t[s], s, e);
      if (r === !1) return !1
    }
    return !0
  }

  function y(t) {
    if (t[dh] instanceof Function) return t[dh](!0);
    var i, n = [];
    return l(t, function (t) {
      i = t && t.clone instanceof Function ? t[dh]() : t, n.push(i)
    }, this), n
  }

  function g(t, i, e) {
    e === n || 0 > e ? t.push(i) : t[oh](e, 0, i)
  }

  function x(t, i) {
    var n = t[lh](i);
    return 0 > n || n >= t[rh] ? !1 : t.splice(n, 1)
  }

  function p(t, i) {
    var n = !1;
    return l(t, function (t) {
      return i == t ? (n = !0, !1) : void 0
    }), n
  }

  function E(t, i) {
    var n = t;
    for (var e in i) if (i.__lookupGetter__) {
      var s = i.__lookupGetter__(e), r = i.__lookupSetter__(e);
      s || r ? (s && n.__defineGetter__(e, s), r && n.__defineSetter__(e, r)) : n[e] = i[e]
    } else n[e] = i[e];
    return n
  }

  function m(t, i, n) {
    if (!(t instanceof Function)) throw new Error("subclass must be type of Function");
    var e = null;
    vh == typeof i && (e = i, i = t, t = function () {
      i[bh](this, arguments)
    });
    var s = t[yh], r = function () {
    };
    return r[yh] = i[yh], t[yh] = new r, t[gh] = i.prototype, t[gh].constructor = i, E(t[yh], s), e && E(t[yh], e), n && E(t[yh], n), t.prototype[xh] = t, t
  }

  function w(t, i, n) {
    return T(t, i, "constructor", n)
  }

  function T(t, i, n, e) {
    var s = i[gh];
    if (s) {
      var r = s[n];
      return r ? r[bh](t, e) : void 0
    }
  }

  function k(t, i, n, e) {
    if ("constructor" == n) return M(t, i, e);
    if (i[ph] instanceof Function) {
      var s = i[ph][yh][n];
      return s instanceof Function ? s[bh](t, e) : void 0
    }
  }

  function M(t, i, n) {
    return i[ph] instanceof Function ? i[ph][bh](t, n) : void 0
  }

  function O(t, i) {
    return t.super_ = i, t[yh] = Object.create(i[yh], {
      super_: {value: i, enumerable: !1},
      constructor: {value: t, enumerable: !1}
    }), t
  }

  function I(t, i, n) {
    if (!(t instanceof Function) && t instanceof Object) {
      i = t[Eh];
      var e;
      return t.hasOwnProperty("constructor") ? (e = t.constructor, delete t.constructor) : e = i ? function () {
        i[bh](this, arguments)
      } : function () {
      }, I(e, i, t)
    }
    if (i && !(i instanceof Function) && i instanceof Object) return I(t, i[Eh], i);
    if (i && O(t, i), n) {
      var s = t.prototype;
      for (var r in n) s[r] = n[r]
    }
    return t
  }

  function S(t, i, e, s, r) {
    if (s) return void Object[mh](t, i, {value: e, enumerable: !0});
    var h = {configurable: !0, enumerable: !0}, a = wh + i;
    e !== n && (t[a] = e), h.get = function () {
      return this[a]
    }, h.set = function (t) {
      var n = this[a];
      if (n == t) return !1;
      var e = new wY(this, i, t, n);
      return this[Th](e) ? (this[a] = t, r && r[ah](this, t, n), this.onEvent(e), !0) : !1
    }, Object[mh](t, i, h)
  }

  function A(t, i) {
    for (var n = 0, e = i[rh]; e > n; n++) {
      var s = i[n];
      S(t, s[kh] || s, s[Mh] || s.value, s[Oh], s[Ih])
    }
  }

  function j(t, i, n) {
    return i instanceof Object ? t = t.bind(i) : i && !n && (n = parseInt(i)), i && !n && (n = parseInt(i)), n ? setTimeout(t, n) : setTimeout(t)
  }

  function P(i, n) {
    return n && (i = i.bind(n)), t[Sh](i)
  }

  function C(t, i) {
    return t.className = i, t
  }

  function L(t, i) {
    if (!t.hasOwnProperty(Ah)) {
      var n = t.getAttribute(xh);
      if (!n) return C(t, i);
      for (var e = n[jh](Ph), s = 0, r = e[rh]; r > s; s++) if (e[s] == i) return;
      return n += Ph + i, C(t, n)
    }
    t[Ah].add(i)
  }

  function D(t, i) {
    if (!t.hasOwnProperty(Ah)) {
      var n = t[Ch](xh);
      if (!n || !n[lh](i)) return;
      for (var e = "", s = n[jh](Ph), r = 0, h = s[rh]; h > r; r++) s[r] != i && (e += s[r] + Ph);
      return C(t, e)
    }
    t[Ah][_h](i)
  }

  function R(t) {
    return !isNaN(t) && t instanceof Number || Lh == typeof t
  }

  function N(t) {
    return t !== n && (t instanceof String || Dh == typeof t)
  }

  function B(t) {
    return t !== n && (t instanceof Boolean || Rh == typeof t)
  }

  function $(t) {
    return Array.isArray(t)
  }

  function F(i) {
    i || (i = t.event), i[Nh] ? i.preventDefault() : i[Bh] = !1
  }

  function G(i) {
    i || (i = t.event), i.stopPropagation ? i[$h]() : i.cancelBubble || (i[Fh] = !0)
  }

  function z(t) {
    F(t), G(t)
  }

  function H(t) {
    return Math[Gh](Math[zh]() * t)
  }

  function Y() {
    return Math[zh]() >= .5
  }

  function U(t) {
    var i = !0;
    for (var n in t) {
      i = !1;
      break
    }
    return i
  }

  function W(t) {
    if (t && t > 0 && 1 > t) {
      var i = Math.floor(16777215 * Math[zh]());
      return Hh + (i >> 16 & 255) + Yh + (i >> 8 & 255) + Yh + (255 & i) + Yh + t.toFixed(2) + Uh
    }
    return V(Math[Gh](16777215 * Math[zh]()))
  }

  function q(t) {
    return t > 0 ? Math[Gh](t) : Math.ceil(t)
  }

  function X(t) {
    return t > 0 ? Math[uh](t) : Math[Gh](t)
  }

  function V(t) {
    return 16777216 > t ? Wh + (qh + t.toString(16)).slice(-6) : Hh + (t >> 16 & 255) + Yh + (t >> 8 & 255) + Yh + (255 & t) + Yh + ((t >> 24 & 255) / 255).toFixed(2) + Uh
  }

  function Z(t, i, n) {
    vh != typeof n || n.hasOwnProperty(Xh) || (n[Xh] = !0), Object[mh](t, i, n)
  }

  function K(t, i) {
    for (var n in i) if (Vh != n[0]) {
      var e = i[n];
      vh != typeof e || e.hasOwnProperty(Xh) || (e[Xh] = !0)
    }
    Object[Zh](t, i)
  }

  function J(i, n) {
    n || (n = t);
    for (var e = i[jh](Kh), s = 0, r = e[rh]; r > s; s++) {
      var h = e[s];
      n = n[h]
    }
    return n
  }

  function Q(t) {
    return t instanceof MouseEvent || t instanceof Object && t.touches !== n
  }

  function ti() {
    t[Jh] && console.log.apply(console, arguments)
  }

  function ii(i) {
    t[Jh] && console[Qh](i)
  }

  function ni(i) {
    t[Jh] && console.error(i)
  }

  function ei(t, i, n) {
    var e, s, r;
    0 == t._nc ? (e = -1, r = 0, s = i) : 0 == t._n9 ? (e = 0, r = 1, s = n) : (e = -1 / t._nc, s = (t._nc - e) * i + t._na, r = 1);
    var h = new fY;
    return h._nc = e, h._na = s, h._n9 = r, h._n7 = i, h._n2 = n, h._l2 = Math[ta](e, r), h[ia] = Math.cos(h._l2), h._sin = Math.sin(h._l2), h
  }

  function si(t, i, n, e, s) {
    var r, h;
    i > e ? r = -1 : e > i && (r = 1), n > s ? h = -1 : s > n && (h = 1);
    var a, o;
    if (!r) return o = 0 > h ? t.y : t.bottom, {x: i, y: o};
    if (!h) return a = 0 > r ? t.x : t[na], {x: a, y: n};
    var f = (n - s) / (i - e), c = n - f * i, u = 0 > r ? i - t.x : i - t[na], _ = 0 > h ? n - t.y : n - t[ea];
    return Math.abs(f) >= Math.abs(_ / u) ? (o = 0 > h ? t.y : t[ea], a = (o - c) / f) : (a = 0 > r ? t.x : t[na], o = f * a + c), {
      x: a,
      y: o
    }
  }

  function ri(t, i, n, e, s, r, h, a) {
    return 0 >= h || 0 >= a || 0 >= n || 0 >= e ? !1 : (h += s, a += r, n += t, e += i, (s > h || h > t) && (r > a || a > i) && (t > n || n > s) && (i > e || e > r))
  }

  function hi(t, i, n, e, s, r) {
    return s >= t && t + n >= s && r >= i && i + e >= r
  }

  function ai(t, i, n, e, s, r, h, a, o) {
    return o && (t -= o, i -= o, n += o + o, e += o + o), s >= t && r >= i && t + n >= s + h && i + e >= r + a
  }

  function oi(t, i, n, e, s, r, h, a) {
    var o = t;
    o += n;
    var f = i;
    f += e;
    var c = s;
    c += h;
    var u = r;
    return u += a, s > t && (t = s), r > i && (i = r), o > c && (o = c), f > u && (f = u), o -= t, f -= i, 0 > o || 0 > f ? null : new uY(t, i, o, f)
  }

  function fi(t) {
    return sa in t && ra in t
  }

  function ci(t, i) {
    var n = ui(t, i[ha], i.height);
    return n.x += i.x || 0, n.y += i.y || 0, n
  }

  function ui(t, i, e) {
    if (!t) return {x: 0, y: 0};
    if (N(t) && (t = dY[aa](t)), t instanceof dY) {
      var s, r, h = t.horizontalPosition, a = t.verticalPosition;
      switch (h) {
        case lY:
          s = 0;
          break;
        case bY:
          s = i;
          break;
        default:
          s = i / 2
      }
      switch (a) {
        case yY:
          r = 0;
          break;
        case xY:
          r = e;
          break;
        default:
          r = e / 2
      }
      return {x: s, y: r}
    }
    if (t.x !== n) return t[oa] ? {x: t.x * i, y: t.y * e} : {x: t.x, y: t.y};
    throw new Error("Position not be supported - " + t)
  }

  function _i(t, i, n) {
    t[hh].add(i, n), t[fa](i, n)
  }

  function di(t, i) {
    t._fi && (t._fi.remove(i), t.onChildRemove(i))
  }

  function li(t) {
    return t[ca](/^-ms-/, ua)[ca](/-([\da-z])/gi, function (t, i) {
      return i[_a]()
    })
  }

  function vi(t) {
    return t[ca](/[A-Z]/g, function (t) {
      return da + t.toLowerCase()
    }).replace(/^ms-/, la)
  }

  function bi(t, i) {
    var n = t[va];
    if (!n) return !1;
    var e, s;
    for (e in i) i.hasOwnProperty(e) && (s = $Y(e)) && (n[s] = i[e]);
    return t
  }

  function yi(t) {
    var i, n, e = "";
    for (i in t) t.hasOwnProperty(i) && (n = $Y(i)) && (e += vi(n) + ba + t[i] + ya);
    return e ? e[ga](0, e.length - 1) : e
  }

  function gi(t, i, n) {
    (i = $Y(i)) && (t.style[i] = n)
  }

  function xi(t, i) {
    return NY ? (i && !N(i) && (i = yi(i)), NY.insertRule ? void NY[xa](t + pa + i + Ea, 0) : void (NY[ma] && NY[ma](t, i, 0))) : !1
  }

  function pi(t, i) {
    var n = t[wa];
    return n ? (i = i || t[Ta](), i.width / n) : 1
  }

  function Ei(i, n) {
    i.touches && (i = i.changedTouches && i.changedTouches[rh] ? i[ka][0] : i[Ma][0]);
    var e = n[Ta](), s = i[Oa] || 0, r = i[Ia] || 0;
    iY && ZH && (t[Sa] && s == i[Aa] && (s -= t[Sa]), t[ja] && r == i[Pa] && (r -= t[ja])), s -= e[Ca], r -= e.top;
    var h = pi(n, e);
    return h && 1 !== h && (s /= h, r /= h), {x: s, y: r}
  }

  function mi(t, i) {
    var n, e;
    t.touches ? (n = t.cx, e = t.cy) : (n = t[Oa], e = t[Ia]);
    var s = pi(i);
    return s && 1 !== s && (n /= s, e /= s), {timeStamp: t[La], x: n, y: e}
  }

  function wi(t, i, n) {
    this._lz = t, this[Da] = n, this[Ra] = i, this._dragPoints = new ki, this[Na]()
  }

  function Ti(t) {
    return KH && t.metaKey || !KH && t[Ba]
  }

  function ki() {
    this[$a] = []
  }

  function Mi(t, i, n, e, s) {
    Ii(t, function (e) {
      if (i) {
        var s = e.responseXML;
        if (!s) return void (n || bU)(Fa + t + Ga);
        i(s)
      }
    }, n, e, s)
  }

  function Oi(t, i, n, e, s) {
    Ii(t, function (e) {
      if (i) {
        var s, r = e.responseText;
        if (!r) return (n || bU)(Fa + t + za), s = new Error(Fa + t + za), i(r, s);
        try {
          r = JSON[Ha](r)
        } catch (h) {
          (n || bU)(h), s = h
        }
        i(r, s)
      }
    }, n, e, s)
  }

  function Ii(t, i, n, e, s) {
    (n === !1 || e === !1) && (s = !1);
    try {
      var r = new XMLHttpRequest, h = encodeURI(t);
      if (s !== !1) {
        var a;
        a = h[lh](Ya) > 0 ? "&" : Ya, h += a + Ua + Date.now()
      }
      r[Wa](qa, h), r[Xa] = function () {
        return 4 == r[Va] ? r[Za] && 200 != r[Za] ? void (n || bU)(Fa + t + Ka) : void (i && i(r)) : void 0
      }, r.send(e)
    } catch (o) {
      (n || bU)(Fa + t + Ka, o)
    }
  }

  function ri(t, i, n, e, s, r, h, a) {
    return 0 >= h || 0 >= a || 0 >= n || 0 >= e ? !1 : (h += s, a += r, n += t, e += i, (s > h || h > t) && (r > a || a > i) && (t > n || n > s) && (i > e || e > r))
  }

  function ai(t, i, n, e, s, r, h, a) {
    return s >= t && r >= i && t + n >= s + h && i + e >= r + a
  }

  function Si(t, i, n) {
    return t instanceof Object && t.x ? ji(t, i, 0, 0) : Ai(t, i, n, 0, 0)
  }

  function Ai(t, i, n, e, s) {
    var r = Math.sin(n), h = Math.cos(n), a = t - e, o = i - s;
    return t = a * h - o * r + e, i = a * r + o * h + s, new aY(t, i, n)
  }

  function ji(t, i, n, e) {
    n = n || 0, e = e || 0;
    var s = Math.sin(i), r = Math.cos(i), h = t.x - n, a = t.y - e;
    return t.x = h * r - a * s + n, t.y = h * s + a * r + e, t
  }

  function Pi(t, i, n) {
    return Ci(t, i, n, 0, 0)
  }

  function Ci(t, i, n, e, s) {
    var r = Ai(t.x, t.y, i, e, s), h = Ai(t.x + t.width, t.y, i, e, s), a = Ai(t.x + t.width, t.y + t[Ja], i, e, s),
      o = Ai(t.x, t.y + t[Ja], i, e, s);
    return n ? n[Qa]() : n = new uY, n.addPoint(r), n[to](h), n[to](a), n.addPoint(o), n
  }

  function Li(t, i) {
    var n = this[io] || 1;
    this[va][ha] = t + no, this.style.height = i + no, this[ha] = t * n, this.height = i * n
  }

  function Di(t) {
    var i = t[eo] || t[so] || t[ro] || t[ho] || t[ao] || 1;
    return xU / i
  }

  function Ri(t, n, e) {
    var s = i[oo](fo);
    if (s.g = s.getContext(co), t !== !0 && !e) return t && n && (s[ha] = t, s[Ja] = n), s;
    var r = s.g;
    return r.ratio = s[io] = Di(r), s[uo] = Li, r._l6 = function () {
      this[fo][ha] = this[fo].width
    }, t && n && s[uo](t, n), s
  }

  function Ni(t, i) {
    return pU || (pU = Ri()), t && i && (pU.width = t, pU.height = i), pU.g
  }

  function Bi(t, i, n) {
    return (n || eY.FONT_STYLE) + Ph + (i || eY[_o]) + lo + (t || eY.FONT_FAMILY)
  }

  function $i(t, i, n, e, s, r, h, a, o, f) {
    if (t[vo](), t.translate(n, e), EU && mU > h) {
      var c = h / mU;
      t.scale(c, c), h = mU, f = null
    }
    o || (o = eY.LINE_HEIGHT), h || (h = eY[_o]), o *= h, t.font = f || Bi(r, h, a), t[bo] = s, t[yo] = go;
    for (var u = o / 2, _ = i[jh](xo), d = 0, l = _[rh]; l > d; d++) {
      var v = _[d];
      t[po](v, 0, u), u += o
    }
    t[Eo]()
  }

  function Fi(t, i, n, e, s, r) {
    if (!t) return {width: 0, height: 0};
    if (i || (i = eY[_o]), EU && mU > i) {
      var h = i / mU, a = Fi(t, mU, n, e, s);
      return a.width *= h, a[Ja] *= h, a
    }
    var o = Ni();
    o.font = r || Bi(n, i, e), s || (s = eY.LINE_HEIGHT);
    for (var f = i * s, c = 0, u = 0, _ = t[jh](xo), d = 0, l = _[rh]; l > d; d++) {
      var v = _[d];
      c = Math.max(o[mo](v)[ha], c), u += f
    }
    return {width: c, height: u}
  }

  function Gi(t, i, n, e, s) {
    var r;
    try {
      r = t[wo](i, n, e, s)
    } catch (h) {
    }
    return r
  }

  function zi(t) {
    return Math.log(t + Math[To](t * t + 1))
  }

  function Hi(t, i) {
    i = i || t(1);
    var n = 1 / i, e = .5 * n, s = Math.min(1, i / 100);
    return function (r) {
      if (0 >= r) return 0;
      if (r >= i) return 1;
      for (var h = r * n, a = 0; a++ < 10;) {
        var o = t(h), f = r - o;
        if (Math.abs(f) <= s) return h;
        h += f * e
      }
      return h
    }
  }

  function Yi(t, i, n) {
    var e = 1 - t, s = e * e * i[0] + 2 * e * t * i[2] + t * t * i[4],
      r = e * e * i[1] + 2 * e * t * i[3] + t * t * i[5];
    if (n) {
      var h = (i[0] + i[4] - 2 * i[2]) * t + i[2] - i[0], a = (i[1] + i[5] - 2 * i[3]) * t + i[3] - i[1];
      return {x: s, y: r, rotate: Math[ta](a, h)}
    }
    return {t: t, x: s, y: r}
  }

  function Ui(t, i, n) {
    var e = t - 2 * i + n;
    return 0 != e ? (t - i) / e : -1
  }

  function Wi(t, i) {
    i.add(t[4], t[5]);
    var n = Ui(t[0], t[2], t[4]);
    if (n > 0 && 1 > n) {
      var e = Yi(n, t);
      i.add(e.x, e.y)
    }
    var s = Ui(t[1], t[3], t[5]);
    if (s > 0 && 1 > s) {
      var e = Yi(s, t);
      i.add(e.x, e.y)
    }
    return i
  }

  function qi(t, i) {
    return Math.abs(t - i) < 1e-7
  }

  function Xi(t, i) {
    return qi(t[0], i[0]) && qi(t[1], i[1])
  }

  function Vi(t) {
    if (Xi([t[0], t[1]], [t[2], t[3]])) {
      var i = t[0], n = t[1], e = t[4], s = t[5], r = Math[To](wU(i, n, e, s));
      return function (t) {
        return r * t
      }
    }
    if (Xi([t[0], t[1]], [t[4], t[5]]) || Xi([t[2], t[3]], [t[4], t[5]])) {
      var i = t[0], n = t[1], e = t[2], s = t[3], r = Math[To](wU(i, n, e, s));
      return function (t) {
        return r * t
      }
    }
    var h = t[0], a = t[2], o = t[4], f = h - 2 * a + o, c = 2 * a - 2 * h;
    h = t[1], a = t[3], o = t[5];
    var u = h - 2 * a + o, _ = 2 * a - 2 * h, d = 4 * (f * f + u * u), l = 4 * (f * c + u * _), v = c * c + _ * _,
      r = 4 * d * v - l * l;
    if (1e-5 > r && r > -1e-5) {
      var b = oY(t[0], t[1], t[2], t[3]), y = oY(t[2], t[3], t[4], t[5]);
      return function (t) {
        return (2 * b + (y - b) * t) * t
      }
    }
    var g = 1 / r, x = .125 * Math.pow(d, -1.5), p = 2 * Math[To](d),
      E = (r * zi(l / Math[To](r)) + 2 * Math[To](d) * l * Math.sqrt(v)) * x;
    return function (t) {
      var i = l + 2 * t * d, n = i / Math[To](r), e = i * i * g;
      return (r * Math.log(n + Math.sqrt(e + 1)) + p * i * Math[To](v + t * l + t * t * d)) * x - E
    }
  }

  function Zi(t, i, n) {
    var e = 1 - t, s = i[0], r = i[2], h = i[4], a = i[6],
      o = s * e * e * e + 3 * r * t * e * e + 3 * h * t * t * e + a * t * t * t;
    if (n) var f = 3 * t * t * a + (6 * t - 9 * t * t) * h + (9 * t * t - 12 * t + 3) * r + (-3 * t * t + 6 * t - 3) * s;
    s = i[1], r = i[3], h = i[5], a = i[7];
    var c = s * e * e * e + 3 * r * t * e * e + 3 * h * t * t * e + a * t * t * t;
    if (n) {
      var u = 3 * t * t * a + (6 * t - 9 * t * t) * h + (9 * t * t - 12 * t + 3) * r + (-3 * t * t + 6 * t - 3) * s;
      return {x: o, y: c, rotate: Math.atan2(u, f)}
    }
    return {x: o, y: c}
  }

  function Ki(t, i, n, e) {
    var s = -t + 3 * i - 3 * n + e;
    if (0 == s) return [(t - i) / (2 * n - 4 * i + 2 * t)];
    var r = 2 * t - 4 * i + 2 * n, h = i - t, a = r * r - 4 * s * h;
    return 0 > a ? void 0 : 0 == a ? [-r / (2 * s)] : (a = Math[To](a), [(a - r) / (2 * s), (-a - r) / (2 * s)])
  }

  function Ji(t, i) {
    i.add(t[6], t[7]);
    var n = Ki(t[0], t[2], t[4], t[6]);
    if (n) for (var e = 0; e < n[rh]; e++) {
      var s = n[e];
      if (!(0 >= s || s >= 1)) {
        var r = Zi(s, t);
        i.add(r.x, r.y)
      }
    }
    if (n = Ki(t[1], t[3], t[5], t[7])) for (var e = 0; e < n.length; e++) {
      var s = n[e];
      if (!(0 >= s || s >= 1)) {
        var r = Zi(s, t);
        i.add(r.x, r.y)
      }
    }
  }

  function Qi(t) {
    var i = {x: t[0], y: t[1]}, n = {x: t[2], y: t[3]}, e = {x: t[4], y: t[5]}, s = {x: t[6], y: t[7]}, r = i.x - 0,
      h = i.y - 0, a = n.x - 0, o = n.y - 0, f = e.x - 0, c = e.y - 0, u = s.x - 0, _ = s.y - 0,
      d = 3 * (-r + 3 * a - 3 * f + u), l = 6 * (r - 2 * a + f), v = 3 * (-r + a), b = 3 * (-h + 3 * o - 3 * c + _),
      y = 6 * (h - 2 * o + c), g = 3 * (-h + o), x = function (t) {
        var i = d * t * t + l * t + v, n = b * t * t + y * t + g;
        return Math.sqrt(i * i + n * n)
      }, p = (x(0) + 4 * x(.5) + x(1)) / 6;
    return p
  }

  function tn(t, i) {
    function n(t, i, n, e) {
      var s = -t + 3 * i - 3 * n + e, r = 2 * t - 4 * i + 2 * n, h = i - t;
      return function (t) {
        return 3 * (s * t * t + r * t + h)
      }
    }

    function e(t, i) {
      var n = s(t), e = r(t);
      return Math.sqrt(n * n + e * e) * i
    }

    var s = n(t[0], t[2], t[4], t[6]), r = n(t[1], t[3], t[5], t[7]);
    i = i || 100;
    var h = 1 / i;
    return function (t) {
      if (!t) return 0;
      for (var i, n = 0, s = 0; ;) {
        if (i = n + h, i >= t) return s += e(n, i - n);
        s += e(n, h), n = i
      }
    }
  }

  function nn(t, i, n) {
    return wU(i, n, t.cx, t.cy) <= t[ko] + TU
  }

  function en(t, i, n, e) {
    return n = n || sn(t, i), new rn((t.x + i.x) / 2, (t.y + i.y) / 2, n / 2, t, i, null, e)
  }

  function sn(t, i) {
    return oY(t.x, t.y, i.x, i.y)
  }

  function rn(t, i, n, e, s, r, h) {
    this.cx = t, this.cy = i, this.r = n, this[ko] = n * n, this.p1 = e, this.p2 = s, this.p3 = r, this[Mo] = h
  }

  function hn(t, i, n, e) {
    this.cx = t, this.cy = i, this.width = n, this.height = e
  }

  function an(t) {
    var i = t[0], n = t[1], e = t[2], s = rn[Oo](i, n, e);
    return fn(t, i, n, e, s)
  }

  function on(t, i) {
    i = i || cn(t);
    for (var n, e = i[ha] / i.height, s = [], r = t[rh], h = 0; r > h; h++) n = t[h], s[Io]({x: n.x, y: n.y * e});
    var a = an(s);
    return a ? new hn(a.cx, a.cy / e, 2 * a.r, 2 * a.r / e) : void 0
  }

  function fn(t, i, n, e, s) {
    for (var r, h, a = t[rh], o = s[ko], f = 0; a > f; f++) if (r = t[f], r != i && r != n && r != e) {
      var c = wU(s.cx, s.cy, r.x, r.y);
      c - TU > o && (o = c, h = r)
    }
    if (!h) return s;
    var u, _ = rn._k6Circle(h, i, n), d = rn[Oo](h, i, e), l = rn[Oo](h, e, n);
    return nn(_, e.x, e.y) && (u = _), nn(d, n.x, n.y) && (!u || u.r > d.r) && (u = d), nn(l, i.x, i.y) && (!u || u.r > l.r) && (u = l), i = u.p1, n = u.p2, e = u.p3 || u._otherPoint, fn(t, i, n, e, u)
  }

  function cn(t) {
    for (var i, n = t[rh], e = new uY, s = 0; n > s; s++) i = t[s], e.add(i.x, i.y);
    return e
  }

  function un(t, i, n, e, s) {
    this._63 && this[So]();
    var r = s ? this.getBounds(s) : this[Ao], h = n / r.width, a = t - h * r.x, o = e / r[Ja], f = i - o * r.y,
      c = this._f6, u = [];
    return l(c, function (t) {
      var i = t.clone(), n = i[$a];
      if (n && n[rh]) {
        for (var e = n[rh], s = [], r = 0; e > r; r++) {
          var c = n[r];
          r++;
          var _ = n[r];
          c = h * c + a, _ = o * _ + f, s[Io](c), s[Io](_)
        }
        i[$a] = s
      }
      u[Io](i)
    }, this), new iW(u)
  }

  function _n(t, i, n, e, s, r) {
    if (s = s || 0, n = n || 0, !s && !r) return !1;
    if (!e) {
      var h = this.getBounds(s);
      if (!h[jo](t, i, n)) return !1
    }
    var a = Math.round(2 * n) || 1, o = Ni(a, a), f = (o.canvas, -t + n), c = -i + n;
    if (o[Po](1, 0, 0, 1, f, c), !o[Co]) {
      this._lk(o), s && o[Lo](), r && o[Do]();
      var u = Gi(o, 0, 0, a, a);
      if (!u) return !1;
      u = u[Ro];
      for (var _ = u[rh] / 4; _ > 0;) {
        if (u[4 * _ - 1] > tW) return !0;
        --_
      }
      return !1
    }
    return o[No] = (s || 0) + 2 * n, this._lk(o), s && o[Co](n, n) ? !0 : r ? o[Bo](n, n) : !1
  }

  function dn(t, i, n) {
    if (!this._iy) return null;
    var e = this._f6;
    if (e[rh] < 2) return null;
    n === !1 && (t += this._iy);
    var s = e[0];
    if (0 >= t) return Ks(s.points[0], s[$a][1], e[1][$a][0], e[1][$a][1], t, i);
    if (t >= this._iy) {
      s = e[e[rh] - 1];
      var r, h, a = s[$a], o = a[rh], f = a[o - 2], c = a[o - 1];
      if (o >= 4) r = a[o - 4], h = a[o - 3]; else {
        s = e[e[rh] - 2];
        var u = s[$o];
        r = u.x, h = u.y
      }
      return Ks(f, c, f + f - r, c + c - h, t - this._iy, i)
    }
    for (var _, d = 0, l = 1, o = e[rh]; o > l; l++) if (_ = e[l], _._iy) {
      if (!(d + _._iy < t)) {
        var v, u = s[$o];
        if (_.type == KU) {
          var b = _[$a];
          v = ln(t - d, _, u.x, u.y, b[0], b[1], b[2], b[3], _._r)
        } else {
          if (!_._lf) return Ks(u.x, u.y, _[$a][0], _[$a][1], t - d, i);
          var y = Hi(_._lf, _._iy)(t - d), b = _.points;
          v = _[Fo] == ZU && 6 == b.length ? Zi(y, [u.x, u.y][fh](b), !0) : Yi(y, [u.x, u.y][fh](b), !0), v.t = y
        }
        return i && (v.x -= i * Math.sin(v[Go] || 0), v.y += i * Math.cos(v[Go] || 0)), v
      }
      d += _._iy, s = _
    } else s = _
  }

  function ln(t, i, n, e, s, r, h, a) {
    if (t <= i._l1) return Ks(n, e, s, r, t, t);
    if (t >= i._iy) return t -= i._iy, Ks(i._p2x, i._p2y, h, a, t, t);
    if (t -= i._l1, i._o) {
      var o = t / i._r;
      i[zo] && (o = -o);
      var f = Ai(i[Ho], i[Yo], o, i._o.x, i._o.y);
      return f[Go] += i[Uo] || 0, f.rotate += Math.PI, f
    }
    return Ks(i._p1x, i[Yo], i[Wo], i[qo], t, t)
  }

  function ei(t, i, n) {
    var e, s, r;
    0 == t._nc ? (e = -1, r = 0, s = i) : 0 == t._n9 ? (e = 0, r = 1, s = n) : (e = -1 / t._nc, s = (t._nc - e) * i + t._na, r = 1);
    var h = new fY;
    return h._nc = e, h._na = s, h._n9 = r, h._n7 = i, h._n2 = n, h
  }

  function vn(t) {
    return t %= 2 * Math.PI, 0 > t && (t += 2 * Math.PI), t
  }

  function bn(t, i, n, e, s, r, h, a) {
    var o = oY(i, n, e, s), f = oY(e, s, r, h);
    if (!o || !f) return t._d = 0, t._r = 0, t._l1 = o, t._l2 = f, t._iy = 0;
    var c = gn(e, s, i, n), u = gn(e, s, r, h);
    t[Uo] = c, t[Xo] = u;
    var _ = c - u;
    _ = vn(_), _ > Math.PI && (_ = 2 * Math.PI - _, t._CCW = !0);
    var d = Math.PI - _, l = Math.tan(_ / 2), v = a / l, b = Math.min(o, f);
    v > b && (v = b, a = l * v);
    var y, g = e + Math.cos(c) * v, x = s + Math.sin(c) * v, p = e + Math.cos(u) * v, E = s + Math.sin(u) * v,
      m = new fY(i, n, e, s), w = new fY(e, s, r, h), T = ei(m, g, x), k = ei(w, p, E), M = T._3s(k),
      O = Math[ta](x - M.y, g - M.x), I = Math[ta](E - M.y, p - M.x);
    y = t[zo] ? I : O;
    for (var S, A = 0; 4 > A;) {
      var j = A * rY;
      if (vn(j - y) <= d) {
        var P, C;
        if (S ? S++ : S = 1, 0 == A ? (P = M.x + a, C = M.y) : 1 == A ? (P = M.x, C = M.y + a) : 2 == A ? (P = M.x - a, C = M.y) : (P = M.x, C = M.y - a), t[Vo + S] = {
          x: P,
          y: C
        }, 2 == S) break
      }
      A++
    }
    return t[Ho] = g, t[Yo] = x, t[Wo] = p, t._p2y = E, t._o = M, t._d = v, t._r = a, t._l1 = o - v, t._l2 = f - v, t._iy = t._l1 + d * a
  }

  function yn(t, i, n, e, s, r, h) {
    var a = gn(n, e, t, i), o = gn(n, e, s, r), f = a - o;
    return h ? f : (0 > f && (f = -f), f > Math.PI && (f -= Math.PI), f)
  }

  function gn(t, i, n, e) {
    return Math[ta](e - i, n - t)
  }

  function xn(t, i) {
    for (var n = t[wo](0, 0, i[ha], i.height)[Ro], e = !1, s = 3, r = n[rh]; r > s; s += 4) if (n[s] < 255) {
      e = !0;
      break
    }
    return e
  }

  function pn(t) {
    return t = mn(t), /^png/i.test(t) || /^gif/i[Zo](t)
  }

  function En(t, i, n, e, s) {
    if (i && n) {
      t[No] = .5, t[Ko] = Jo, t[Qo](0, 0, i, n), e && (t[tf] = e, t.fillRect(0, 0, i, n));
      var r = 2, h = 40, a = 28, o = 10, f = Math.min(i / (h + o), n / (a + o));
      t[vo](), t[nf] = .6, t.translate(i / 2, n / 2), t[ef](f, f), t[sf](-h / 2, -a / 2), t[rf](), t[hf](0, 0, h, a), t.clip(), t[tf] = af, t[Do](), t.globalAlpha = 1, t[of](0, 21), t[ff](12, 12), t[ff](26, 28), t[of](18, 18), t[ff](28, 8), t[ff](40, 18), t[No] = r, t.strokeStyle = cf, t.stroke(), s && (t[rf](), t[of](26, -4), t.lineTo(17, 11), t.lineTo(23, 17), t[ff](14, 32), t[No] = 5, t.strokeStyle = e || uf, t.stroke()), t[Eo]()
    }
  }

  function mn(t) {
    var i = MU.exec(t);
    if (i) return i[1];
    var n = t[_f](Kh);
    return n >= 0 && n < t[rh] - 1 ? t.substring(n + 1) : void 0
  }

  function wn(t) {
    if (!t) return null;
    if (t instanceof iW) return CU;
    if (t.draw instanceof Function) return PU;
    if (N(t)) {
      var i = mn(t);
      if (i) {
        if (!HH && OU.test(i)) return jU;
        if (IU[Zo](i)) return AU
      }
      return SU
    }
  }

  function Tn(t, i, n) {
    if (this._lp = wn(t), !this._lp) throw new Error("the image format is not supported", t);
    this._lr = t, this[df] = i, this._8u = n, this.width = i || eY.IMAGE_WIDTH, this.height = n || eY[lf], this._ja = {}
  }

  function kn(t, i, n, e) {
    return i ? (NU[t] = new Tn(i, n, e), t) : void delete NU[t]
  }

  function Mn(t) {
    if (t._kj) return t._kj;
    var i = N(t);
    if (!i && !t[kh]) return t._kj = new Tn(t);
    var n = t.name || t;
    return n in NU ? NU[n] : NU[n] = new Tn(t)
  }

  function On(t) {
    return t in NU
  }

  function In(t, i, n) {
    n = n || {};
    var e = t[vf](n[No]);
    if (!e[ha] || !e.height) return !1;
    var s = i[bf](co), r = i[io] || 1, h = n[yf] || gf, a = /full/i[Zo](h), o = /uniform/i[Zo](h), f = 1, c = 1;
    if (a) {
      var u = i[ha], _ = i[Ja], d = n[xf], l = 0, v = 0;
      if (d) {
        var b, y, g, x;
        R(d) ? b = y = g = x = d : (b = d.top || 0, y = d[ea] || 0, g = d[Ca] || 0, x = d[na] || 0), u -= g + x, _ -= b + y, l += g, v += b
      }
      f = u / e[ha], c = _ / e[Ja], o && (f > c ? (l += (u - c * e[ha]) / 2, f = c) : c > f && (v += (_ - f * e[Ja]) / 2, c = f)), (l || v) && s[sf](l, v)
    }
    s.translate(-e.x * f, -e.y * c), t[pf](s, r, n, f, c, !0)
  }

  function Sn(t, i, n) {
    var e = Mn(t);
    return e ? (e[So](), (e._lp == jU || e._6a()) && e[Ef](function (t) {
      t[mf] && (this[ha] = this.width, In(t[mf], this, n))
    }, i), void In(e, i, n)) : (yU[wf](Tf + t), !1)
  }

  function An(t, i, n, e) {
    var s = t[rh];
    if (s && !(0 > s)) {
      e = e || 1;
      for (var r, h, a = 0; a++ < s;) if (r = t[kf](a, 0), r && (h = oY(i, n, r.x, r.y), !(h > e))) {
        for (var o = 0; o++ < e;) if (r = t[kf](a + o, 0)) {
          var f = oY(i, n, r.x, r.y);
          if (f >= h) {
            a += o;
            break
          }
          h = f
        }
        for (var c = a, u = t[rh] - 1, _ = 0, a = 0, d = t._f6[rh]; d > a; a++) if (_ += t._f6[a]._iy || 0, _ > c) {
          u = a;
          break
        }
        return r[rh] = c, r.index = u, r
      }
    }
  }

  function jn(t, i, n) {
    return {x: t.x + (i.x - t.x) * n, y: t.y + (i.y - t.y) * n}
  }

  function Pn(t, i, n, e, s) {
    e = e || eY.ADD_SEGMENT_TYPE;
    var r = An(t, i, n, s);
    if (r) {
      i = r.x, n = r.y;
      var h = t._f6, a = r[Mf], o = a == h[rh] - 1, f = h[a], c = h.slice(0);
      if (f[Fo] == XU || f[Fo] == JU) if (Of == e) c[oh](a, 0, new QU(XU, [r.x, r.y])); else {
        var u = h[a - 1].lastPoint, _ = f.firstPoint, d = oY(u.x, u.y, i, n) / oY(u.x, u.y, _.x, _.y), l = jn(u, r, d),
          v = jn(r, _, d);
        c[oh](a, 1, new QU(VU, [l.x, l.y, i, n]), new QU(VU, f.invalidTerminal ? [v.x, v.y] : [v.x, v.y, _.x, _.y]))
      } else if (If in r && Of != e) {
        var d = r.t;
        if (f[Fo] == ZU) {
          var u = h[a - 1][$o], _ = {x: f[$a][0], y: f[$a][1]}, b = {x: f[$a][2], y: f[$a][3]},
            y = {x: f[$a][4], y: f[$a][5]}, l = jn(u, _, d), v = jn(_, b, d), g = jn(b, y, d), x = jn(l, v, d),
            p = jn(v, g, d);
          c.splice(a, 1, new QU(ZU, [l.x, l.y, x.x, x.y, i, n]), new QU(ZU, f[Sf] ? [p.x, p.y, g.x, g.y] : [p.x, p.y, g.x, g.y, y.x, y.y]))
        } else if (f[Fo] == VU) {
          var u = h[a - 1].lastPoint, _ = {x: f.points[0], y: f[$a][1]}, b = {x: f[$a][2], y: f[$a][3]},
            l = jn(u, _, d), v = jn(_, b, d);
          c.splice(a, 1, new QU(VU, [l.x, l.y, i, n]), new QU(VU, f[Sf] ? [v.x, v.y] : [v.x, v.y, b.x, b.y]))
        }
      } else {
        var _ = f.lastPoint;
        c.splice(a, 1, new QU(XU, [i, n]), new QU(XU, [_.x, _.y]))
      }
      return {atEnd: o, index: a, isCurve: Of !== e, segments: c}
    }
  }

  function Cn(t) {
    var i = t[ha], n = t.height, e = Gi(t.g, 0, 0, i, n);
    return e ? Dn(e[Ro], i, n) : void 0
  }

  function Ln(t, i, n) {
    this._$x(t, i, n)
  }

  function Dn(t, i, n) {
    return new Ln(t, i, n)
  }

  function Rn(t) {
    if (Wh == t[0]) {
      if (t = t[ga](1), 3 == t[rh]) t = t[0] + t[0] + t[1] + t[1] + t[2] + t[2]; else if (6 != t[rh]) return;
      return t = parseInt(t, 16), [t >> 16 & 255, t >> 8 & 255, 255 & t]
    }
    if (/^rgb/i[Zo](t)) {
      var i = t[lh](Af), n = t.indexOf(Uh);
      if (0 > i || i > n) return;
      if (t = t.substring(i + 1, n), t = t[jh](Yh), t[rh] < 3) return;
      var e = parseInt(t[0]), s = parseInt(t[1]), r = parseInt(t[2]), h = 3 == t.length ? 255 : parseInt(t[3]);
      return [e, s, r, h]
    }
    yU[wf]("color format error, [" + t + jf)
  }

  function Nn(t, i, n) {
    return n || (n = eY[Pf]), n == gU[Cf] ? t * i : n == gU[Lf] ? Math.min(t, i) : n == gU[Df] ? 1 - (1 - i) / t : n == gU[Rf] ? t + i - 1 : n == gU.BLEND_MODE_LIGHTEN ? Math.max(t, i) : n == gU[Nf] ? t + i - t * i : i
  }

  function Bn(t, i, n) {
    var e = Gi(t.g, 0, 0, t.width, t[Ja]);
    if (e) {
      var s = e.data;
      if (n instanceof Function) s = n(t, s, i) || s; else {
        var r = i[0] / 255, h = i[1] / 255, a = i[2] / 255;
        if (n == gU.BLEND_MODE_GRAY) for (var o = 0, f = s[rh]; f > o; o += 4) {
          var c = 77 * s[o] + 151 * s[o + 1] + 28 * s[o + 2] >> 8;
          s[o] = c * r | 0, s[o + 1] = c * h | 0, s[o + 2] = c * a | 0
        } else for (var o = 0, f = s[rh]; f > o; o += 4) s[o] = 255 * Nn(r, s[o] / 255, n) | 0, s[o + 1] = 255 * Nn(h, s[o + 1] / 255, n) | 0, s[o + 2] = 255 * Nn(a, s[o + 2] / 255, n) | 0
      }
      var t = Ri(t.width, t.height);
      return t.g.putImageData(e, 0, 0), t
    }
  }

  function $n(t, i, n, e) {
    return 1 > n && (n = 1), Fn(t - n, i - n, 2 * n, 2 * n, e)
  }

  function Fn(t, i, n, e, s) {
    n = Math[Bf](n) || 1, e = Math[Bf](e) || 1;
    var r = Ni(n, e);
    r[Po](1, 0, 0, 1, -t, -i), s.draw(r);
    var h = Gi(r, 0, 0, n, e);
    if (!h) return !1;
    h = h[Ro];
    for (var a = h[rh] / 4; a-- > 0;) if (h[4 * a - 1] > tW) return !0;
    return !1
  }

  function Gn(t, i, n, e, s, r) {
    t -= s.$x, i -= s.$y;
    var h = s._fk[$f](t, i, n, e);
    if (!h) return !1;
    t = h.x * r, i = h.y * r, n = h[ha] * r, e = h[Ja] * r, n = Math[Bf](n) || 1, e = Math[Bf](e) || 1;
    var a = Ni(), o = a.canvas;
    o[ha] < n || o[Ja] < e ? (o[ha] = n, o[Ja] = e) : (a[Po](1, 0, 0, 1, 0, 0), a[Ff](0, 0, n, e)), a[Po](1, 0, 0, 1, -t - s.$x * r, -i - s.$y * r), a.scale(r, r), s._jm(a, 1);
    var f = Gi(a, 0, 0, n, e);
    if (!f) return !1;
    f = f[Ro];
    for (var c = f[rh] / 4; c-- > 0;) if (f[4 * c - 1] > tW) return !0;
    return !1
  }

  function zn(t, i, n, e, s, r, h, a, o) {
    if (hi(t, i, n, e, a, o)) return null;
    var f, c, u, _ = new QU(XU, [t + n - s, i]), d = new QU(VU, [t + n, i, t + n, i + r]),
      l = new QU(XU, [t + n, i + e - r]), v = new QU(VU, [t + n, i + e, t + n - s, i + e]),
      b = new QU(XU, [t + s, i + e]), y = new QU(VU, [t, i + e, t, i + e - r]), g = new QU(XU, [t, i + r]),
      x = new QU(VU, [t, i, t + s, i]), p = (new QU(JU), [_, d, l, v, b, y, g, x]),
      E = new uY(t + s, i + r, n - s - s, e - r - r);
    t > a ? (f = lY, u = 5) : a > t + n ? (f = bY, u = 1) : (f = vY, u = 0), i > o ? (c = yY, f == lY && (u = 7)) : o > i + e ? (c = xY, f == bY ? u = 3 : f == vY && (u = 4)) : (c = gY, f == lY ? u = 6 : f == bY && (u = 2));
    var m = Xn(u, t, i, n, e, s, r, h, a, o, E), w = m[0], T = m[1], k = new iW, M = k._f6;
    M[Io](new QU(qU, [w.x, w.y])), M[Io](new QU(XU, [a, o])), M[Io](new QU(XU, [T.x, T.y])), T._lx && (M.push(T._lx), T[Gf]++);
    for (var O = T[Gf] % 8, I = w[Gf]; M.push(p[O]), ++O, O %= 8, O != I;) ;
    return w._lx && M[Io](w._lx), k[zf](), k
  }

  function Hn(t, i, e, s, r, h, a, o, f, c, u, _, d, l) {
    var v = new fY(_, d, e, s), b = new fY(i[0], i[1], i[4], i[5]), y = b._3s(v, u), g = y[0], x = y[1];
    if (g[Hf] !== n) {
      g[Gf] = (t - 1) % 8, x[Gf] = (t + 1) % 8;
      var p = g._rest;
      7 == t ? (g.y = h + c + Math.min(l[Ja], p), x.x = r + f + Math.min(l.width, p)) : 5 == t ? (g.x = r + f + Math.min(l[ha], p), x.y = h + o - c - Math.min(l.height, p)) : 3 == t ? (g.y = h + o - c - Math.min(l[Ja], p), x.x = r + a - f - Math.min(l.width, p)) : 1 == t && (g.x = r + a - f - Math.min(l[ha], p), x.y = h + c + Math.min(l[Ja], p))
    } else {
      v._mx(v._n7, v._n2, g.x, g.y), g = v._$c(i), v._mx(v._n7, v._n2, x.x, x.y), x = v._$c(i);
      var E = Vn(i, [g, x]), m = E[0], w = E[2];
      g[Gf] = t, x._lxNO = t, g._lx = new QU(VU, m.slice(2)), x._lx = new QU(VU, w.slice(2))
    }
    return [g, x]
  }

  function Yn(t, i, n, e, s, r, h, a, o, f) {
    var c, u;
    if (o - a >= i + r) c = {y: n, x: o - a}, c[Gf] = 0; else {
      c = {y: n + h, x: Math.max(i, o - a)};
      var _ = [i, n + h, i, n, i + r, n], d = new fY(o, f, c.x, c.y);
      if (c = d._$c(_)) {
        $(c) && (c = c[0].t > c[1].t ? c[0] : c[1]);
        var l = Vn(_, [c]);
        l = l[0], l && (c._lx = new QU(VU, l[ch](2))), c[Gf] = 7
      } else c = {y: n, x: i + r}, c[Gf] = 0
    }
    if (i + e - r >= o + a) u = {y: n, x: o + a}, u[Gf] = 0; else {
      u = {y: n + h, x: Math.min(i + e, o + a)};
      var v = [i + e - r, n, i + e, n, i + e, n + h], d = new fY(o, f, u.x, u.y);
      if (u = d._$c(v)) {
        $(u) && (u = u[0].t < u[1].t ? u[0] : u[1]);
        var l = Vn(v, [u]);
        l && l[l.length - 1] && (u._lx = new QU(VU, l[l[rh] - 1].slice(2))), u[Gf] = 1
      } else u = {y: n, x: i + e - r}, u[Gf] = 0
    }
    return [c, u]
  }

  function Un(t, i, n, e, s, r, h, a, o, f) {
    var c, u;
    if (f - a >= n + h) c = {x: i + e, y: f - a}, c[Gf] = 2; else {
      c = {x: i + e - r, y: Math.max(n, f - a)};
      var _ = [i + e - r, n, i + e, n, i + e, n + h], d = new fY(o, f, c.x, c.y);
      if (c = d._$c(_)) {
        $(c) && (c = c[0].t > c[1].t ? c[0] : c[1]);
        var l = Vn(_, [c]);
        l = l[0], l && (c._lx = new QU(VU, l[ch](2))), c[Gf] = 1
      } else c = {x: i + e, y: n + h}, c._lxNO = 2
    }
    if (n + s - h >= f + a) u = {x: i + e, y: f + a}, u[Gf] = 2; else {
      u = {x: i + e - r, y: Math.min(n + s, f + a)};
      var v = [i + e, n + s - h, i + e, n + s, i + e - r, n + s], d = new fY(o, f, u.x, u.y);
      if (u = d._$c(v)) {
        $(u) && (u = u[0].t < u[1].t ? u[0] : u[1]);
        var l = Vn(v, [u]);
        l[1] && (u._lx = new QU(VU, l[1].slice(2))), u._lxNO = 3
      } else u = {x: i + e, y: n + s - h}, u[Gf] = 2
    }
    return [c, u]
  }

  function Wn(t, i, n, e, s, r, h, a, o, f) {
    var c, u;
    if (o - a >= i + r) u = {y: n + s, x: o - a}, u._lxNO = 4; else {
      u = {y: n + s - h, x: Math.max(i, o - a)};
      var _ = [i + r, n + s, i, n + s, i, n + s - h], d = new fY(o, f, u.x, u.y);
      if (u = d._$c(_)) {
        $(u) && (u = u[0].t < u[1].t ? u[0] : u[1]);
        var l = Vn(_, [u]);
        l = l[l.length - 1], l && (u._lx = new QU(VU, l[ch](2))), u[Gf] = 5
      } else u = {y: n + s, x: i + r}, u[Gf] = 4
    }
    if (i + e - r >= o + a) c = {y: n + s, x: o + a}, c[Gf] = 4; else {
      c = {y: n + s - h, x: Math.min(i + e, o + a)};
      var v = [i + e, n + s - h, i + e, n + s, i + e - r, n + s], d = new fY(o, f, c.x, c.y);
      if (c = d._$c(v)) {
        $(c) && (c = c[0].t > c[1].t ? c[0] : c[1]);
        var l = Vn(v, [c]);
        l[0] && (c._lx = new QU(VU, l[0][ch](2))), c[Gf] = 3
      } else c = {y: n + s, x: i + e - r}, c[Gf] = 4
    }
    return [c, u]
  }

  function qn(t, i, n, e, s, r, h, a, o, f) {
    var c, u;
    if (f - a >= n + h) u = {x: i, y: f - a}, u[Gf] = 6; else {
      u = {x: i + r, y: Math.max(n, f - a)};
      var _ = [i, n + h, i, n, i + r, n], d = new fY(o, f, u.x, u.y);
      if (u = d._$c(_)) {
        $(u) && (u = u[0].t < u[1].t ? u[0] : u[1]);
        var l = Vn(_, [u]);
        l = l[l[rh] - 1], l && (u._lx = new QU(VU, l[ch](2)))
      } else u = {x: i, y: n + h};
      u[Gf] = 7
    }
    if (n + s - h >= f + a) c = {x: i, y: f + a}, c._lxNO = 6; else {
      c = {x: i + r, y: Math.min(n + s, f + a)};
      var v = [i + r, n + s, i, n + s, i, n + s - h], d = new fY(o, f, c.x, c.y);
      if (c = d._$c(v)) {
        $(c) && (c = c[0].t > c[1].t ? c[0] : c[1]);
        var l = Vn(v, [c]);
        l[0] && (c._lx = new QU(VU, l[0][ch](2))), c[Gf] = 5
      } else c = {x: i, y: n + s - h}, c._lxNO = 6
    }
    return [c, u]
  }

  function Xn(t, i, n, e, s, r, h, a, o, f, c) {
    var u = a / 2;
    switch (r = r || 1e-4, h = h || 1e-4, t) {
      case 7:
        var _ = [i, n + h, i, n, i + r, n], d = i + r, l = n + h;
        return Hn(t, _, d, l, i, n, e, s, r, h, a, o, f, c);
      case 5:
        return _ = [i + r, n + s, i, n + s, i, n + s - h], d = i + r, l = n + s - h, Hn(t, _, d, l, i, n, e, s, r, h, a, o, f, c);
      case 3:
        return _ = [i + e, n + s - h, i + e, n + s, i + e - r, n + s], d = i + e - r, l = n + s - h, Hn(t, _, d, l, i, n, e, s, r, h, a, o, f, c);
      case 1:
        return _ = [i + e - r, n, i + e, n, i + e, n + h], d = i + e - r, l = n + h, Hn(t, _, d, l, i, n, e, s, r, h, a, o, f, c);
      case 0:
        return Yn(t, i, n, e, s, r, h, u, o, f, c);
      case 2:
        return Un(t, i, n, e, s, r, h, u, o, f, c);
      case 4:
        return Wn(t, i, n, e, s, r, h, u, o, f, c);
      case 6:
        return qn(t, i, n, e, s, r, h, u, o, f, c)
    }
  }

  function Vn(t, i) {
    for (var e, s, r, h, a, o, f = t[0], c = t[1], u = t[2], _ = t[3], d = t[4], l = t[5], v = [], b = 0; b < i[rh]; b++) a = i[b], o = a.t, 0 != o && 1 != o ? (e = f + (u - f) * o, s = c + (_ - c) * o, r = u + (d - u) * o, h = _ + (l - _) * o, v[Io]([f, c, e, s, a.x, a.y]), f = a.x, c = a.y, u = r, _ = h) : v[Io](null);
    return r !== n && v[Io]([a.x, a.y, r, h, d, l]), v
  }

  function Zn(t) {
    return this[Yf] && this[Uf] && (t.x -= this[Uf].x, t.y -= this[Uf].y), this[Wf] && ji(t, this.$rotate), t.x += this.$offsetX || 0, t.y += this.$offsetY || 0, this[qf] && this[Xf] ? ji(t, this[Xf]) : t
  }

  function Kn(t) {
    return this.$rotatable && this.$_hostRotate && ji(t, -this[Xf]), t.x -= this.$offsetX || 0, t.y -= this[Vf] || 0, this[Wf] && ji(t, -this[Wf]), this.$layoutByAnchorPoint && this._nc8 && (t.x += this._nc8.x, t.y += this[Uf].y), t
  }

  function Jn() {
    var t = this[Zf];
    this[Zf] && (this[Zf] = !1, this[Kf] = !0, this._85[Jf](this._jk), this[Qf] && this._85[tc](this.$padding), this.$border && this._85[tc](this.$border));
    var i = this._$l();
    if (i) var n = this.showPointer && this[ic];
    return this.$invalidateAnchorPoint && this[Yf] && (this.$invalidateAnchorPoint = !1, n && (t = !0), this[Uf] = ui(this[nc], this._85[ha], this._85.height), this[Uf].x += this._85.x, this._nc8.y += this._85.y), i ? (t && (this._naackgroundGradientInvalidateFlag = !0, Qn[ah](this, n)), this[ec] && (this[ec] = !1, this._naackgroundGradient = this[sc] && this[rc] && this[rc][Ao] ? BU[yh].generatorGradient.call(this[sc], this[rc].bounds) : null), t) : (this[hc] = !1, t)
  }

  function Qn(t) {
    var i = this._85.x + this[ac] / 2, n = this._85.y + this.$border / 2, e = this._85[ha] - this.$border,
      s = this._85[Ja] - this.$border, r = 0, h = 0;
    if (this[oc] && (R(this[oc]) ? r = h = this[oc] : (r = this[oc].x || 0, h = this[oc].y || 0), r = Math.min(r, e / 2), h = Math.min(h, s / 2)), t && (this[fc] = this[Uf].x - this[cc] + this[uc], this[_c] = this[Uf].y - this[Vf] + this[dc], !this._85[jo](this._pointerX, this._pointerY))) {
      var a = new eW(i, n, e, s, r, h, this[ic], this[fc], this[_c]);
      return this._lvShape = a._lx, this[rc].bounds.set(i, n, e, s), void (this.__m1Pointer = !0)
    }
    this[rc] && this[rc][Qa](), this[rc] = Mq[lc](i, n, e, s, r, h, this._lvShape), this._lvShape[Ao].set(i, n, e, s)
  }

  function te(t, i, n, e) {
    return e && (t[ha] < 0 || t.height < 0) ? (t.x = i, t.y = n, void (t[ha] = t[Ja] = 0)) : (i < t.x ? (t[ha] += t.x - i, t.x = i) : i > t.x + t[ha] && (t[ha] = i - t.x), void (n < t.y ? (t[Ja] += t.y - n, t.y = n) : n > t.y + t[Ja] && (t[Ja] = n - t.y)))
  }

  function ie(t, i, e) {
    var s, r = t[vc], h = t[bc] === n ? this[bc] : t[bc];
    return this.$data instanceof iW && h ? (s = kU._nck(r, this[yc], this.lineWidth, i, e), s.x *= this._ju, s.y *= this._jw) : (s = ui(r, this._85.width, this._85.height), s.x += this._85.x, s.y += this._85.y), Zn[ah](this, s)
  }

  function ne(t, i) {
    if (i) if (i._85[gc]()) t.$x = i.$x, t.$y = i.$y; else {
      var n = ie[ah](i, t);
      t.$x = n.x, t.$y = n.y, t[xc] = n[Go]
    } else t.$x = 0, t.$y = 0;
    t[pc] && hW[ah](t)
  }

  function ee(t) {
    if (t[Ec] === n) {
      var i, e;
      if (t.setLineDash) i = t[mc], e = t[wc]; else {
        var s;
        if (t[Tc] !== n) s = Tc; else {
          if (t[kc] === n) return !1;
          s = kc
        }
        e = function (t) {
          this[s] = t
        }, i = function () {
          return this[s]
        }
      }
      Z(t, Ec, {
        get: function () {
          return i[ah](this)
        }, set: function (t) {
          e[ah](this, t)
        }
      })
    }
    if (t[Mc] === n) {
      var r;
      if (t[Oc] !== n) r = Oc; else {
        if (t[Ic] === n) return;
        r = Ic
      }
      Z(t, Mc, {
        get: function () {
          return this[r]
        }, set: function (t) {
          this[r] = t
        }
      })
    }
  }

  function se(t, i, n, e, s) {
    var r, h, a, o, f, c, u, _, d = function (t) {
      return function (i) {
        t(i)
      }
    }, l = function () {
      h = null, a = null, o = f, f = null, c = null
    }, v = function (t) {
      r = t, u || (u = Ri()), u[ha] = r.width, u[Ja] = r[Ja], i[ha] = r.width, i[Ja] = r[Ja]
    }, b = function (t) {
      y(), l(), h = t[Sc] ? t[Ac] : null, a = 10 * t[jc], f = t[Pc]
    }, y = function () {
      if (c) {
        var t = c[wo](0, 0, r[ha], r[Ja]), n = {data: t, _pixels: Dn(t[Ro], r[ha], r[Ja]), delay: a};
        s[ah](i, n)
      }
    }, g = function (t) {
      c || (c = u[bf](co));
      var i = t[Cc] ? t.lct : r.gct, n = c[wo](t.leftPos, t.topPos, t[ha], t[Ja]);
      t[Lc][Dc](function (t, e) {
        h !== t ? (n.data[4 * e + 0] = i[t][0], n[Ro][4 * e + 1] = i[t][1], n[Ro][4 * e + 2] = i[t][2], n.data[4 * e + 3] = 255) : (2 === o || 3 === o) && (n[Ro][4 * e + 3] = 0)
      }), c[Rc](n, t.leftPos, t[Nc])
    }, x = function () {
    }, p = {
      hdr: d(v), gce: d(b), com: d(x), app: {NETSCAPE: d(x)}, img: d(g, !0), eof: function () {
        y(), n[ah](i)
      }
    }, E = new XMLHttpRequest;
    HH || E[Bc]("text/plain; charset=x-user-defined"), E[$c] = function () {
      _ = new uW(E[Fc]);
      try {
        dW(_, p)
      } catch (t) {
        e[ah](i, Ha)
      }
    }, E[Gc] = function () {
      e[ah](i, zc)
    }, E[Wa](qa, t, !0), E.send()
  }

  function re(t) {
    var i = [49, 10, 10, 113, 117, 110, 101, 101, 10, 50, 46, 48, 10, 49, 53, 53, 55, 57, 57, 48, 53, 51, 55, 54, 49, 54, 10, 50, 48, 49, 57, 46, 57, 46, 49, 10, 10, 116, 101, 109, 112, 32, 108, 105, 99, 101, 110, 115, 101, 32, 45, 32, 50, 48, 49, 57, 46, 57, 46, 49];
    return i[Dc](function (n, e) {
      i[e] = t(n)
    }), i[Hc]("")
  }

  function he(t, i) {
    try {
      if (null == t || t[rh] < 8) return;
      if (null == i || i[rh] <= 0) return;
      for (var n = "", e = 0; e < i[rh]; e++) n += i[Yc](e).toString();
      var s = Math[Gh](n.length / 5),
        r = parseInt(n[Uc](s) + n[Uc](2 * s) + n[Uc](3 * s) + n[Uc](4 * s) + n[Uc](5 * s), 10), h = Math[Bf](i[rh] / 2),
        a = Math.pow(2, 31) - 1, o = parseInt(t[ga](t[rh] - 8, t[rh]), 16);
      for (t = t[ga](0, t[rh] - 8), n += o; n[rh] > 10;) n = (parseInt(n[ga](0, 10), 10) + parseInt(n[ga](10, n[rh]), 10)).toString();
      n = (r * n + h) % a;
      for (var f = "", c = "", e = 0; e < t[rh]; e += 2) f = parseInt(parseInt(t[ga](e, e + 2), 16) ^ Math.floor(n / a * 255), 10), c += String[Wc](f), n = (r * n + h) % a;
      return 0 | c[0] ? YW = xW[qc + mW + Xc](c) : null
    } catch (u) {
    }
  }

  function ae() {
    var t = vW;
    if (!t) return void (ZW = !0);
    HW = t;
    var i;
    t = t.split(Yh);
    for (var n = 0; n < t[rh] && (i = he(t[n], yW), !(i && i[jh](xo).length >= 8));) 1 == t[rh] && (i = he(t[n], Vc)), n++;
    if (!i || i[jh](xo)[rh] < 8) return qW = !0, "" === yW || yW == Zc + MW + Kc + OW + If || yW == Jc + kW + Qc ? (XW = QW, ZW = !1, void (zW = !1)) : (XW = QW, void (ZW = !0));
    zW = i.split(xo);
    var e = zW[3];
    if (e != tV) return void (qW = !0);
    ZW = !1;
    var s = zW[0];
    s > 1 && (qW = !1);
    var r = zW[5];
    VW = r;
    var h = zW[6];
    XW = h
  }

  function oe() {
    var t = HW;
    if (t) {
      var i;
      t = t[jh](Yh);
      for (var n = 0; n < t[rh] && (i = tq(t[n], yW), !(i && i.split(xo)[rh] >= 8));) 1 == t[rh] && (i = tq(t[n], Vc)), n++;
      if (i[jh](xo)[rh] >= 8) return void (KW = !1)
    }
    return yW && yW != Zc + MW + Kc + OW + If && yW != Jc + kW + Qc ? void (KW = !0) : void (KW = !1)
  }

  function fe() {
    if (qW) {
      var t = pr[PW + Fo]._jm, i = WW;
      pr[PW + Fo]._jm = function () {
        t[bh](this, arguments), i[ah](this._naaseCanvas, this.g)
      };
      var n = lq[PW + Fo]._gp;
      lq[PW + Fo]._gp = function (t) {
        n[bh](this, arguments), i.call(this, t)
      }
    }
  }

  function ce() {
    if (VW !== !0 && VW) {
      var t = VW[jh](Kh);
      if (3 != t.length) return void (Iq.prototype._jm = null);
      var i = parseInt(t[0], 10), n = parseInt(t[1], 10), e = parseInt(t[2], 10), s = 3,
        r = (365.2425 * (i - 2e3 + 10 * s) + (n - 1) * s * 10 + e) * s * 8 * s * 1200 * 1e3;
      bW > r && (Iq.prototype._jm = null)
    }
  }

  function ue() {
    var t = 0 | XW;
    if (t) {
      var i = DY[yh]._ki;
      DY[PW + Fo]._ki = function () {
        return this._je[rh] > t ? !1 : i[bh](this, arguments)
      }
    }
  }

  function _e() {
    ZW && (sY[PW + Fo]._ki = sY[PW + Fo]._f9)
  }

  function de() {
    if (KW) {
      var t = WW, i = lq[PW + Fo]._gp;
      lq[PW + Fo]._gp = function (n) {
        i[bh](this, arguments), t[ah](this, n)
      }
    }
  }

  function le() {
    if (JW) {
      var t = pr[PW + Fo]._jm, i = WW;
      pr[PW + Fo]._jm = function () {
        t.apply(this, arguments), i[ah](this[tu], this.g)
      }
    }
  }

  function ve() {
    zW === n && (lq[PW + Fo]._iz = uY.equals)
  }

  function be(t) {
    var i = Ri(!0);
    return ee(i.g), i[iu] = function () {
      return !1
    }, t[nu](i), i[eu] = rq, i
  }

  function d(t, i) {
    function n(t, n) {
      for (var e = t[rh], s = n[rh], r = e + s, h = new Array(r), a = 0, o = 0, f = 0; r > f;) h[f++] = a === e ? n[o++] : o === s || i(t[a], n[o]) <= 0 ? t[a++] : n[o++];
      return h
    }

    function e(t) {
      var i = t[rh], s = Math[uh](i / 2);
      return 1 >= i ? t : n(e(t[ch](0, s)), e(t.slice(s)))
    }

    return e(t)
  }

  function ye(t) {
    t[ha] = t[ha]
  }

  function ge(t) {
    uq || (uq = "imageSmoothingEnabled" in CanvasRenderingContext2D[yh] ? "imageSmoothingEnabled" : "mozImageSmoothingEnabled" in CanvasRenderingContext2D[yh] ? "mozImageSmoothingEnabled" : "msImageSmoothingEnabled" in CanvasRenderingContext2D[yh] ? "msImageSmoothingEnabled" : "webkitImageSmoothingEnabled" in CanvasRenderingContext2D[yh] ? "webkitImageSmoothingEnabled" : "imageSmoothingEnabled"), t[uq] = !1
  }

  function xe(t, i, n, e, s) {
    e = X(i + e) - (i = q(i)), s = X(n + s) - (n = q(n)), t.clearRect(i, n, e, s), t[hf](i, n, e, s)
  }

  function q(t) {
    return Math[Gh](t)
  }

  function X(t) {
    return Math[uh](t)
  }

  function pe(t) {
    var i = [];
    return t[Dc](function (t) {
      i[Io](-t)
    }), i
  }

  function Ee(t) {
    return t %= bq, 0 > t && (t += bq), t
  }

  function me(t, i, n, e, s, r, h, a) {
    var o = ((t * e - i * n) * (s - h) - (t - n) * (s * a - r * h)) / ((t - n) * (r - a) - (i - e) * (s - h)),
      f = ((t * e - i * n) * (r - a) - (i - e) * (s * a - r * h)) / ((t - n) * (r - a) - (i - e) * (s - h));
    if (isNaN(o) || isNaN(f)) return !1;
    if (t >= n) {
      if (!(o >= n && t >= o)) return !1
    } else if (!(o >= t && n >= o)) return !1;
    if (i >= e) {
      if (!(f >= e && i >= f)) return !1
    } else if (!(f >= i && e >= f)) return !1;
    if (s >= h) {
      if (!(o >= h && s >= o)) return !1
    } else if (!(o >= s && h >= o)) return !1;
    if (r >= a) {
      if (!(f >= a && r >= f)) return !1
    } else if (!(f >= r && a >= f)) return !1;
    return !0
  }

  function we(t, i) {
    for (var n = 0, e = t[rh]; e > n;) {
      for (var s = t[n], r = t[(n + 1) % e], h = 0; 4 > h;) {
        var a = i[h], o = i[(h + 1) % e];
        if (me(s[0], s[1], r[0], r[1], a[0], a[1], o[0], o[1])) return !0;
        h++
      }
      n++
    }
    return !1
  }

  function Te(t, i, n, e) {
    return [t * e - i * n, t * n + i * e]
  }

  function ke(t) {
    return t[su] ? (t = t.parent, t._dh ? t._dh : t instanceof Oq && t._h9 === !1 ? t : null) : null
  }

  function Me(t, i, n) {
    if (n = n || i[ru], n == t) return !1;
    var e = t[hu](n);
    return e || (e = new HX(t, n), t[au][n.id] = e), e._il(i, t)
  }

  function Oe(t, i, n) {
    if (n = n || i[ru], n == t) return !1;
    var e = t.getEdgeBundle(n);
    return e ? e._n9w(i, t) : void 0
  }

  function Ie(t, i, e) {
    return e === n && (e = i[ru]), e != t ? (t._7x || (t._7x = new sY), t._7x.add(i) === !1 ? !1 : void t._99++) : void 0
  }

  function Se(t, i, n) {
    return t._7x && t._7x[_h](i) !== !1 ? (t._99--, void Oe(t, i, n)) : !1
  }

  function Ae(t, i) {
    return i[ou] != t ? (t._95 || (t._95 = new sY), t._95.add(i) === !1 ? !1 : void t[fu]++) : void 0
  }

  function je(t, i) {
    return t._95 && t._95[_h](i) !== !1 ? (t[fu]--, void Oe(i[ou], i, t)) : !1
  }

  function Pe(t, i) {
    if (i === n && (i = t instanceof wq), i) {
      if (t[cu]()) return null;
      var e = Pe(t[uu], !1);
      if (t[_u]()) return e;
      for (var s = Pe(t.to, !1); null != e && null != s;) {
        if (e == s) return e;
        if (e.isDescendantOf(s)) return s;
        if (s.isDescendantOf(e)) return e;
        e = Pe(e, !1), s = Pe(s, !1)
      }
      return null
    }
    for (var r = t[su]; null != r;) {
      if (r._hi()) return r;
      r = r.parent
    }
    return null
  }

  function Ce(t, i, n) {
    t._hi() && t[eh]() && t[hh][Dc](function (t) {
      t instanceof Tq && i.add(t) && Ce(t, i, n)
    }, this), t.hasFollowers() && t._du[Dc](function (t) {
      (null == n || n[du](t)) && i.add(t) && Ce(t, i, n)
    })
  }

  function Le(t, i) {
    i[su] ? i[su][lu](i, i.parent[vu] - 1) : t[bu][yu](i, t[bu][rh] - 1)
  }

  function De(t, i) {
    i.parent ? i[su].setChildIndex(i, 0) : t[bu].setIndex(i, 0)
  }

  function Re(t, i) {
    if (i instanceof wq) return void (i[cu]() || Be(t, i));
    for (Le(t, i); i = i.parent;) Le(t, i)
  }

  function Ne(t, i) {
    if (i instanceof wq) return void (i.isInvalid() || $e(t, i));
    for (De(t, i); i = i[su];) De(t, i)
  }

  function Be(t, i) {
    var n = i[ou];
    if (i[_u]()) Le(t, n); else {
      var e = i.toAgent;
      Le(t, n), Le(t, e)
    }
  }

  function $e(t, i) {
    var n = i.fromAgent;
    if (i[_u]()) De(t, n); else {
      var e = i[ru];
      De(t, n), De(t, e)
    }
  }

  function Fe(t, i) {
    return t._99++, t._fb ? (i._hl = t._hr, t._hr._hj = i, void (t._hr = i)) : (t._fb = i, void (t._hr = i))
  }

  function Ge(t, i) {
    t._99--, t._hr == i && (t._hr = i._hl), i._hl ? i._hl._hj = i._hj : t._fb = i._hj, i._hj && (i._hj._hl = i._hl), i._hl = null, i._hj = null, Oe(t, i, i.$to)
  }

  function ze(t, i) {
    return t._nc3++, t._i8 ? (i._j3 = t._jh, t._jh._j6 = i, void (t._jh = i)) : (t._i8 = i, void (t._jh = i))
  }

  function He(t, i) {
    t._nc3--, t._jh == i && (t._jh = i._j3), i._j3 ? i._j3._j6 = i._j6 : t._i8 = i._j6, i._j6 && (i._j6._j3 = i._j3), i._j3 = null, i._j6 = null
  }

  function Ye(t, i) {
    return i = i || new sY, t[gu](function (t) {
      i.add({id: t.id, edge: t, fromAgent: t[xu]._dh, toAgent: t.$to._dh})
    }), t[pu](function (t) {
      t instanceof Tq && Ye(t, i)
    }), i
  }

  function Ue(t, i, n) {
    return qe(t, i, n) === !1 ? !1 : We(t, i, n)
  }

  function We(t, i, n) {
    if (t._fb) for (var e = t._fb; e;) {
      if (i.call(n, e) === !1) return !1;
      e = e._hj
    }
  }

  function qe(t, i, n) {
    if (t._i8) for (var e = t._i8; e;) {
      if (i[ah](n, e) === !1) return !1;
      e = e._j6
    }
  }

  function Xe(t, i, e, s, r, h, a) {
    return h || a ? (h = h || 0, a = a === n ? h : a || 0, h = Math.min(h, s / 2), a = Math.min(a, r / 2), t.moveTo(i + h, e), t[ff](i + s - h, e), t.quadTo(i + s, e, i + s, e + a), t[ff](i + s, e + r - a), t[Eu](i + s, e + r, i + s - h, e + r), t.lineTo(i + h, e + r), t.quadTo(i, e + r, i, e + r - a), t[ff](i, e + a), t.quadTo(i, e, i + h, e), t[zf](), t) : (t.moveTo(i, e), t[ff](i + s, e), t.lineTo(i + s, e + r), t[ff](i, e + r), t[zf](), t)
  }

  function Ve(t, i) {
    var n = i.r || 1, e = i.cx || 0, s = i.cy || 0, r = n * Math.tan(Math.PI / 8), h = n * Math.sin(Math.PI / 4);
    t.moveTo(e + n, s), t[Eu](e + n, s + r, e + h, s + h), t.quadTo(e + r, s + n, e, s + n), t[Eu](e - r, s + n, e - h, s + h), t[Eu](e - n, s + r, e - n, s), t.quadTo(e - n, s - r, e - h, s - h), t[Eu](e - r, s - n, e, s - n), t[Eu](e + r, s - n, e + h, s - h), t[Eu](e + n, s - r, e + n, s)
  }

  function Ze(t, i, n, e, s) {
    i instanceof hn && (e = i.width, s = i.height, n = i.cy - s / 2, i = i.cx - e / 2);
    var r = .5522848, h = e / 2 * r, a = s / 2 * r, o = i + e, f = n + s, c = i + e / 2, u = n + s / 2;
    return t[of](i, u), t[mu](i, u - a, c - h, n, c, n), t.curveTo(c + h, n, o, u - a, o, u), t[mu](o, u + a, c + h, f, c, f), t[mu](c - h, f, i, u + a, i, u), t
  }

  function Ke(t, i, n, e, s) {
    var r = 2 * e, h = 2 * s, a = i + e / 2, o = n + s / 2;
    return t.moveTo(a - r / 4, o - h / 12), t[ff](i + .306 * e, n + .579 * s), t[ff](a - r / 6, o + h / 4), t[ff](i + e / 2, n + .733 * s), t[ff](a + r / 6, o + h / 4), t[ff](i + .693 * e, n + .579 * s), t.lineTo(a + r / 4, o - h / 12), t[ff](i + .611 * e, n + .332 * s), t[ff](a + 0, o - h / 4), t[ff](i + .388 * e, n + .332 * s), t.closePath(), t
  }

  function Je(t, i, n, e, s) {
    return t[of](i, n), t[ff](i + e, n + s / 2), t.lineTo(i, n + s), t[zf](), t
  }

  function Qe(t, i, n, e, s) {
    return t[of](i, n + s / 2), t[ff](i + e / 2, n), t.lineTo(i + e, n + s / 2), t[ff](i + e / 2, n + s), t[zf](), t
  }

  function ts(t, i, n, e, s, r) {
    return t.moveTo(i, n), t.lineTo(i + e, n + s / 2), t[ff](i, n + s), r || (t.lineTo(i + .25 * e, n + s / 2), t[zf]()), t
  }

  function is(t, i, n, e, s) {
    if (!t || 3 > t) throw new Error("edge number must greater than 2");
    t = 0 | t, e = e || 50, s = s || 0, i = i || 0, n = n || 0;
    for (var r, h, a = 0, o = 2 * Math.PI / t, f = new iW; t > a;) r = i + e * Math.cos(s), h = n + e * Math.sin(s), a ? f.lineTo(r, h) : f[of](r, h), ++a, s += o;
    return f[zf](), f
  }

  function ns() {
    var t = new iW;
    return t[of](75, 40), t[mu](75, 37, 70, 25, 50, 25), t[mu](20, 25, 20, 62.5, 20, 62.5), t.curveTo(20, 80, 40, 102, 75, 120), t[mu](110, 102, 130, 80, 130, 62.5), t.curveTo(130, 62.5, 130, 25, 100, 25), t.curveTo(85, 25, 75, 37, 75, 40), t
  }

  function es() {
    var t = new iW;
    return t[of](20, 0), t[ff](80, 0), t[ff](100, 100), t[ff](0, 100), t[zf](), t
  }

  function ss() {
    var t = new iW;
    return t[of](100, 0), t[ff](100, 80), t[ff](0, 100), t[ff](0, 20), t[zf](), t
  }

  function rs() {
    var t = new iW;
    return t[of](20, 0), t[ff](100, 0), t[ff](80, 100), t.lineTo(0, 100), t[zf](), t
  }

  function hs() {
    var t = new iW;
    return t[of](43, 23), t.lineTo(28, 10), t.lineTo(37, 2), t[ff](63, 31), t[ff](37, 59), t.lineTo(28, 52), t[ff](44, 38), t[ff](3, 38), t[ff](3, 23), t[zf](), t
  }

  function as() {
    var t = new iW;
    return t[of](1, 8), t[ff](7, 2), t[ff](32, 26), t.lineTo(7, 50), t[ff](1, 44), t.lineTo(18, 26), t[zf](), t[of](27, 8), t[ff](33, 2), t[ff](57, 26), t[ff](33, 50), t[ff](27, 44), t[ff](44, 26), t[zf](), t
  }

  function os() {
    var t = new iW;
    return t[of](0, 15), t.lineTo(23, 15), t[ff](23, 1), t.lineTo(47, 23), t.lineTo(23, 43), t[ff](23, 29), t[ff](0, 29), t.closePath(), t
  }

  function fs() {
    var t = new iW;
    return t[of](0, 21), t.lineTo(30, 21), t[ff](19, 0), t.lineTo(25, 0), t[ff](47, 25), t.lineTo(25, 48), t[ff](19, 48), t[ff](30, 28), t[ff](0, 28), t[zf](), t
  }

  function cs() {
    var t = new iW;
    return t[of](0, 0), t[ff](34, 24), t[ff](0, 48), t[ff](14, 24), t[zf](), t
  }

  function us() {
    var t = new iW;
    return t[of](20, 0), t[ff](34, 14), t[ff](20, 28), t.lineTo(22, 18), t[ff](1, 25), t.lineTo(10, 14), t.lineTo(1, 3), t[ff](22, 10), t[zf](), t
  }

  function _s() {
    var t = new iW;
    return t[of](4, 18), t.lineTo(45, 18), t[ff](37, 4), t[ff](83, 25), t[ff](37, 46), t.lineTo(45, 32), t[ff](4, 32), t[zf](), t
  }

  function ds() {
    var t = new iW;
    return t.moveTo(17, 11), t[ff](27, 11), t[ff](42, 27), t.lineTo(27, 42), t[ff](17, 42), t[ff](28, 30), t[ff](4, 30), t[ff](4, 23), t[ff](28, 23), t.closePath(), t
  }

  function ls() {
    Mq[wu](gU[Tu], Ze(new iW, 0, 0, 100, 100)), Mq[wu](gU[ku], Xe(new iW, 0, 0, 100, 100)), Mq[wu](gU[Mu], Xe(new iW, 0, 0, 100, 100, 20, 20)), Mq.register(gU[Ou], Ke(new iW, 0, 0, 100, 100)), Mq[wu](gU[Iu], Je(new iW, 0, 0, 100, 100)), Mq[wu](gU.SHAPE_PENTAGON, is(5)), Mq[wu](gU[Su], is(6)), Mq[wu](gU[Au], Qe(new iW, 0, 0, 100, 100)), Mq.register(gU[ju], ns()), Mq[wu](gU[Pu], es()), Mq.register(gU[Cu], ss()), Mq.register(gU[Lu], rs());
    var t = new iW;
    t[of](20, 0), t[ff](40, 0), t[ff](40, 20), t.lineTo(60, 20), t.lineTo(60, 40), t.lineTo(40, 40), t[ff](40, 60), t.lineTo(20, 60), t[ff](20, 40), t.lineTo(0, 40), t[ff](0, 20), t[ff](20, 20), t[zf](), Mq[wu](gU[Du], t), Mq[wu](gU[Ru], ts(new iW, 0, 0, 100, 100)), Mq[wu](gU.SHAPE_ARROW_1, hs()), Mq[wu](gU[Nu], as()), Mq[wu](gU[Bu], os()), Mq[wu](gU[$u], fs()), Mq.register(gU[Fu], cs()), Mq[wu](gU[Gu], us()), Mq.register(gU[zu], _s()), Mq[wu](gU.SHAPE_ARROW_8, ds()), Mq.register(gU[Hu], ts(new iW, 0, 0, 100, 100, !0))
  }

  function vs() {
    w(this, vs, arguments), this[Yu] = !0
  }

  function bs() {
    w(this, bs), this._$n = new SY
  }

  function ys() {
    if (this._h9 === !0) {
      var t = this._7x, i = this._95;
      if (t) for (t = t._je; t[rh];) {
        var n = t[0];
        Se(this, n, n[ru])
      }
      if (i) for (i = i._je; i.length;) {
        var n = i[0];
        je(this, n, n.fromAgent)
      }
      return void this[pu](function (t) {
        t._hi() && ys[ah](t)
      })
    }
    var e = Ye(this);
    e[Dc](function (t) {
      t = t[Uu];
      var i = t[xu], n = t.$to, e = i.isDescendantOf(this), s = n.isDescendantOf(this);
      e && !s ? (Ie(this, t), Me(this, t)) : s && !e && (Ae(this, t), Me(t[ou], t, this))
    }, this)
  }

  function gs() {
    w(this, gs, arguments), this.$image = null
  }

  function xs(t, i, n, e) {
    return t[i] = n, e ? {
      get: function () {
        return this[i]
      }, set: function (t) {
        if (t !== this[i]) {
          this[i] = t, !this._ncc, this._18 = !0;
          for (var n = e[rh]; --n >= 0;) this[e[n]] = !0
        }
      }
    } : {
      get: function () {
        return this[i]
      }, set: function (t) {
        t !== this[i] && (this[i] = t)
      }
    }
  }

  function ps(t, i) {
    var n = {}, e = {};
    for (var s in i) {
      var r = i[s];
      r[Wu] && r[Wu].forEach(function (t, i, n) {
        n[i] = qu + t, e[t] = !0
      }), n[s] = xs(t, wh + s, r[Xu], r.validateFlags)
    }
    for (var h in e) t[qu + h] = !0;
    Object[Zh](t, n)
  }

  function Es(t, i, n, e) {
    if (Array[Vu](i)) for (var s = i[rh]; --s >= 0;) Es(t, i[s], n, e); else {
      var r = i[Zu];
      if (r) {
        if (r instanceof Iq || (r = t[r]), !r) return
      } else r = t;
      if (e || (e = t.getProperty(i[Ku], n)), i[Ju] && (r[i[Ju]] = e), i[Qu]) {
        var h = i.callback;
        h instanceof Function || (h = t[h]), h instanceof Function && h.call(t, e, r)
      }
    }
  }

  function ms() {
    Sq.forEach(function (t) {
      this[t] = {}
    }, this)
  }

  function ws(t, i, n, e) {
    return e == gU[t_] ? void (t[n] = i) : e == gU[i_] ? void t.set(n, i) : e == gU[n_] ? void t[e_](n, i) : !1
  }

  function Ts() {
    w(this, Ts, arguments)
  }

  function ks() {
    w(this, ks, arguments)
  }

  function Ms(t, i, n, e, s, r) {
    var h = Os(t, i, n, e), a = [];
    return js(t) ? Is(h, i, n, a, e[s_](Aq[r_]), s, r) : Gs(t, i, n, a, h, e, s, r), a
  }

  function Os(t, i, n) {
    if (null != t) {
      if (t == gU.EDGE_TYPE_ELBOW_HORIZONTAL || t == gU[h_] || t == gU[a_] || t == gU[o_] || t == gU[f_]) return !0;
      if (t == gU.EDGE_TYPE_ELBOW_VERTICAL || t == gU[c_] || t == gU[u_] || t == gU.EDGE_TYPE_EXTEND_TOP || t == gU.EDGE_TYPE_EXTEND_BOTTOM) return !1
    }
    var e = Ls(i, n), s = Ds(i, n);
    return e >= s
  }

  function Is(t, i, n, e, s) {
    t ? Ws(i, n, e, s) : qs(i, n, e, s)
  }

  function Ss(t, i) {
    return i[s_](Aq[__])
  }

  function As(t) {
    return null != t && (t == gU[d_] || t == gU[o_] || t == gU[l_] || t == gU[f_])
  }

  function js(t) {
    return t && (t == gU[v_] || t == gU.EDGE_TYPE_ELBOW_HORIZONTAL || t == gU.EDGE_TYPE_ELBOW_VERTICAL)
  }

  function Ps(t, i, n, e, s, r, h) {
    if (t == gU[a_]) return {x: h.x, y: r.y};
    if (t == gU[u_]) return {x: r.x, y: h.y};
    var a;
    if (As(t)) {
      var o = Math.min(n.y, e.y), f = Math.min(n.x, e.x), c = Math.max(n[ea], e[ea]), u = Math.max(n.right, e.right);
      if (a = s[s_](Aq.EDGE_EXTEND), t == gU[d_]) return new aY((f + u) / 2, o - a);
      if (t == gU[o_]) return new aY(f - a, (o + c) / 2);
      if (t == gU[l_]) return new aY((f + u) / 2, c + a);
      if (t == gU.EDGE_TYPE_EXTEND_RIGHT) return new aY(u + a, (o + c) / 2)
    }
    var _ = Ss(t, s);
    if (a = _ ? Rs(t, i, n, e, s[s_](Aq.EDGE_SPLIT_PERCENT)) : s[s_](Aq.EDGE_SPLIT_VALUE), a == Number.NEGATIVE_INFINITY || a == Number[b_]) return new aY(e.x + e.width / 2, e.y + e.height / 2);
    if (0 == a) return new aY(n.x + n[ha] / 2, n.y + n[Ja] / 2);
    if (i) {
      var d = n.x + n[na] < e.x + e[na];
      return new aY($s(d, a, n.x, n[ha]), n.y + n[Ja] / 2)
    }
    var l = n.y + n[ea] < e.y + e[ea];
    return new aY(n.x + n[ha] / 2, $s(l, a, n.y, n.height))
  }

  function Cs(t, i, n, e) {
    var s = Math.max(i, e) - Math.min(t, n);
    return s - (i - t + e - n)
  }

  function Ls(t, i) {
    var n = Math.max(t.x + (t.width || 0), i.x + (i[ha] || 0)) - Math.min(t.x, i.x);
    return n - (t[ha] || 0) - (i[ha] || 0)
  }

  function Ds(t, i) {
    var n = Math.max(t.y + (t[Ja] || 0), i.y + (i.height || 0)) - Math.min(t.y, i.y);
    return n - (t[Ja] || 0) - (i[Ja] || 0)
  }

  function Rs(t, i, n, e, s) {
    var r = Ns(s, i, n, e, null);
    return r * s
  }

  function Ns(t, i, n, e) {
    return i ? Bs(t, n.x, n.right, e.x, e[na]) : Bs(t, n.y, n[ea], e.y, e[ea])
  }

  function Bs(t, i, n, e, s) {
    var r = Cs(i, n, e, s), h = e + s > i + n;
    if (r > 0) {
      if (1 == t) return r + (s - e) / 2;
      if (t >= 0 && 1 > t) return r;
      if (0 > t) return h ? e - i : n - s
    }
    return Math.abs(h && t > 0 || !h && 0 > t ? n - s : i - e)
  }

  function $s(t, i, n, e) {
    return t == i > 0 ? n + e + Math.abs(i) : n - Math.abs(i)
  }

  function Fs(t, i) {
    var n = t[rh];
    if (!(3 > n)) {
      var e = i[s_](Aq.EDGE_CORNER);
      if (e != gU.EDGE_CORNER_NONE) {
        var s = i[s_](Aq.EDGE_CORNER_RADIUS), r = 0, h = 0;
        s && (R(s) ? r = h = s : (r = s.x || 0, h = s.y || 0));
        for (var a, o, f, c, u = t[0], _ = t[1], d = null, l = 2; n > l; l++) {
          var v = t[l], b = _.x - u.x, y = _.y - u.y, p = v.x - _.x, E = v.y - _.y, m = !b || b > -TU && TU > b,
            w = !y || y > -TU && TU > y, T = !p || p > -TU && TU > p, k = !E || E > -TU && TU > E, M = w;
          (m && k || w && T) && (M ? (a = Math.min(2 == l ? Math.abs(b) : Math.abs(b) / 2, r), o = Math.min(l == n - 1 ? Math.abs(E) : Math.abs(E) / 2, h), f = new aY(_.x - (b > 0 ? a : -a), _.y), c = new aY(_.x, _.y + (E > 0 ? o : -o))) : (a = Math.min(l == n - 1 ? Math.abs(p) : Math.abs(p) / 2, r), o = Math.min(2 == l ? Math.abs(y) : Math.abs(y) / 2, h), f = new aY(_.x, _.y - (y > 0 ? o : -o)), c = new aY(_.x + (p > 0 ? a : -a), _.y)), x(t, _), l--, n--, (f.x != u.x || f.y != u.y) && (g(t, f, l), l++, n++), e == gU.EDGE_CORNER_BEVEL ? (g(t, c, l), l++, n++) : e == gU[y_] && (g(t, [_, c], l), l++, n++)), u = _, _ = v
        }
        null != d && c.x == _.x && c.y == _.y && x(t, _)
      }
    }
  }

  function Gs(t, i, n, e, s, r, h, a) {
    var o = r.getStyle(Aq[g_]), f = null == o;
    if (null != o) {
      var c = (new uY)[x_](i)[x_](n);
      c[p_](o) || (s = zs(o.x, o.y, c.y, c.x, c[ea], c.right))
    } else o = Ps(t, s, i, n, r, h, a);
    s ? Us(i, n, o, e, f, h, a) : Ys(i, n, o, e, f, h, a)
  }

  function zs(t, i, n, e, s, r) {
    return n > i && n - i > e - t && n - i > t - r || i > s && i - s > e - t && i - s > t - r ? !1 : !0
  }

  function Hs(t, i, n) {
    return i >= t.x && i <= t[na] && n >= t.y && n <= t[ea]
  }

  function Ys(t, i, n, e, s, r, h) {
    var a = Math.max(t.y, i.y), o = Math.min(t.y + t.height, i.y + i[Ja]), f = null != n ? n.y : o + (a - o) / 2,
      c = r ? r.x : t.x + t[ha] / 2, u = h ? h.x : i.x + i[ha] / 2;
    if (0 == s && null != n && (n.x >= t.x && n.x <= t.x + t[ha] && (c = n.x), n.x >= i.x && n.x <= i.x + i[ha] && (u = n.x)), Hs(i, c, f) || Hs(t, c, f) || e[Io](new aY(c, f)), Hs(i, u, f) || Hs(t, u, f) || e[Io](new aY(u, f)), 0 == e.length) if (null != n) Hs(i, n.x, f) || Hs(t, n.x, f) || e.push(new aY(n.x, f)); else {
      var _ = Math.max(t.x, i.x), d = Math.min(t.x + t[ha], i.x + i.width);
      e[Io](new aY(_ + (d - _) / 2, f))
    }
  }

  function Us(t, i, n, e, s, r, h) {
    var a = Math.max(t.x, i.x), o = Math.min(t.x + t.width, i.x + i.width), f = null != n ? n.x : o + (a - o) / 2,
      c = r ? r.y : t.y + t[Ja] / 2, u = h ? h.y : i.y + i[Ja] / 2;
    if (0 == s && null != n && (n.y >= t.y && n.y <= t.y + t.height && (c = n.y), n.y >= i.y && n.y <= i.y + i.height && (u = n.y)), Hs(i, f, c) || Hs(t, f, c) || e[Io](new aY(f, c)), Hs(i, f, u) || Hs(t, f, u) || e.push(new aY(f, u)), 0 == e.length) if (null != n) Hs(i, f, n.y) || Hs(t, f, n.y) || e[Io](new aY(f, n.y)); else {
      var _ = Math.max(t.y, i.y), d = Math.min(t.y + t[Ja], i.y + i.height);
      e[Io](new aY(f, _ + (d - _) / 2))
    }
  }

  function Ws(t, i, n, e) {
    var s = i.x + i.width < t.x, r = t.x + t[ha] < i.x, h = s ? t.x : t.x + t.width, a = t.y + t[Ja] / 2,
      o = r ? i.x : i.x + i[ha], f = i.y + i.height / 2, c = e, u = s ? -c : c, _ = new aY(h + u, a);
    u = r ? -c : c;
    var d = new aY(o + u, f);
    if (s == r) {
      var l = s ? Math.min(h, o) - e : Math.max(h, o) + e;
      n[Io](new aY(l, a)), n[Io](new aY(l, f))
    } else if (_.x < d.x == s) {
      var v = a + (f - a) / 2;
      n.push(_), n[Io](new aY(_.x, v)), n[Io](new aY(d.x, v)), n[Io](d)
    } else n[Io](_), n.push(d)
  }

  function qs(t, i, n, e) {
    var s = i.y + i[Ja] < t.y, r = t.y + t[Ja] < i.y, h = t.x + t.width / 2, a = s ? t.y : t.y + t[Ja],
      o = i.x + i.width / 2, f = r ? i.y : i.y + i.height, c = e, u = s ? -c : c, _ = new aY(h, a + u);
    u = r ? -c : c;
    var d = new aY(o, f + u);
    if (s == r) {
      var l = s ? Math.min(a, f) - e : Math.max(a, f) + e;
      n[Io](new aY(h, l)), n.push(new aY(o, l))
    } else if (_.y < d.y == s) {
      var v = h + (o - h) / 2;
      n.push(_), n[Io](new aY(v, _.y)), n.push(new aY(v, d.y)), n[Io](d)
    } else n.push(_), n.push(d)
  }

  function Xs(t) {
    return t == gU[E_] || t == gU.EDGE_TYPE_ORTHOGONAL_HORIZONTAL || t == gU[a_] || t == gU[c_] || t == gU.EDGE_TYPE_VERTICAL_HORIZONTAL || t == gU.EDGE_TYPE_EXTEND_TOP || t == gU[o_] || t == gU[l_] || t == gU[f_] || t == gU.EDGE_TYPE_ELBOW || t == gU.EDGE_TYPE_ELBOW_HORIZONTAL || t == gU[m_]
  }

  function Vs(t, i) {
    var n, e;
    i && i.width && i[Ja] ? (n = i[ha], e = i[Ja]) : n = e = isNaN(i) ? eY[w_] : i;
    var s = Mq.getShape(t, -n, -e / 2, n, e);
    return s || (s = new iW, s[of](-n, -e / 2), s[ff](0, 0), s[ff](-n, e / 2)), s
  }

  function Zs(t, i) {
    var n = Math.sin(i), e = Math.cos(i), s = t.x, r = t.y;
    return t.x = s * e - r * n, t.y = s * n + r * e, t
  }

  function Ks(t, i, n, e, s, r) {
    var h = Math[ta](e - i, n - t), a = new aY(s, r);
    return a.rotate = h, Zs(a, h), a.x += t, a.y += i, a
  }

  function Js(t, i, e, s, r, h) {
    i = si(s, i.x, i.y, e.x, e.y), e = si(r, e.x, e.y, i.x, i.y);
    var a = Math.PI / 2 + Math[ta](e.y - i.y, e.x - i.x), o = t * Math.cos(a), f = t * Math.sin(a);
    i.x += o, i.y += f, e.x += o, e.y += f;
    var c = e.x - i.x, u = e.y - i.y;
    if (h == gU[T_]) {
      var _ = Math[To](c * c + u * u), d = 5;
      return _ > 2 * d && (a = Math.atan2(e.y - i.y, e.x - i.x), c = d * Math.cos(a), u = d * Math.sin(a), i.x += c, i.y += u, e.x -= c, e.y -= u), [new QU(XU, [i.x, i.y]), new QU(XU, [e.x, e.y])]
    }
    return [new QU(ZU, [i.x + .25 * c, i.y + .25 * u, i.x + .75 * c, i.y + .75 * u, n, n])]
  }

  function Qs(t, i, e) {
    if (g(t, new QU(qU, [i.x, i.y]), 0), e) {
      if (t.length > 1) {
        var s = t[t.length - 1];
        if (VU == s[Fo] && (s[Sf] || s[$a][2] === n || null === s[$a][2])) return s[$a][2] = e.x, s.points[3] = e.y, void (s[Sf] = !0);
        if (ZU == s.type && (s.invalidTerminal || s[$a][4] === n || null === s[$a][4])) return s[$a][4] = e.x, s.points[5] = e.y, void (s.invalidTerminal = !0)
      }
      t.push(new QU(XU, [e.x, e.y]))
    }
  }

  function tr(t, i, n) {
    var e = i[k_](t.getStyle(Aq.EDGE_FROM_PORT), n), s = t.getStyle(Aq[M_]);
    return s && (e.x += s.x || 0, e.y += s.y || 0), e
  }

  function ir(t, i, n) {
    var e = i[k_](t[s_](Aq[O_]), n), s = t.getStyle(Aq[I_]);
    return s && (e.x += s.x || 0, e.y += s.y || 0), e
  }

  function nr(t, i, n, e, s) {
    var r = e == s, h = t[S_][A_](e), a = r ? h : t[S_][A_](s);
    if (h && a) {
      var o = i[j_], f = t[P_](h), c = r ? f : t[P_](a), u = i[C_]();
      if (r && !u) return t.drawLoopedEdge(t[L_], h, o, f);
      var _ = tr(t, h, f), d = ir(t, a, c);
      if (!r && !o && !u) {
        var l = e[Yu], v = s[Yu];
        if (l != v) {
          var b, y, g, x, p = i[D_];
          l ? (b = h, y = f, g = a, x = c) : (b = a, y = c, g = h, x = f);
          var E = or(y, b, l, g, x, p);
          if (E && 2 == E.length) {
            var m = E[0], w = E[1];
            return n.moveTo(m.x, m.y), w.x == m.x && w.y == m.y && (w.y += .01), n[ff](w.x, w.y), void (n._63 = !0)
          }
        }
      }
      t._3f(i, n, h, a, o, f, c, _, d), (!r || u) && er(t, i, n, h, a, o, f, c, _, d), n._63 = !0
    }
  }

  function er(t, i, e, s, r, h, a, o, f, c) {
    var u = t.fromAtEdge, _ = t[R_];
    if (!u && !_) return void Qs(e._f6, f, c);
    var d = e._f6;
    if (d[rh]) {
      if (u) {
        var l = d[0], v = l[N_];
        sr(s, a, v, f, n, n)
      }
      if (_) {
        var b, y = d[d.length - 1], g = y[$o], x = y[$a][rh], p = y[Sf] || g.x === n || g.y === n;
        x >= 4 && (p || o.contains(g.x, g.y)) && (p || (c = g), b = !0, g = {
          x: y[$a][x - 4],
          y: y[$a][x - 3]
        }, o.contains(g.x, g.y) && (c = g, x >= 6 ? (g = {
          x: y[$a][x - 6],
          y: y[$a][x - 5]
        }, y[$a] = y[$a][ch](0, 4), y[Fo] = VU) : 1 == d[rh] ? (g = {
          x: f.x,
          y: f.y
        }, y[$a] = y[$a][ch](0, 2), y[Fo] = XU) : (y = d[d.length - 2], g = y[$o]))), sr(r, o, g, c, n, n), b && (x = y.points.length, y.points[x - 2] = c.x, y[$a][x - 1] = c.y, c = null)
      }
    } else {
      var E = Math.atan2(c.y - f.y, c.x - f.x), m = Math.cos(E), w = Math.sin(E);
      u && sr(s, a, c, f, m, w), _ && sr(r, o, f, c, -m, -w)
    }
    Qs(e._f6, f, c)
  }

  function sr(t, i, e, s, r, h) {
    if (r === n) {
      var a = Math.atan2(e.y - s.y, e.x - s.x);
      r = Math.cos(a), h = Math.sin(a)
    }
    for (e = {x: e.x, y: e.y}, i[B_](e.x, e.y) || (e = si(i, s.x, s.y, e.x, e.y)); ;) {
      if (!i.contains(e.x, e.y)) return s;
      if (t[$_](e.x - r, e.y - h, eY[F_])) {
        s.x = e.x - r / 2, s.y = e.y - h / 2;
        break
      }
      e.x -= r, e.y -= h
    }
    return s
  }

  function rr(t, i, n, e, s, r, h, a) {
    if (i[C_]()) return i._8s;
    var o = i[j_];
    if (Xs(o)) {
      var f = Ms(o, n, e, t, h, a);
      if (!f || !f.length) return null;
      g(f, h, 0), f[Io](a), o != gU[v_] && Fs(f, t);
      for (var c = [], u = f.length, _ = 1; u - 1 > _; _++) {
        var d = f[_];
        c[Io]($(d) ? new QU(VU, [d[0].x, d[0].y, d[1].x, d[1].y]) : new QU(XU, [d.x, d.y]))
      }
      return c
    }
    if (i[G_]) {
      var l = t._1v();
      if (!l) return;
      return Js(l, h, a, n, e, t[s_](Aq[z_]))
    }
  }

  function hr(t, i, n) {
    var e = t[s_](Aq[H_]), s = t._1v(), r = e + .2 * s, h = i.x + i[ha] - r, a = i.y, o = i.x + i[ha], f = i.y + r;
    e += s;
    var c = .707, u = -.707, _ = i.x + i.width, d = i.y, l = _ + c * e, v = d + u * e, b = {x: h, y: a},
      y = {x: l, y: v}, g = {x: o, y: f}, x = b.x, p = y.x, E = g.x, m = b.y, w = y.y, T = g.y,
      k = ((T - m) * (w * w - m * m + p * p - x * x) + (w - m) * (m * m - T * T + x * x - E * E)) / (2 * (p - x) * (T - m) - 2 * (E - x) * (w - m)),
      M = ((E - x) * (p * p - x * x + w * w - m * m) + (p - x) * (x * x - E * E + m * m - T * T)) / (2 * (w - m) * (E - x) - 2 * (T - m) * (p - x)),
      r = Math[To]((x - k) * (x - k) + (m - M) * (m - M)), O = Math[ta](b.y - M, b.x - k),
      I = Math[ta](g.y - M, g.x - k), S = I - O;
    return 0 > S && (S += 2 * Math.PI), ar(k, M, O, S, r, r, !0, n)
  }

  function ar(t, i, n, e, s, r, h, a) {
    var o, f, c, u, _, d, l, v, b, y, g;
    if (Math.abs(e) > 2 * Math.PI && (e = 2 * Math.PI), _ = Math[uh](Math.abs(e) / (Math.PI / 4)), o = e / _, f = o, c = n, _ > 0) {
      d = t + Math.cos(c) * s, l = i + Math.sin(c) * r, moveTo ? a[of](d, l) : a[ff](d, l);
      for (var x = 0; _ > x; x++) c += f, u = c - f / 2, v = t + Math.cos(c) * s, b = i + Math.sin(c) * r, y = t + Math.cos(u) * (s / Math.cos(f / 2)), g = i + Math.sin(u) * (r / Math.cos(f / 2)), a[Eu](y, g, v, b)
    }
  }

  function or(t, i, n, e, s, r) {
    var h = s.cx, a = s.cy, o = Math.cos(r), f = Math.sin(r), c = cr(i, t, {x: h, y: a}, -o, -f);
    if (!c) {
      var u = h < t.x, _ = h > t[na], d = a < t.y, l = a > t[ea], v = t.cx, b = t.cy, y = u || _, g = d || l;
      r = Math[ta](a - b, h - v), y || g || (r += Math.PI), o = Math.cos(r), f = Math.sin(r), c = cr(i, t, {
        x: h,
        y: a
      }, -o, -f) || {x: v, y: b}
    }
    var x = cr(e, s, {x: c.x, y: c.y}, -c.perX || o, -c[Y_] || f, !1) || {x: h, y: a};
    return n ? [c, x] : [x, c]
  }

  function fr(t, i, n, e, s, r) {
    var h = i < t.x, a = i > t[na], o = n < t.y, f = n > t[ea];
    if (h && e > 0) {
      var c = t.x - i, u = n + c * s / e;
      if (u >= t.y && u <= t.bottom) return {x: t.x, y: u, perX: e, perY: s}
    }
    if (a && 0 > e) {
      var c = t.right - i, u = n + c * s / e;
      if (u >= t.y && u <= t[ea]) return {x: t.right, y: u, perX: e, perY: s}
    }
    if (o && s > 0) {
      var _ = t.y - n, d = i + _ * e / s;
      if (d >= t.x && d <= t.right) return {x: d, y: t.y, perX: e, perY: s}
    }
    if (f && 0 > s) {
      var _ = t[ea] - n, d = i + _ * e / s;
      if (d >= t.x && d <= t[na]) return {x: d, y: t[ea], perX: e, perY: s}
    }
    return r !== !1 ? fr(t, i, n, -e, -s, !1) : void 0
  }

  function cr(t, i, n, e, s, r) {
    if (!i.contains(n.x, n.y)) {
      if (n = fr(i, n.x, n.y, e, s, r), !n) return;
      return ur(t, i, n, n.perX, n[Y_])
    }
    return r === !1 ? ur(t, i, n, e, s) : ur(t, i, {x: n.x, y: n.y, perX: e, perY: s}, e, s) || ur(t, i, n, -e, -s)
  }

  function ur(t, i, n, e, s) {
    for (; ;) {
      if (!i[B_](n.x, n.y)) return;
      if (t[$_](n.x + e, n.y + s)) break;
      n.x += e, n.y += s
    }
    return n
  }

  function _r(t) {
    return On(t) ? t : t[U_](/.(gif|jpg|jpeg|png)$/gi) || /^data:image\/(\w+\+?\w+);base64,/i.test(t) ? t : (t = J(t), t instanceof Object && t[pf] ? t : void 0)
  }

  function dr(t) {
    for (var i = t[su]; i;) {
      if (i.enableSubNetwork) return i;
      i = i.parent
    }
    return null
  }

  function lr() {
    w(this, lr, arguments)
  }

  function vr(t, n, e, s, r, h, a) {
    var o = i[oo](W_);
    o[eu] = q_, bi(o, Xq), n && bi(o, n);
    var f = i.createElement(X_);
    return h && (iY && (f[V_] = h), WY || (f[Z_] = h)), f[kh] = a, f.src = e, bi(f, Vq), r && bi(f, r), s && gi(f, K_, J_), o._img = f, o[nu](f), t[nu](o), o
  }

  function br(t, n) {
    this[Q_] = i.createElement(W_), this[Q_].className = td, bi(this[Q_], {
      "background-color": id,
      overflow: nd,
      "user-select": ed,
      position: sd
    }), this[rd] = vr(this[Q_], {width: hd}, eY[ad], !1, null, n, od), this._left = vr(this[Q_], {height: hd}, eY.NAVIGATION_IMAGE_LEFT, !1, Zq, n, Ca), this[fd] = vr(this[Q_], {
      height: hd,
      right: cd
    }, eY.NAVIGATION_IMAGE_LEFT, !0, Zq, n, na), this[ud] = vr(this[Q_], {
      width: hd,
      bottom: cd
    }, eY.NAVIGATION_IMAGE_TOP, !0, null, n, ea), t.appendChild(this[Q_])
  }

  function yr(t, i) {
    if (!eY.NAVIGATION_IMAGE_LEFT) {
      var n = Ri(20, 40), e = n.g;
      e[ef](e.ratio, e.ratio), e.moveTo(16, 4), e[ff](4, 20), e[ff](16, 36), e[No] = 3, e[_d] = Bf, e[dd] = Bf, e[Ko] = uf, e[ld] = vd, e[bd] = 5, e.stroke(), eY[yd] = n[gd]();
      var s = Ri(n.height, n.width, !1);
      s.g[sf](s[ha], 0), s.g.rotate(Math.PI / 2), s.g[xd](n, 0, 0), eY[ad] = s.toDataURL()
    }
    this._naaseCanvas = t;
    var r = function (i) {
      z(i);
      var n, e, s = i.target, r = s[kh];
      if (Ca == r) n = 1; else if (na == r) n = -1; else if (od == r) e = 1; else {
        if (ea != r) return;
        e = -1
      }
      iY && (s.className = pd, setTimeout(function () {
        s[eu] = ""
      }, 100)), z(i), t._kp._8v(n, e)
    };
    br.call(this, i, r), this._3a(i[wa], i[Ed])
  }

  function gr(t, i) {
    this[tu] = t, this[md](i, t)
  }

  function xr() {
    w(this, xr, arguments)
  }

  function pr(t, i) {
    this[tu] = t, this._jf = be(i), this.g = this._jf.g, this._9b = new sY
  }

  function Er(t) {
    var i = t[wd], n = [];
    return t.graphModel.forEach(function (i) {
      t.isVisible(i) && t[Td](i) && n[Io](i)
    }), i.set(n)
  }

  function mr(t, i, n, e) {
    var s = t[Ao];
    n = n || s, i = i || 1;
    var r = null;
    r && n[ha] * n[Ja] * i * i > r && (i = Math.sqrt(r / n.width / n[Ja]));
    var h = Ri();
    ee(h.g), h[ha] = n.width * i, h[Ja] = n.height * i, t._8f._gp(h.g, i, n);
    var a = null;
    try {
      a = h[gd](e || kd)
    } catch (o) {
      yU[wf](o)
    }
    return {canvas: h, data: a, width: h[ha], height: h[Ja]}
  }

  function wr(t) {
    this.graph = t, this[Md] = t[Md]
  }

  function Tr(t, i) {
    this[Od] = t, this[Id] = i || Sd
  }

  function kr() {
    w(this, kr, arguments)
  }

  function Mr(t, i) {
    if (!t) return i;
    var e = {};
    for (var s in t) e[s] = t[s];
    for (var s in i) e[s] === n && (e[s] = i[s]);
    return e
  }

  function Or() {
    w(this, Or, arguments)
  }

  function Ir(t, i, n, e) {
    var s;
    return t[Ad](function (r) {
      var h = r[Ro];
      return h instanceof yU.Node && (!e || e(h) !== !1) && r[jd].intersectsPoint(i - r.x, n - r.y) && r.hitTest(i, n, eY[Pd] / t.scale) ? (s = h, !1) : void 0
    }), s
  }

  function Sr(t) {
    this[hf] = t[vf](), this.points = t[Cd](), this[Ld] = t[Dd]()
  }

  function Ar() {
    w(this, Ar, arguments)
  }

  function jr() {
    w(this, jr, arguments)
  }

  function Pr() {
    w(this, Pr, arguments)
  }

  function Cr(i, n, e) {
    i += t[Sa], n += t[ja];
    var s = e[Ta]();
    return {x: i + s[Ca], y: n + s.top}
  }

  function Lr(t, i, n) {
    var e = t[Rd], s = t[Nd];
    t.style[Ca] = i - e / 2 + no, t.style.top = n - s / 2 + no
  }

  function Dr(t) {
    var n = i.createElement(fo), e = n[bf](co), s = getComputedStyle(t, null), r = s[Bd];
    r || (r = s.fontStyle + Ph + s[$d] + Ph + s[Fd]), e.font = r;
    var h = t.value, a = h[jh](xo), o = parseInt(s[$d]), f = 0, c = 0;
    return yU.forEach(a, function (t) {
      var i = e[mo](t).width;
      i > f && (f = i), c += 1.2 * o
    }), {width: f, height: c}
  }

  function Rr(t, n) {
    if (Lh == typeof t[Gd] && Lh == typeof t[zd]) {
      var e = t[Xu], s = t[Gd];
      t[Xu] = e.slice(0, s) + n + e.slice(t[zd]), t.selectionEnd = t[Gd] = s + n[rh]
    } else if (Hd != typeof i[Yd]) {
      var r = i[Yd].createRange();
      r[Ud] = n, r[Wd](!1), r[qd]()
    }
  }

  function Nr(i) {
    if (HH) {
      var n = t[Xd] || t[Sa], e = t[Vd] || t.pageYOffset;
      return i.select(), void t[Zd](n, e)
    }
    i.select()
  }

  function Br() {
  }

  function $r() {
    w(this, $r, arguments), this.handlerSize = iY ? 8 : 5
  }

  function Fr(t) {
    return t[Fo] == VU || t[Fo] == ZU
  }

  function Gr() {
    w(this, Gr, arguments), this[Kd] = iY ? 8 : 4, this._rotateHandleLength = iY ? 30 : 20
  }

  function zr(t, i) {
    var n = new uY;
    return n[to](Zn[ah](t, {x: i.x, y: i.y})), n[to](Zn[ah](t, {
      x: i.x + i[ha],
      y: i.y
    })), n[to](Zn[ah](t, {x: i.x + i[ha], y: i.y + i[Ja]})), n[to](Zn[ah](t, {x: i.x, y: i.y + i.height})), n
  }

  function Hr(t) {
    t %= 2 * Math.PI;
    var i = Math[Bf](t / Qq);
    return 0 == i || 4 == i ? "ew-resize" : 1 == i || 5 == i ? "nwse-resize" : 2 == i || 6 == i ? "ns-resize" : Jd
  }

  function Yr() {
  }

  function Ur(n, e, s) {
    var r = i[Qd], h = new uY(t[Sa], t[ja], r.clientWidth - 2, r[Ed] - 2), a = n[Rd], o = n[Nd];
    e + a > h.x + h[ha] && (e = h.x + h[ha] - a), s + o > h.y + h.height && (s = h.y + h[Ja] - o), e < h.x && (e = h.x), s < h.y && (s = h.y), n[va][Ca] = e + no, n[va].top = s + no
  }

  function Wr(t) {
    this[tl] = t, this[il] = function (t) {
      return this._kl && (this[nl] ? (cancelAnimationFrame(this._kl), this[nl] = null) : clearTimeout(this._kl), this._kl = null), t === !0 || t === !1 ? void this[tl]() : t ? void (this._kl = setTimeout(function () {
        this[tl](), this._kl = null
      }[el](this), t)) : (this._isFrameTimer = !0, void (this._kl = requestAnimationFrame(function () {
        this[tl](), this._kl = null, this._isFrameTimer = null
      }.bind(this))))
    }
  }

  function qr(t, i, n, e, s) {
    this[mf] = t, this.type = sl, this[rl] = i, this[hl] = n, this[Ro] = e, this[al] = s
  }

  function Xr(t) {
    this._4l = {}, this._kp = t, this._kp._1d[ol](this._90, this), this[fl] = gU[cl]
  }

  function Vr(t) {
    return t >= 100 && 200 > t
  }

  function Zr(t) {
    return t == vX || t == wX || t == mX || t == gX || t == MX || t == OX
  }

  function Kr() {
    var t, i, n = {}, e = [], s = 0, r = {}, h = 0;
    this[S_][Dc](function (a) {
      if (this.isLayoutable(a)) if (a instanceof Tq) {
        var o = {node: a, id: a.id, x: a.x, y: a.y};
        for (this[ul] && this[ul](a, o), n[a.id] = o, e[Io](o), s++, i = a[su]; i instanceof Oq;) {
          t || (t = {});
          var f = t[i.id];
          f || (f = t[i.id] = {id: i.id, children: []}), f[hh].push(o), i = i[su]
        }
      } else if (a instanceof wq && !a[_u]() && a[ou] && a[ru]) {
        var o = {edge: a};
        r[a.id] = o, h++
      }
    }, this);
    var a = {};
    for (var o in r) {
      var f = r[o], c = f[Uu], u = c[ou], _ = c.toAgent, d = u.id + da + _.id, l = _.id + da + u.id;
      if (n[u.id] && n[_.id] && !a[d] && !a[l]) {
        var v = n[u.id], b = n[_.id];
        f[uu] = v, f.to = b, a[d] = f, this[_l] && this[_l](c, f)
      } else delete r[o], h--
    }
    return {
      groups: t,
      nodesArray: e,
      nodes: n,
      nodeCount: s,
      edges: r,
      edgeCount: h,
      minEnergy: this.minEnergyFunction(s, h)
    }
  }

  function Jr(t) {
    this[S_] = t, this.currentMovingNodes = {}
  }

  function Qr() {
    w(this, Qr, arguments)
  }

  function th(t, i, n, e, s) {
    e ? t.forEachEdge(function (e) {
      var r = e[dl](t);
      r != n && r[ll] != s && i(r, t)
    }, this, !0) : t[vl](function (e) {
      var r = e.toAgent;
      r != n && r[ll] != s && i(r, t)
    })
  }

  var ih = "1b3d12c1f332adb3e393a5484d72c471285d5ddaf7ebe46747bfe665b6c9f818d18869991425f15dc790ffa879112a66c56948a7f81b79e6fbb1c4d7fb447ebbf21e84d5d59c054165501fca5dc35823584cd1e2678442785c06cd93e765b1abe68f8ec5597c8d8bef1f2bc768767aa94ea5d29d02704076ce42885bcb09256523de0a27f39264ee50ca5a7499b4359adc5a8c7563d6fdcaa3c8202466a12f8c4c0e6bcec3cc9e0ca7c356d24e94ec067f9390d2cb99e5ed902101127b1efd0487e83232a24d3e38df9c02bf700fc54876b9e8533fe233a1fb381247b82de356e4cd4fb5f59275cfcd22b88efe9ede49a73d3bcfdfa24019f433663e01510d7714c88414dedea649d2cb2bf91ed4a30eb7cf3e85bd52d142303e9448136a0a5b5d76418eaae2b8e321df1b232ea7ffe001c5ef1c4b626a183391d4c918c75abeddd0f2e086d33d736aa8c0793a06e4c99618a2ba20a72e104c81fc04b5d8c7be49581b734d852195cb7043afce512c51d64600b83ee003a171dd1d7aa1c8804874ca7d55d8c5f73a2b3ef9e7925231f13e1a04e76fe0f277e3edeeecea0980b6b65393966cce1c94793ae79a573deef31fef8b07a9c751e2a76a4029a69055cdbc1182b1c3ca8f3f1edcbd96dbb026f8a5d6a4414d0505ff5787bfe8ad9bcdb4b4d6be64f042f32e7b1da074e55004950328529ed68c5e7df057d328ff802ca50aaaf5c3ceb39c4f9dea2a0a90e0bb62ef64bef635d230ff4583c7748ee70110eaa875bfc5a9129c73197e3ae25c6ab9f77d585ce7779e5cf7be1e43d4ecb9a2de88cf3c0099a18b67b19c190e5a9b5a115f183a888648c4b28411da4ac4ec56c53465aae4fd2513c94efdd59c19acc193fcbeb815c84a349a742128bf5cd19fed64087d1f7f8ac87850cc5989da51f0c1ec662c4e4ecd65d79bbd0e50d6dfa498da64f3a7f311dba9038c850b336dddff2a8daa040ff6c682816833dfa37a23a33b49e74c53d850eb67a1820f8749f1fa892434b5a424ed84db893a2dc6bffcb03e8c14c881d12896383ac4383598e21ef7ef27870471c4d213cfdf50da1c6a9d11f128dfe56d9c768466c38c338296b06f77491e469d453cefed7e6676bff39cf350bce8fd850f0faf9d59f27487a378223e13d0b228573ef0e6026fe12392dbadc6af260c64e82c8d82387f2eed735abbc019fc2206890b33e8a55292a3ebab3dafb9edffee00b55f49f5e30c07c3da8797fec5f647dbfc543261ca625b443710d0461ea9204c36cbce8f0d41ef1d8ffa27b6438094d0f2396ae81a44a690014837c9e6a2c5b467c037ce41920f407a7c26d8d2e133a31b85810e53c2d5df69e15655fcd71866131e77efbef48bbff68622f54b1438fef4e83633b9c5022b4c47fd7c37265cf6ca38805a6ea2bb2438bfd47a4e8273c02f4817f73d4d0ef5b5be2bb7054600620279763d12252179d16ca18f3f79371d2dadf659db1886fea68161e6ff21cd925a51b53a6325f8e6482565686ea0ac6bd1617727a18691523ba0d981e79039fc260de69a7d8a2b3c5426292c4e18747f9b57aa2616a1a9eb20aacf8bc866db11a031dba840dcf674561e47e0e1f9fc4854913669f7b1f0138a1cb36ab943c7b5d93961040f01e22e99b207325d154c41f93ad7319495cbe3313e6bccee7ea7d54b05f85e502dc805ac9aac6cd9ae90b2debba5fb8aa89cc6e62917db32286d0889a75830eec18cb364d32238127cd9290956ef9f024f207c2b0d42222c0c3020beb1ab298db7130b70da33e318e6b4f10ce1a59e5463bde2424a883ebc985eb58506d98b9dcd802396c8d838be1d0da31ff846dd282febdf1fa7ec7f81a61826e648ed4bf03574ddbd062bb7b4e2c3ff163d5b22918d3fc6d0de0d47287a99a66ecbbcd03870ea07f47d0ebc3e2c28e06b85de06a6f8d7c447c4a3df6583122dea1f7cefd578dee5a6c3b4e49d020d547bf794221e39013bb27cbc4ca7358e81c80fd418f9cd9b815f4eb148006b6b1f7295261f781aef9fa0ee67ef4fcc4b0bc1dc1d6b528b03c1f8d4a6e4d722ca0f750b71f8afbc5211b5b8473d4486b6f7d57568960b3b50a5dbc7e466322efb8a271485a235884f44b00f72d4d19be84a081729a9469bd4d70efa37aa8d297e3132c693bf28c814a2f1ad1d3e85231524c6e60b80609b389f39d4539597096b3d62323bedb6746fc723a2747af6db6ac57bb169be0798a82b266316e1fcb7c9a3e9a3f7ea4b02613dfa78d12655821da7e3dcf34fe4f2ab104f0664e0119c13996851b37b67c7bb33d0d410faf3643b589ec51bd15ccf3dea8720e61e3b47ddf1dfb2aba4b961b00958548e5cdbd315d42ae33a13930d72a0e987be7ae01402b125114a502176c2d095306d2db99686c3e779a73f9c04d1f011b14b2a46a773eb5c7de1dcec6abc276dd63c75e496efe8c8b951d06580f66724638b1e8707e875bc6188d584ee7247e5e59a5773492adcbcdb1479ee23f24a69eaabac2567f9a4b5e7d2fc9b0e08ae2c0e08514eb89eef6bae095df974a3a73936f4766ad4274a172e3143672338b1a67f1fdec67f76db6dc066838cc85da27cf668195807b4748fefa591c5813c5609c075eeb4ada3c909e8c7cd6e1b497026b94969ef29d95587038e4deaffeb57505453444d69bff6a1d9c019896328a0db10c1b8fc9af52ae079b1d04b7005ec3c4c0d293c753eeafddfec5ca45bed93debc657138e2659e43158e578e999cab80b8f9c25fb21574e42a1fc840fff436179ca956202982429602eb188c1a5e05245d8ab048e9d4856ab024b2aac76242de518f2c300f4c3bb0aa71346793ed762ad8fee76a96de055fccddb9432ac80d74569f995dc7d1519a710dccb5184cfbb066def5aecc3700972cee51c4fb7d52f729216e874de08fea502c69c3d6a2cc1af543b899560ab8c46db287926ec9faec369609cdd672620cac8e47094b2c36f9d70dd697fd83689762d195ddb94182d15544005653c3fd2601a2b6103c82f1daa16051d13398953861049fa286a7e7f70604c1a48f39b8d886725302b93d384b3377414f7f6230c1c206a2f4e5ec08175ebbced2aef28c662c7795b0a2ac4d50ce8bd58306b4e0e94c2723ac7f8166d1f61249ac2a7835208cf0967374d45c275cb2318c7cd2b2c77bc48c262e365b214af6ca6c731c84b616da5c956c8b2a6d9c3760d06dd5608b1ad1ebf7672c3c7ef6eb6ed473b14bd89368ea6f55469f7e6adc686e359025db3b14418e7ca15dc3fabe069689783864e72cf79a34d0edbc1a5617d1ffec6aed40620401121a2a1d82f3996fc5610d7498a3faacb06fe6890435ee2ec5e9b3a01f05aeba401045aeb0d68184cbbeeb29f3330e2f830db617b48ba8a1a48310da95398764ddb50b138dad2b944d4575d2ea02c1e8442a9bf28e49dc46036d61ff5bf739668c7dca70ae5ee886934985ac40eb4317c372c039e8d5ed787cf78db93c7550e82e2994d2c7b07ee6b7e414dc568e882c0066c5fc7ea3a95d3060c9e7865a0d4aa01ac1d8ae04d6673b2f92281d29d087b9eb38f6e4caae84027354ce2815634c9234da38c61ca939c5a72727a63acaa0e5f1a5cb0242baa514980efb81b64d5d63ff32c3abfd7c97750b8d0b0a995feeed0ac2acbf0c2dcf6bf7815062493b6bc6a40d031e793981dab736a866f875212ea20f565a737cdf96a75fe005330a065449601def140919a07c70983fd1880b7431af9f2c420b641b49ae8baf6dfa42296290a992fe29282de820348ff9ca340828a3b80cbc5dcfe85dbf5aefdc715e23bbec3f661f16470cd306b0fdb85c016e2260217889c9291e6d7c735ae84beefbce4d20c61e964c212e264985d9a12a35496a0cd1326f87b752c66be03e02b5cc70669359e67e05f1f67dabaa5a2dd720d72185ce73dd48ffe3962461d8f8925864f77302410db2fb6189b69f108ae892142b17b09bf083de231f39b2834ebabb2b7f0179ac6d717be8304ab380e1e18c54193f3a0f8b697b094cad502792cf06004a414faf496768104bc4f1e1dc40f26e1f628f63f91da707de83f42cedb6df869d26878394f43710769a13c66a3713b06e380550269d0a0cd0e091c349b67143aa10933edc0a1958c2ae543dd7973c2cc84dd68dc8b61beca7cd67782073dbb014e58b674f9ea078cc2c48c7beb2025dca7473b9d1340569fc3b3b400ca2888b03689c8a829e871d391af579db651de1843a38b5b474d631ab01d78107d1c6a53a286b63c7065c5efe0005a4752bc9d908500470ab7b8fdfe8421a12bfd161c44aa0692dfde11a568f332e3d8a57ea02dbcac64b3f479324cc4aba11fe8b39024b8214e304989567750c3fbd969d974b49e3edaebfaeb37f09f2d54779cebe417fe20f88d6bfd9575b45639fff5846f4d7c124ba8a3eac94c463474b4dc8cefe9022e6fa228a3bbeeaec0b1c5d4c6cb07274a572bfb9646f57e278261b21c0294050f8fe6ae7bb003645e688e0eb125f954f88f0f444b1d2431dae1d541e7c74978d81e72453cd363907253960722b69c935b0a82d1d7e780b94c8670299ad97e3f596cd98d2ed782d7fefa179bbc1ccb0d4ff5a93ce27d3cc222cde7d0bfad9b73dbffc1f32de10ca3523be000329298abf9cb1ef2fd51cf747b3bc505261dab26eb7e7817ee990cb9092c6cde973a0a98fe793d13a60170fa8d5c15bda11c829f302ebc828a86f7f3d540f833642d0e2dcede1fb461bff4000d3bacfe6c9e3970b710a1b62c7be5f6998a4273a9956cdbb4a08ee1ef494c4d2c05fc7b72bc45169b32e75fdc203b5a81852fd7aa18295e28f63ced757726b36c35216855226ef8b914d442633c93b955fe169c75503b1f66b0f78b890e2daee360d89d4b37ff04bf75d95902fdcd334ddc58165bea309298ec278f6c9ab6f36345861105d31c667813b0ed3b86d3235ef3d9705b4d1d3a9f6418cd6149c805f1b313e12c6e032c441263a99d9d26221145fb919db14262bec01ca123a4242d068cb27adc65766ad29df68c4f7fdedf1671635ba3491eda6dcb95bf7ae429282e0b1f1ac8acc4c20e470ebb30bd5a281ca963425f30ff77b13fa231cf032c0626f852f12ea0d191111fae80fcbb1ee553b398464c66442a08b3fdbf07977fcb133e7c0145f47e4677fca9765222a708c21ad2be8c7e7926050ea5633514622bce6a7426cda00268e0db55ce50f1f4e4f8977ef976b08e3186585b3ea63c048781e52af9169f2a8c067211b42d74b07a1ed6188e22b63bdff6c606b3655409dd4812f7ae42e46b431fd18d69df780bdc1075a9e19fc47d508e0a517b4d918f6981c91c99be17b972f76a6f68eddbb5470929cbe6bcfd63a9b106bb778de4dbd17cf60a6d54aeda95ce1b3a8c00f28e4a7db55f6cc2843408916c3e5cd81d1c60c4243972db46f4c62dbb41fa9c9bcc629ff009dd741914032a42ed4fca30df6b78283cdcf7289fe47b4417ab7e63eeeac8f2e0274e6329115b8c552ccd3d24759fa408cbc4efb9324c77ac2a79a26a4ed54eb1f5f9a982d1484c172a27c3a5277b183878938a15214963d87b7d2d0698eea6c62f708a6a2eaf8249803b639d6bed5f0e767a7f1dccae207329fd1b8e896a080a03a10791cbf1248cbdc52da79b18409512835902cc885760fb02157808fc000dc172daa28b2643436f5aed4cadb207a22d3c2e6d3a88aa2ff483f75dcef477a123c66790798351c52ae53c67734d2a1e6588e15dadab52c014a12fc8849e19274b2b827235f475f00f204903afd3750f80e111443d729fcf7482b3aa4f096e9ee1f53c5cbc7d5f34145b69b51292dba079ab4f087edf8ccb253093cdbacc86d6d080b11e9d2e6ec2e9b3433625933487708074e0d1c9d08f68e77df2ac3fd51ef3ad9c4042c69ff9e4c6de8212be5257521cf01aca7b017c2ae50aa9964684257afbdb74677330f8cf9583faffc3f5fa9923eab6a480fc58fa61575f8f80024cab600c85c500cb8d7bf810082afbbdb0dd9066b80879880700692119c0dbe78e3e80eb43201f38a6a8621d2776e77d6538aa27941016a8d69d3524d71fe5b2d404757bcc1e671141efca9a46c9760eef3f3c7ceae94a4e047b9301da88cc92e40733887a919687409aa791d0795abd9e65b0ce8023385cad5dbd093f74eb97efe63305ac2102267cfa5c51b76c5b5616b05052d0111036dd7a3fe6ef6271c713c33bac3c20d8ddafa6cf9677b0ec6b0e6fd3dc443572fcafb0b11d30e815c6a9fafbe28bee59ee207484d2daedf994eeca2e4cc2f9d25f7ddff354c82582225a838e23d79e9261b16dd2733dc06c493e40b56a90b8d2e8d019302362e5c1bee1cbd7be6050a7f8377fdeb3a2a305015aa0350564424334c766d1ee02d5b12eb67e50578a01790b1fd6914e676286c2b01030529127251ca5921a76a6ecd44ef43460bffb8de0f480bebb4da92f7058bd16ad92e1aa413c33aba51bab04be34ddc7fdbc44599d17a0fb8d3d96aef4a429741ecd1836e245bf12992e3b32692387248a53f726bbfff2527070259677e9776ad1376eae9353169930ab3822a695cfb8f2d0d8ca39bd818abb2a5455af2939e6530afc5970551878c2ac9aeaccfb1d3d87c4b41fb2519e3ac3eedd43e030afb86ea31ccefecd30bdeca435c5cdde3c0ec39ac2794deb8024b424c344c8454fe8a67e11b0ef8256ec6b72dbbf32c30628f8e6dfdd1a2848f52a1e68aff337ef7a6feeadbdec6c22b641a300eeb3fa0fe5c9f826075d9e74414c4ec25399554c4eda94f0590c443c076fdbe07cc249b72537bdfa1f88e8ae463556afba311d2509702d1ccf7c8ffc0877123e505fb1ffd512cde3648487b94ef39abe419a9a415c3f7424be17290548cc5f7efeb74eec054c8e45816c8eb400e587deec31c807fe6a92bb1e2d38800bc66916af4cd43af0582749c1b728aaa9d104c772e4c6090d03c9767b23cc7ca7a844afd9df2267608a4b568d5c6b374a9fad022cd31379e54f49b6416f5efd5830feb12eb8f5ad8a1262e4ded3f301cebba2ac138d118c6ae485271855eb7d99e2b9bdb1c7b73a349bc02711094614235419e1f2c7a360b693eddc81ced42c07179fc9cbadb356b109a3b5a11f36b881b2ad95d2b76b4400171182428ec280f97b012ceb9446f5353d5bed7c77965ac7324289bdbf606eb1e3fdd10211683b3c94f5add79c9412e2adfa47a3fe9120ce76862288d33aec467fad3e48802a22fb6418e25178f7855d9ec521ed52633a8fd771163aed6146d9a2a60d9fa55eb01a6e33190068a734f7c9b7cf931d6638e2e95314011aae7ae2857e3166af454c944ec1d83adfca23c66a6f2c90723eaa5b19229bdfb2073ee232ce1408ce3b7774a12df3e5c18c7e165b69c3dc7a3b536be37f73bc1792ab99220c59ca27992b2914594fced8858914400f5132d0322e0831f95ba9d18fcef3883fc716a1b1644d71464344938e8ce69333a9be7d51589bacefe8213b6f1fd51a50ec7afcaebdeb994802a3fd99b38867d18441fa25bb2f79f450c4e88d0a95627bf2052acff8b5bf63afa83e3f100a4a810a3ab77b9a4b73c95f8fcb57c4097d52e7acdb8890b429ffdb7bffc8e6029cbf873a3b233fd7a7a1f5887838a29d02384574f01c6c38cf6982766e5390890c11fc6c97ee7d1f8d7764abdef151aa9a75e6b84eb7f7ebc335c529f7cfed65370d371e2ecd6bd4011fa9344a19ae630eb14f292d016145862740e2dc424de6a4e0c476ab86d39e8847138447c33e5b3bf213cf59c5d356807890d32192d12896fa5b2331b404a82fbf08eb94e437a42ef002bde417d69be508e24d2cdd7a95d215dddb2c8145d1604a50b5cab2ab3b09d325d93e9daca80e033e837ebcb5f25ae9528eb860e3e055b33810a3c463102f69acc47f8c4b85fdeebfb9bd4056367517600bf76b213a5a8b58590aab98e3fab8ccb01e4d4ee112e88060d46c2842f9e72a900f31409a711de6ff6cad3446a2cacf341c26093b02c326b25457b92333f47636979eb269249c69e60302758c3e6366675589eff69729ec0f5a1f12f0822724e9286c495aeb150314f4562c83dfd1f9179e22c0d6f51fe2fa531478abf19c389d24470df47aa1fe235e91084d4ad9eae1462796bc5a5f05784477b1c9e8ac258de7cb5208ddf5a6674de286cda074c934d8b11034122c6f7773a2ca607192a10777e433039734e62c000f00c7984409f7ea8ceeeca9f01b21f322433ca064db575401ad73e35534714592aba6506525c153b2da752e7eafda42a71f20258908d392cd88725894daf749ce6ef29748ac6c213a8a029f8f44ea3f8b71fb043ba7c3aab9fc214acfcd692f360b978c8adfaeabeb62c2353ac755ff39634857b621fdea0f00ff81b1b91ba4804f949779b40ef00dc2725731aba92e5f33851779108fbae87ae3771fad0a05629013581d02d4406225fadd4e63b193f1c8a4e68dc2c631f2540d7cc01faa2c0f676c4686c6bf8a2d612ca33bae47775efb11c9d59f0b8846f35b09bd1cc892cad95702e7a1c4a6892890beb9b8ebf72d13f8c3e2cb001f0d75b15674690a16884d033d947f1ccf5c902b23e4bd86724999ff0de1fcfabd80eb02d3af057049ec5b754aaf1a3e9573ffdcaab62a96e3b010cc83024587e0732844de4cf9ab1dc4e8881d1fe22afe04ebaaff9b42c65aa6a3949785736c8d14f92755b0a0f5ea0c7659452348426effcfdc9be97c8d0770c339ef0d81afe5df9e5e85576ff9a614f1181521a91fd6a3f71b667e1386d12bce013ceb63f603351e6a6926a7191eccda55cd67d2bf17a1b11f2e008d4ac709ca37fd67e61ff5cbef1cb4197d8e8c985f7f7ec6f518b8bfb5a32894915f1371788dc6c4482909af7ebebcf04556a7620d61ac0daa862fe13076deb8bdf6330eefcc864f9964acba90e7cad320f281cb2e303667c12585231051c3b1e59afae8bb0ff51d541ff33e0d11f5c171d3748f3700516f9024bf9085b5b4f4c77d6a60fb06ad8137ca5e985d62da8b5bf8ff0a1744dfb5f132d20d5ac576164068f2c6322710ffd12b7944db576275e4851262702d442e29cb35e2630a4cd1fe439b347315a0466a36a80529d9da82810aac56d1f92d09186d7ded6bc1f1fb4e3eda2903e5ed67c19697154221153da3874515e930218fa49e5ef57b6eabfb587b1b0358910104d02a2657cb9ec7aa9d617370a80149ba9b945455ce53b8590365888d27187377c77af16ac868be26a973a00b398626bb6619729b42079fb939ec6dd763e48269cfed24c7a00f15f32592c21c05c8616094cd4571a8a1cfe9bb51e2ca8f62da995d03ce85a73ae965bde626179efd7d4e41ea10dba372e854c1aa1008dfd51d4baffaf8c073a00d9dc942b10a0a58f254a05d13909a6c4afd93a4a49c8af6378a481c722f92c395f8f884b89d6dcf1cf5ef3ea80ee57bf72e6c150dabbd6441c7bf4a64d67b5d2d05dbb89c489d72eff96c09f9dedbd4b88213d8773aab12b8ca48b246eded66014c5c9efc63cb25c60ddb8c163cb71e9ced0774db5c5833745c8a9772de9e8f8b20420ab4c529d20c4ed932ec6665c651d45ebf84d9aadf0c49cbfd1d18ae1507fd6afa225cd5c59b15064c7f923867cc096a473dcbe34319f6e8bec40e9852b7155ae0b7b1176288be444c2617e04eea1a1da030e3b2951ab6994758005074077334c0504fc36ba08a3c6a243bbe3a9c9b02511d6afdc3e64a3d1d58619a5b9a455bb455ce49d0245369e714fc33d5efda20e638cb153ce0c277cf56c0a3d6ef64fb5d67eaa5ea0dfa3f45f40c1b6dff2d491f60ff3f6c3915d2acad816f7ce176dcc5177b66e76c3e01c8c5dcca0c5c9cae207bbca7f32b821cab35721248ba099daccaa45df84edbda855c04d2624a61290d42f9c6ce103a2f90a0e8816639a7f954223714457018930ebe8388133f3a0126fc7a99dc239cd9ec7285d8b1004c3bb07de00d83ff2254b2f9f4d1afa78bdfa17e013a56ed1a320e5d43fe4e0d3dc07b6ad2d6950b5aace5c014c99ffef157a83b11d1ae5e9f20122e1c3c70709cedfa3e5845597a4365650b6d9a7fa970200a13281f5a51d84ed207e05f7ed89b95f7eb7884fe26bb731f6679e85b453f584ca7c5bb3920c507dbba387ae1be87f41889fe62d8e0f1127ced33bff97483ccaa45ef4b7c4a9fbfaa77feed08a08d7b1efca2164c338bba8050832cd6363a95a31deba28dc9f10fc9546a00fdb10c2679ce8a0a93728b9f951b2c202811caae698084a7727ef52332dce3296ac0413fc8da744cffca2dedb133d3c9f7292d9c8ec0a143dfde8c34010282a2ec667dec953b6f898c469b07004a4d4ce7009d5762569800fa9ce60ba2dd8c9c90839765ae83e08e50d359ae13ef8a4d414598e41436f7f4fd9b21d77adb5f26ce872fd1f024aef7b8bfab78b59c9773e56ec3316da99da5747b6c7867c5b27977dc322fd3c7ab121d1865a1fd84db30e72d94a5cc7ef98d758715a846cdcaeb3e059c34d4a83b9cf8bc3bd20cd14502e3a918e6f5722e609bdbfba9bcf8126640c3dc312535058e597dacf588a161dd5c0935dc08914567b2f7cf7f1bfb8923521a99983de07d4f6d37bc9ed7340c98da4ca843b644c9e38d955ba553e04396a142032ae1021afb11b62d8b9b3862a687fee2e98d9171ce6e77960e579e44876cf4aac9a92d9701dc1caf1cc90d2458bc913ba68d65ba9f6562abe6cd00eed235c11a921a1633a5ba1f23c7614bdbce8578da1c0cd1d9c7cced875a9070091526f0bd6e18cf0105d0b0babd0db833a242710a0c2f67c43f6532bda01a2395967cf35a7beb9972b0ae2e8ac14ed9b1acde8ff8acbb017cc284719cb535f66352f70e4150b9bfe2abe5108539f572b83deaf44726c8b4935b2de3f38da7021f81dd9a79fbeaf05c5f3f23c108b4c427066edf75e3cd22a1d6b90c535721629e516adcc0a6a156f9aaa341585c91034cae4686d4929746185d53bf27b0b607b7dec7fec3822a30407093130e3940fa9a758bbeb5aef79f95874959c995babe033550fce3edd7d8e7f6b9ace302220950a6c6a02fe1b5c965f6b1bdbdf0a96ade24c86943d5fa5de4ae7bb87b6b9fcbe3c914b25b29c26371978fe15a8d64c3d93191d0eb6f02afcf184e1c4125ce07d179a2c3d4d9937b5ba96bd29e5c51ce5dc6ab44a79259487226783828707fd9a64f78015542a7ab32b90698f95141a40553726eb114507a5d0d245003dd6a9558c33665c0a0cce2996ce1a998de9e54acfa9b2e46076d21b95eae9553eec9fd4560c15fe1d5dbefbe4d63a114183ee81cb58fbbab0165767a6b872176460837704597fae7c2923011948c90f8570fb0470436d9498dd34b523ba5c9cdd1c3e736459c8b0a47aa1a87632482d1d7e24a242683403cbcf717f31ce0f847bef91326ac1094035f2527353827215a474aa05f9000c24d444f5eab18506a1876ef2b2f62319c7d4d63454e1c231ca5ce210f555cae3e1f33099f83c877babd91c3e22eaf79df9d14708a891e08ce5eb7075f7b97a3be5ee4a0d994ae229c883fe8746f1a2c864b84cc6b5927137e0db3b2508105bed08c6cf5c82231837b360561258d0dca1735b8f1c72c4a34ef10dfbf0c78da5f91dcb5e29f115163c762f18ade2d3c47e268f4c44054c23edfd52128a54a045f4ce6b980902a7331a12f9551920628768542ef898f27e83de5460eeeadeb3c31d4128447a2b8c94b3b003d2a0b0a960579a8ae89a4661d279d45a7260c928c6de041b2b32d86af11685425622b89ec3ac91be4e5a57d928cb9305fe6b6a0ea0b8533f1c26fb7bf8a904f8d1ee2cc61696e4813d0b6355ce97c3fbfa4fd5abab7b849f60e71b04033c1a132911fd236c5cb1c3504413a5a25563d9214cacd7d482457ce1219f900927c6abca462aff7c3046ddad72572a41b5795510f65ae96807cfc3817cb0a265a37434ae9453dedda3c4b4e236af98cb1e3e8edb5a6d0936459934bf0f0ab95ef7165ea6ebc85905c9778f0ef3f2dbfb4e4336c55be652cddd8a1b306d0132272c702ec8c5f7aa5250b68dfd9535858d4f55aa97a88b78bf6a29fe4748c3543d81764201e1d1499cc19c00e79adef4aee248776a2de458bc9dd2317acc85d6e80362d4e9e83deceb281a9b95c1e3801b66cb3a732ef9f80ab7bfb7db28ffa136f38b73cd997fb12fd3b2c0ad8e1114f5a5f8a8e797418888263a0839facfde1707b976ae476191912f5430a9da4163723177c43009f40c49f6d36d1df14e8159763297d25454b19fffdaf103a4b3105a31ff30eca29e1e50b227b5b7ac324b04d22ea3a99319024ab8c78bd8f9df698e672e3cc532432fe54d977ee2fb4ccdb805a161e3421ce708ac0e39dd73b2db6237bd0d49d40321df5c12e69ce72bc445806692a87744f455ee045346968b9c8bb7ad18ac384b1f5d9c6dc931f393e4f5d3968e55e35c7c8f289cb5636dd63eac4f57e9a3333deef361c2f44594235ee42340cd7551e976a66e4406fe9ea5f51a66faca3b8d6777ec82a349dcf82fbb585ca9eeb64c1465ab869b8ece10252107d92478ac0db17f69d930ed57812fe82ff5e9125e8380397e565d698ba66150cdfb523dc4c9e61e9402c1db74ae5eaf97010d24688585f1f79ba4d8b7789862d76712d1e2617362c008074395614c6b1a448233df692a2953a1379c707cd9ebda25d8278dc14a8c664afdf2764d4f81f27e54eed9473bb05e674546645bc34f627909c12dec488dea80fe23b038c195454ebdfe3813ad172b003df0e04121409f6fba06247c01e9f0f59ebb585376a7ef758b25c8d162d7f20b46675c95f3b19ee18285218871130bb5285ecd73449aea6c73ef541080324080620cc543baf0f27ef4443581f8cc2a3790791f790fb5d3d25648bb5c4522477d384b37e47f02fa36b57ce1358a494940d7d752d7b4b8ae06b9cc97ce68a68a75b41699e78111bed24ce15735e2fad34dc6f6b77222bfec512af9f5d2299341836cba499d5888eaed4490201b39798560e9cfd647784beb27edca4a400478e1ee1417056808317b2b48d0a0e26eba77540ed5a78b2b624bc6f996331a4c02acd47c33c8ec8886c7600e514a5dbbbdf08b38df74cc0496dc2daa9d10b632a1cc0d3b58ed352dac9fc40f6ed026e16cd06bb636e8cef1e5a9bc11e817a0d7b27d8a208a0715bcc1da349060bbbc28013a536630f9205cdb5ff5aa52320350e2de10e62fcddcb36f73d7586e94fccec3efc3a09e981ea2c6d2fc8b44982eab61df03603a032cf0bcf185f40908c0a55d39332cccc525f74de92e4d9cd9df1a9512427d3839b10ed119693d2fcd8880df3efef6eeb92771dec30a7b8af58de5e202da9497eb2da2de253c4bc185f669960d985a76ff325ccaef56d0bcb8ed4753ae77c114232d563c4c02227bae1a3bcb670d02a77382fa00dd5e5aacfb8d61176167795ba2fbe77e9d17e7745fbbb40d743c97841335d3baa1ce7056ce9a7745acf9e857d51671cab26a209ccd5f425b0a2c89ebc0d63726265c6feb3ad1b28af606c5284744d561f5c95066d881f0b0b7055f110dc2db8b5b75178d3603a44f18d8665b32f70ca08ea1101a8f24227633b2232710eb55f4a6230430a9c8fdd8dc6fbb18bbffccf340042f78cbc5283b2f3b3fb0e562b58358beb450d3992f01db309a800f0b2e972d3e382aa0257f391bc6ba914642aad122918b6221c07f4989851bc8e1e5bcef5bc76fbea7d32dd56a3e3db5739aa5dd2f6e01c5dc70d8a3b9a34701dfdf5486fd86f59542ec47522a771a2eb5214bb5240cabf7a7831c5502dfad2e5a73cf91fc078eab99327e91a46491c86bcd40fe38a931b88f1eca34f7e202fde1782d47ba51c79f4f034df91cc831f86440c62608a4ee302c12f1608fe0c34ff41dcb2a76fb8f7fa28a32d0f43d8237f7998b4f62c8da9a95b372b3f124287602ad053b8802d5097734f2b2820e005700ddf3737af6cd921ec13a745c4787df157b6f306c30e7fd29989603a6c62591ee952a383100f5fe78d99581510a8c18d76833ed5973556d390269f6695f2aceb984365808bbd0b866392ec2582287c8876be9b74cf3b63ce366ce8a5eaecb5450d307c6ce470b82a18bb16ce18ede8e8eed4c920bf692cc50d6cfd670f01799f0d2b82221377a7df5abd58f4aec531fec756eb26547b38c3242a59cba0690e485f4e0861098b8473e01779e6aeee4234801a0ae258c9c0d615a66c1c9f7a3418006e1403d546c495fd82d0a169cb7303ba34d60bcf1b4bc278942a4b9924a64dc1252092553be7496121b652ddf5bdffdaf5dba5a51c0d59ef533a0092eb6d88e135e282334b1016dd1eb8e912b6b9a7c9ccc678c237998fbeb329b35f6dbf50e4847574977e237cbeffff7a16c1b2a28545d53307ef6879c391d91035bf115eb68856acd5993351ea344406182e2a7e407847ef014e17acb1e8de031437fd6b15a982c33e9cb49c0635b6bb77cc9d72d4be955baf65bdb80c252056d5ed9a52a5d4b089854e66f40270f7c48619bb44af3ca9e58d9d5ce871df8f857dea0b0a9d68f806cf29dda178d62c92926f3595cb6de5e95c04272824233939ab7d1cc762185e74cdf00bc5042a3f93d97f272082e4ab65e5bfb11895850d0c86ef29fbe9241ec61140cc31c516dd74131f6fa90ec1ff4d8362e80a5a4e9bf195d11db6439b0c2690b853d0e2a55e7dd718e3770f5fc501bbfe60cf93a833242420e8a9776597372eea63b865cfffe227b7dd907650bafa078aec022f59d9a4ceb5f5bb6f6a7906e6c5c8fe04903a72ce292f1bcd87213932003f82ba3d174fa28a0950af4ab4e3e8a8b581d20d4f971564114c043fd957a381c085bf02734dde86fb66e1537b3acd25da527433a1b0fe624d606398066ce3c92b6ad2b270696750c6c1bd2782f9bed77774fd1c2611eaff4f26cfe8c5b74d54c17569dae7ceb261a3d975b2ec437ac1a968728a0997fcbb67162c2a3018909b5c52065f1b8878a96effdd4d6c7c0e402cd30455b7221662e06f825ba0bee36868a3fa223746f123ff3de4856605368847afd74837fa03d1b6a11933ee4dc4723533d95e523d6cf34a93d988707606f9b5787d7178aabf00d0d380a276af0529cf086c2d49c17bf00a75c15bd07745bb5e440de4029c3af5796c841642d4918c5b17f052dbaa91b9aa13ca5d4deb2a7951b11a5a82786ded2282a0d6d5bb1e6de319056f075c682b8f66a942afb04ef13f6b8d2aa5c2973f05019323f34380268b3919c36296301c0de6e63988e509d039f7170a5e12029301ef79cb2319683bb16ed6b8cd7735327fa73bcfab639bcbfc63a3b324e72fd145fc308885526895ffd53f6edd3c077be7b7e92c9d5dacbe4cc557db76d389695bf1971f21726f4e5669a68d681b9aae2f5ba9c97560c882123bd94c55c21e8a6066c106cafe11415c715b0620b1617b5251588bee99f9d050d481e1cce2b8d1331e5684705be21a9716f2304e194c18236046188ea487ee878746d030fafd3c240bdf199b01f07a0240ef6f8ab8bf5a71ec9617a0d395f03e5e07c",
    nh = "[a,w,s,cf,f,ge,c,sa,Chil,A,WS,34,sd]";
  !function (t) {
    function i(t, i) {
      for (var n = "", e = 0; e < i.length; e++) n += i.charCodeAt(e).toString();
      var s = Math.floor(n.length / 5),
        r = parseInt(n.charAt(s) + n.charAt(2 * s) + n.charAt(3 * s) + n.charAt(4 * s) + n.charAt(5 * s)),
        h = Math.round(i.length / 2), a = Math.pow(2, 31) - 1, o = parseInt(t.substring(t.length - 8, t.length), 16);
      for (t = t.substring(0, t.length - 8), n += o; n.length > 10;) n = (parseInt(n.substring(0, 10)) + parseInt(n.substring(10, n.length))).toString();
      n = (r * n + h) % a;
      for (var f = "", c = "", e = 0; e < t.length; e += 2) f = parseInt(parseInt(t.substring(e, e + 2), 16) ^ Math.floor(n / a * 255)), c += String.fromCharCode(f), n = (r * n + h) % a;
      return c
    }

    t = i(t, "QUNEE"), nh = JSON.parse(String.fromCharCode(91) + t + String.fromCharCode(93))
  }(ih);
  var eh = nh[0] + nh[1] + nh[2], sh = nh[3] + nh[1] + nh[2], rh = nh[4], hh = nh[5], ah = nh[6], oh = nh[7],
    fh = nh[8], ch = nh[9], uh = nh[10], _h = nh[11], dh = nh[12], lh = nh[13] + nh[14] + nh[15], vh = nh[16],
    bh = nh[17], yh = nh[18], gh = nh[19], xh = nh[20], ph = nh[21] + nh[22], Eh = nh[21],
    mh = nh[23] + nh[24] + nh[25], wh = nh[26], Th = nh[27] + nh[28] + nh[29], kh = nh[30],
    Mh = nh[31] + nh[32] + nh[33], Oh = nh[34] + nh[14] + nh[35], Ih = nh[36] + nh[37] + nh[38],
    Sh = nh[39] + nh[40] + nh[41] + nh[42] + nh[43], Ah = nh[20] + nh[44] + nh[45], jh = nh[46], Ph = nh[47],
    Ch = nh[3] + nh[40] + nh[48], Lh = nh[49], Dh = nh[50], Rh = nh[51], Nh = nh[52] + nh[53] + nh[54],
    Bh = nh[55] + nh[32] + nh[33], $h = nh[56] + nh[24] + nh[57], Fh = nh[58] + nh[59] + nh[60], Gh = nh[61],
    zh = nh[62], Hh = nh[63] + nh[64], Yh = nh[65], Uh = nh[66], Wh = nh[67], qh = nh[68], Xh = nh[69], Vh = nh[22],
    Zh = nh[23] + nh[24] + nh[70], Kh = nh[71], Jh = nh[72], Qh = nh[73], ta = nh[74] + nh[75],
    ia = nh[22] + nh[76] + nh[77] + nh[78], na = nh[79], ea = nh[80], sa = nh[81], ra = nh[82], ha = nh[83],
    aa = nh[84] + nh[37] + nh[85], oa = nh[86] + nh[24] + nh[87], fa = nh[36] + nh[1] + nh[88] + nh[40] + nh[89],
    ca = nh[90], ua = nh[91] + nh[92], _a = nh[93] + nh[94] + nh[95] + nh[1] + nh[96], da = nh[92],
    la = nh[92] + nh[91] + nh[92], va = nh[97], ba = nh[98], ya = nh[99], ga = nh[100],
    xa = nh[101] + nh[102] + nh[103], pa = nh[104], Ea = nh[105], ma = nh[106] + nh[102] + nh[103],
    wa = nh[107] + nh[108] + nh[109], Ta = nh[3] + nh[59] + nh[110] + nh[1] + nh[111] + nh[102] + nh[112],
    ka = nh[113] + nh[114] + nh[115], Ma = nh[116], Oa = nh[107] + nh[117], Ia = nh[107] + nh[118],
    Sa = nh[119] + nh[120] + nh[121], Aa = nh[119] + nh[117], ja = nh[119] + nh[122] + nh[121], Pa = nh[119] + nh[118],
    Ca = nh[123], La = nh[124] + nh[37] + nh[125], Da = nh[22] + nh[126], Ra = nh[22] + nh[127], Na = nh[22] + nh[128],
    Ba = nh[129] + nh[130] + nh[131], $a = nh[132], Fa = nh[133],
    Ga = nh[134] + nh[135] + nh[47] + nh[136] + nh[47] + nh[137] + nh[71],
    za = nh[134] + nh[138] + nh[47] + nh[136] + nh[47] + nh[137] + nh[71], Ha = nh[139], Ya = nh[140],
    Ua = nh[141] + nh[124] + nh[142], Wa = nh[143], qa = nh[144], Xa = nh[145], Va = nh[146] + nh[37] + nh[147],
    Za = nh[148], Ka = nh[134] + nh[149] + nh[47] + nh[137], Ja = nh[150], Qa = nh[151],
    to = nh[106] + nh[24] + nh[152], io = nh[153], no = nh[154],
    eo = nh[155] + nh[59] + nh[156] + nh[37] + nh[157] + nh[24] + nh[158] + nh[102] + nh[159],
    so = nh[160] + nh[59] + nh[156] + nh[37] + nh[157] + nh[24] + nh[158] + nh[102] + nh[159],
    ro = nh[91] + nh[59] + nh[156] + nh[37] + nh[157] + nh[24] + nh[158] + nh[102] + nh[159],
    ho = nh[161] + nh[59] + nh[156] + nh[37] + nh[157] + nh[24] + nh[158] + nh[102] + nh[159],
    ao = nh[162] + nh[37] + nh[157] + nh[24] + nh[158] + nh[102] + nh[159], oo = nh[163] + nh[28] + nh[164],
    fo = nh[165], co = nh[75] + nh[166], uo = nh[167] + nh[37] + nh[168], _o = nh[169] + nh[22] + nh[170],
    lo = nh[154] + nh[47], vo = nh[171], bo = nh[172] + nh[40] + nh[173], yo = nh[172] + nh[59] + nh[174], go = nh[175],
    xo = nh[176], po = nh[177] + nh[114] + nh[178], Eo = nh[179], mo = nh[180] + nh[114] + nh[178],
    wo = nh[3] + nh[181] + nh[182] + nh[53] + nh[183], To = nh[184], ko = nh[22] + nh[185] + nh[102],
    Mo = nh[22] + nh[186] + nh[24] + nh[152], Oo = nh[22] + nh[187] + nh[188] + nh[1] + nh[189], Io = nh[190],
    So = nh[191], Ao = nh[192], jo = nh[193] + nh[24] + nh[152], Po = nh[167] + nh[114] + nh[194],
    Co = nh[195] + nh[24] + nh[152] + nh[181] + nh[76] + nh[37] + nh[196], Lo = nh[197], Do = nh[177], Ro = nh[198],
    No = nh[199] + nh[108] + nh[109], Bo = nh[195] + nh[24] + nh[152] + nh[181] + nh[76] + nh[24] + nh[200],
    $o = nh[201] + nh[24] + nh[152], Fo = nh[202], Go = nh[203], zo = nh[22] + nh[204],
    Ho = nh[22] + nh[205] + nh[206] + nh[81], Yo = nh[22] + nh[205] + nh[206] + nh[82], Uo = nh[22] + nh[207] + nh[206],
    Wo = nh[22] + nh[205] + nh[75] + nh[81], qo = nh[22] + nh[205] + nh[75] + nh[82], Xo = nh[22] + nh[207] + nh[75],
    Vo = nh[26] + nh[208] + nh[24] + nh[152], Zo = nh[209], Ko = nh[197] + nh[37] + nh[210], Jo = nh[67] + nh[211],
    Qo = nh[197] + nh[102] + nh[112], tf = nh[177] + nh[37] + nh[210], nf = nh[212] + nh[40] + nh[213], ef = nh[214],
    sf = nh[215], rf = nh[216] + nh[24] + nh[200], hf = nh[217],
    af = nh[67] + nh[15] + nh[218] + nh[15] + nh[206] + nh[15] + nh[75], of = nh[219] + nh[114] + nh[161],
    ff = nh[199] + nh[114] + nh[161], cf = nh[67] + nh[220], uf = nh[67] + nh[221],
    _f = nh[201] + nh[181] + nh[222] + nh[14] + nh[15], df = nh[22] + nh[207] + nh[223],
    lf = nh[224] + nh[22] + nh[225], vf = nh[3] + nh[59] + nh[226], bf = nh[3] + nh[1] + nh[227],
    yf = nh[214] + nh[228] + nh[229], gf = nh[230] + nh[71] + nh[231], xf = nh[232], pf = nh[233],
    Ef = nh[22] + nh[234], mf = nh[235], wf = nh[137], Tf = nh[233] + nh[47] + nh[236] + nh[47] + nh[137] + nh[237],
    kf = nh[3] + nh[44] + nh[238], Mf = nh[13], Of = nh[199], If = nh[239], Sf = nh[240] + nh[114] + nh[241],
    Af = nh[64], jf = nh[242], Pf = nh[243] + nh[22] + nh[244], Cf = nh[243] + nh[22] + nh[244] + nh[22] + nh[245],
    Lf = nh[243] + nh[22] + nh[244] + nh[22] + nh[246],
    Df = nh[243] + nh[22] + nh[244] + nh[22] + nh[247] + nh[22] + nh[248],
    Rf = nh[243] + nh[22] + nh[244] + nh[22] + nh[249] + nh[22] + nh[248],
    Nf = nh[243] + nh[22] + nh[244] + nh[22] + nh[250], Bf = nh[251], $f = nh[252], Ff = nh[151] + nh[102] + nh[112],
    Gf = nh[22] + nh[253] + nh[254], zf = nh[255] + nh[24] + nh[200], Hf = nh[22] + nh[256],
    Yf = nh[26] + nh[257] + nh[59] + nh[82] + nh[40] + nh[258] + nh[24] + nh[152], Uf = nh[22] + nh[207] + nh[259],
    Wf = nh[26] + nh[203], qf = nh[26] + nh[260], Xf = nh[261] + nh[262] + nh[102] + nh[263],
    Vf = nh[26] + nh[264] + nh[118], Zf = nh[26] + nh[265] + nh[37] + nh[168],
    Kf = nh[26] + nh[265] + nh[40] + nh[258] + nh[24] + nh[152], Jf = nh[167] + nh[59] + nh[82] + nh[102] + nh[112],
    Qf = nh[26] + nh[232], tc = nh[266], ic = nh[26] + nh[267] + nh[108] + nh[109],
    nc = nh[26] + nh[268] + nh[24] + nh[269],
    ec = nh[22] + nh[270] + nh[271] + nh[272] + nh[181] + nh[273] + nh[42] + nh[274], sc = nh[275] + nh[271] + nh[272],
    rc = nh[22] + nh[276] + nh[37] + nh[277], hc = nh[141] + nh[278] + nh[206] + nh[24] + nh[279],
    ac = nh[26] + nh[280], oc = nh[26] + nh[280] + nh[102] + nh[281], fc = nh[22] + nh[267] + nh[117],
    cc = nh[26] + nh[264] + nh[117], uc = nh[26] + nh[267] + nh[117], _c = nh[22] + nh[267] + nh[118],
    dc = nh[26] + nh[267] + nh[118], lc = nh[3] + nh[102] + nh[112], vc = nh[282],
    bc = nh[257] + nh[59] + nh[82] + nh[24] + nh[200], yc = nh[26] + nh[198], gc = nh[195] + nh[28] + nh[283],
    xc = nh[22] + nh[262] + nh[102] + nh[263], pc = nh[26] + nh[265] + nh[102] + nh[263],
    Ec = nh[199] + nh[53] + nh[284], mc = nh[3] + nh[44] + nh[285] + nh[53] + nh[284],
    wc = nh[167] + nh[44] + nh[285] + nh[53] + nh[284], Tc = nh[160] + nh[53] + nh[284],
    kc = nh[155] + nh[44] + nh[285] + nh[53] + nh[284], Mc = nh[199] + nh[53] + nh[284] + nh[14] + nh[121],
    Oc = nh[160] + nh[53] + nh[284] + nh[14] + nh[121],
    Ic = nh[155] + nh[44] + nh[285] + nh[53] + nh[284] + nh[14] + nh[121], Sc = nh[286] + nh[271] + nh[287],
    Ac = nh[286] + nh[181] + nh[222], jc = nh[288] + nh[114] + nh[289], Pc = nh[290] + nh[228] + nh[291],
    Cc = nh[292] + nh[42] + nh[274], Lc = nh[293], Dc = nh[294] + nh[28] + nh[295],
    Rc = nh[296] + nh[181] + nh[182] + nh[53] + nh[183], Nc = nh[297] + nh[24] + nh[78],
    Bc = nh[298] + nh[228] + nh[289] + nh[114] + nh[299], $c = nh[300], Fc = nh[301] + nh[114] + nh[178], Gc = nh[302],
    zc = nh[303], Hc = nh[304], Yc = nh[305] + nh[1] + nh[229] + nh[40] + nh[239], Uc = nh[305] + nh[40] + nh[239],
    Wc = nh[84] + nh[1] + nh[306] + nh[1] + nh[229], qc = nh[307] + nh[94], Xc = nh[308], Vc = nh[309], Zc = nh[310],
    Kc = nh[311], Jc = nh[312], Qc = nh[218] + nh[71] + nh[218] + nh[71] + nh[206],
    tu = nh[22] + nh[313] + nh[1] + nh[314], iu = nh[315], nu = nh[316] + nh[1] + nh[88],
    eu = nh[20] + nh[317] + nh[318], su = nh[319], ru = nh[93] + nh[40] + nh[320],
    hu = nh[3] + nh[28] + nh[321] + nh[59] + nh[322], au = nh[22] + nh[323] + nh[317] + nh[324],
    ou = nh[84] + nh[40] + nh[320], fu = nh[22] + nh[207] + nh[325], cu = nh[195] + nh[181] + nh[326], uu = nh[84],
    _u = nh[195] + nh[44] + nh[327], du = nh[328], lu = nh[167] + nh[1] + nh[88] + nh[181] + nh[222],
    vu = nh[5] + nh[1] + nh[329], bu = nh[330], yu = nh[167] + nh[181] + nh[222],
    gu = nh[294] + nh[28] + nh[295] + nh[28] + nh[321], xu = nh[26] + nh[84],
    pu = nh[294] + nh[28] + nh[295] + nh[1] + nh[88], Eu = nh[331] + nh[114] + nh[161],
    mu = nh[332] + nh[114] + nh[161], wu = nh[333], Tu = nh[334] + nh[22] + nh[335], ku = nh[334] + nh[22] + nh[336],
    Mu = nh[334] + nh[22] + nh[337], Ou = nh[334] + nh[22] + nh[338], Iu = nh[334] + nh[22] + nh[339],
    Su = nh[334] + nh[22] + nh[340], Au = nh[334] + nh[22] + nh[341], ju = nh[334] + nh[22] + nh[342],
    Pu = nh[334] + nh[22] + nh[343], Cu = nh[334] + nh[22] + nh[344], Lu = nh[334] + nh[22] + nh[345],
    Du = nh[334] + nh[22] + nh[346], Ru = nh[334] + nh[22] + nh[347] + nh[22] + nh[348],
    Nu = nh[334] + nh[22] + nh[347] + nh[22] + nh[75], Bu = nh[334] + nh[22] + nh[347] + nh[22] + nh[325],
    $u = nh[334] + nh[22] + nh[347] + nh[22] + nh[223], Fu = nh[334] + nh[22] + nh[347] + nh[22] + nh[349],
    Gu = nh[334] + nh[22] + nh[347] + nh[22] + nh[188], zu = nh[334] + nh[22] + nh[347] + nh[22] + nh[350],
    Hu = nh[334] + nh[22] + nh[347] + nh[22] + nh[351], Yu = nh[352] + nh[44] + nh[353], Uu = nh[354],
    Wu = nh[191] + nh[42] + nh[355], qu = nh[26] + nh[265], Xu = nh[356], Vu = nh[195] + nh[40] + nh[357], Zu = nh[358],
    Ku = nh[359], Ju = nh[360] + nh[24] + nh[25], Qu = nh[361], t_ = nh[362] + nh[22] + nh[363] + nh[22] + nh[364],
    i_ = nh[362] + nh[22] + nh[363] + nh[22] + nh[365], n_ = nh[362] + nh[22] + nh[363] + nh[22] + nh[366],
    e_ = nh[167] + nh[37] + nh[210], s_ = nh[3] + nh[37] + nh[210], r_ = nh[367] + nh[22] + nh[368],
    h_ = nh[367] + nh[22] + nh[363] + nh[22] + nh[369] + nh[22] + nh[370],
    a_ = nh[367] + nh[22] + nh[363] + nh[22] + nh[370] + nh[22] + nh[371],
    o_ = nh[367] + nh[22] + nh[363] + nh[22] + nh[368] + nh[22] + nh[372],
    f_ = nh[367] + nh[22] + nh[363] + nh[22] + nh[368] + nh[22] + nh[373],
    c_ = nh[367] + nh[22] + nh[363] + nh[22] + nh[369] + nh[22] + nh[371],
    u_ = nh[367] + nh[22] + nh[363] + nh[22] + nh[371] + nh[22] + nh[370],
    __ = nh[367] + nh[22] + nh[374] + nh[22] + nh[375] + nh[22] + nh[376],
    d_ = nh[367] + nh[22] + nh[363] + nh[22] + nh[368] + nh[22] + nh[377],
    l_ = nh[367] + nh[22] + nh[363] + nh[22] + nh[368] + nh[22] + nh[378],
    v_ = nh[367] + nh[22] + nh[363] + nh[22] + nh[379], b_ = nh[380] + nh[22] + nh[381],
    y_ = nh[367] + nh[22] + nh[382] + nh[22] + nh[383], g_ = nh[367] + nh[22] + nh[384] + nh[22] + nh[385],
    x_ = nh[386], p_ = nh[193], E_ = nh[367] + nh[22] + nh[363] + nh[22] + nh[369],
    m_ = nh[367] + nh[22] + nh[363] + nh[22] + nh[379] + nh[22] + nh[371], w_ = nh[347] + nh[22] + nh[170],
    T_ = nh[367] + nh[22] + nh[387] + nh[22] + nh[363] + nh[22] + nh[388],
    k_ = nh[3] + nh[24] + nh[389] + nh[24] + nh[152], M_ = nh[367] + nh[22] + nh[390] + nh[22] + nh[391],
    O_ = nh[367] + nh[22] + nh[392] + nh[22] + nh[393], I_ = nh[367] + nh[22] + nh[392] + nh[22] + nh[391],
    S_ = nh[394], A_ = nh[3] + nh[395], j_ = nh[354] + nh[114] + nh[299],
    P_ = nh[3] + nh[28] + nh[396] + nh[24] + nh[152] + nh[59] + nh[226],
    C_ = nh[0] + nh[24] + nh[200] + nh[37] + nh[397], L_ = nh[398], D_ = nh[399],
    R_ = nh[93] + nh[40] + nh[239] + nh[28] + nh[321], N_ = nh[400] + nh[24] + nh[152], B_ = nh[401],
    $_ = nh[402] + nh[114] + nh[403], F_ = nh[404] + nh[22] + nh[367] + nh[22] + nh[405] + nh[22] + nh[406],
    G_ = nh[26] + nh[407] + nh[28] + nh[408], z_ = nh[367] + nh[22] + nh[387] + nh[22] + nh[363],
    H_ = nh[367] + nh[22] + nh[409] + nh[22] + nh[410], Y_ = nh[411] + nh[118], U_ = nh[412], W_ = nh[413],
    q_ = nh[414] + nh[92] + nh[271] + nh[415] + nh[92] + nh[317] + nh[416] + nh[92] + nh[59] + nh[417], X_ = nh[418],
    V_ = nh[419], Z_ = nh[420], K_ = nh[421], J_ = nh[203] + nh[64] + nh[422] + nh[423] + nh[66],
    Q_ = nh[22] + nh[424] + nh[24] + nh[425], td = nh[414] + nh[92] + nh[271] + nh[415] + nh[92] + nh[317] + nh[416],
    id = nh[63] + nh[64] + nh[218] + nh[426] + nh[218] + nh[426] + nh[218] + nh[426] + nh[218] + nh[66], nd = nh[427],
    ed = nh[428], sd = nh[429], rd = nh[22] + nh[297], hd = nh[430] + nh[431],
    ad = nh[432] + nh[22] + nh[224] + nh[22] + nh[377], od = nh[297], fd = nh[22] + nh[79], cd = nh[218] + nh[154],
    ud = nh[22] + nh[433], _d = nh[199] + nh[1] + nh[434], dd = nh[199] + nh[435] + nh[436],
    ld = nh[437] + nh[1] + nh[438], vd = nh[67] + nh[439], bd = nh[437] + nh[59] + nh[440],
    yd = nh[432] + nh[22] + nh[224] + nh[22] + nh[372], gd = nh[93] + nh[53] + nh[183] + nh[441],
    xd = nh[233] + nh[181] + nh[182], pd = nh[442], Ed = nh[107] + nh[443] + nh[444], md = nh[445],
    wd = nh[446] + nh[228] + nh[447], Td = nh[195] + nh[37] + nh[448], kd = nh[236] + nh[449] + nh[450],
    Md = nh[297] + nh[1] + nh[314], Od = nh[451], Id = nh[31] + nh[1] + nh[452], Sd = nh[31],
    Ad = nh[294] + nh[28] + nh[295] + nh[102] + nh[453] + nh[32] + nh[454] + nh[395], jd = nh[455] + nh[59] + nh[226],
    Pd = nh[456] + nh[22] + nh[406], Cd = nh[3] + nh[24] + nh[389] + nh[24] + nh[457], Ld = nh[31] + nh[24] + nh[152],
    Dd = nh[3] + nh[53] + nh[54] + nh[24] + nh[389] + nh[24] + nh[152], Rd = nh[264] + nh[108] + nh[109],
    Nd = nh[264] + nh[443] + nh[444], Bd = nh[458], $d = nh[458] + nh[37] + nh[168], Fd = nh[458] + nh[42] + nh[459],
    Gd = nh[446] + nh[37] + nh[460], zd = nh[446] + nh[28] + nh[396], Hd = nh[461], Yd = nh[446], Ud = nh[172],
    Wd = nh[462], qd = nh[463], Xd = nh[464] + nh[117], Vd = nh[464] + nh[118], Zd = nh[464] + nh[114] + nh[161],
    Kd = nh[127] + nh[37] + nh[168], Jd = nh[465] + nh[92] + nh[466], Qd = nh[467] + nh[28] + nh[164],
    tl = nh[22] + nh[468], il = nh[469], nl = nh[22] + nh[195] + nh[42] + nh[43] + nh[114] + nh[470], el = nh[471],
    sl = nh[472], rl = nh[473], hl = nh[474], al = nh[475], ol = nh[106] + nh[44] + nh[476],
    fl = nh[477] + nh[228] + nh[229], cl = nh[478] + nh[22] + nh[244] + nh[22] + nh[479],
    ul = nh[316] + nh[317] + nh[229] + nh[181] + nh[480], _l = nh[316] + nh[28] + nh[321] + nh[181] + nh[480],
    dl = nh[186] + nh[317] + nh[229], ll = nh[22] + nh[481],
    vl = nh[294] + nh[28] + nh[295] + nh[14] + nh[482] + nh[28] + nh[321], bl = nh[483] + nh[40] + nh[320],
    yl = nh[155] + nh[102] + nh[484] + nh[40] + nh[41] + nh[42] + nh[43],
    gl = nh[161] + nh[102] + nh[484] + nh[40] + nh[41] + nh[42] + nh[43], xl = nh[167] + nh[114] + nh[485],
    pl = nh[58] + nh[40] + nh[41] + nh[42] + nh[43], El = nh[155] + nh[1] + nh[486] + nh[40] + nh[41] + nh[42] + nh[43],
    ml = nh[160] + nh[1] + nh[486] + nh[40] + nh[41] + nh[42] + nh[43], wl = nh[67] + nh[487],
    Tl = nh[22] + nh[458] + nh[37] + nh[210], kl = nh[488], Ml = nh[22] + nh[458] + nh[37] + nh[168],
    Ol = nh[22] + nh[458] + nh[1] + nh[489], Il = nh[22] + nh[458] + nh[42] + nh[459], Sl = nh[22] + nh[458],
    Al = nh[169] + nh[22] + nh[366], jl = nh[169] + nh[22] + nh[490], Pl = nh[3] + nh[59] + nh[82] + nh[181] + nh[222],
    Cl = nh[134] + nh[491] + nh[47] + nh[492], Ll = nh[401] + nh[59] + nh[82] + nh[181] + nh[166],
    Dl = nh[22] + nh[207] + nh[188], Rl = nh[3] + nh[59] + nh[82] + nh[181] + nh[166], Nl = nh[93] + nh[53] + nh[493],
    Bl = nh[400] + nh[28] + nh[164] + nh[1] + nh[88], $l = nh[494] + nh[317] + nh[318],
    Fl = nh[495] + nh[28] + nh[164] + nh[37] + nh[496], Gl = nh[497] + nh[114] + nh[299],
    zl = nh[24] + nh[152] + nh[64], Hl = nh[426], Yl = nh[498], Ul = nh[22] + nh[499], Wl = nh[37] + nh[168] + nh[64],
    ql = nh[500] + nh[47] + nh[137], Xl = nh[193] + nh[102] + nh[112], Vl = nh[501] + nh[22] + nh[502], Zl = nh[503],
    Kl = nh[504] + nh[24] + nh[269], Jl = nh[505] + nh[24] + nh[269], Ql = nh[506] + nh[317] + nh[318], tv = nh[507],
    iv = nh[508], nv = nh[509], ev = nh[278], sv = nh[510], rv = nh[372] + nh[22] + nh[377],
    hv = nh[372] + nh[22] + nh[511], av = nh[512] + nh[22] + nh[377], ov = nh[512] + nh[22] + nh[511],
    fv = nh[512] + nh[22] + nh[378], cv = nh[373] + nh[22] + nh[377], uv = nh[372] + nh[22] + nh[378],
    _v = nh[373] + nh[22] + nh[511], dv = nh[373] + nh[22] + nh[378], lv = nh[513], vv = nh[514],
    bv = nh[235] + nh[515], yv = nh[426] + nh[202] + nh[515], gv = nh[426] + nh[473] + nh[515],
    xv = nh[359] + nh[114] + nh[299], pv = nh[359] + nh[71] + nh[516],
    Ev = nh[426] + nh[359] + nh[317] + nh[318] + nh[515], mv = nh[426] + nh[517] + nh[32] + nh[33] + nh[515],
    wv = nh[517] + nh[32] + nh[33], Tv = nh[426] + nh[356] + nh[515], kv = nh[359] + nh[317] + nh[318],
    Mv = nh[518] + nh[181] + nh[222], Ov = nh[3] + nh[1] + nh[88] + nh[181] + nh[222], Iv = nh[519] + nh[71] + nh[106],
    Sv = nh[519] + nh[71] + nh[11], Av = nh[519], jv = nh[517] + nh[181] + nh[222], Pv = nh[519] + nh[71] + nh[13],
    Cv = nh[520], Lv = nh[36] + nh[28] + nh[29], Dv = nh[521], Rv = nh[522], Nv = nh[126],
    Bv = nh[11] + nh[44] + nh[476], $v = nh[523], Fv = nh[426] + nh[198] + nh[515], Gv = nh[426] + nh[13] + nh[515],
    zv = nh[426] + nh[517] + nh[181] + nh[222] + nh[515], Hv = nh[524] + nh[22] + nh[525], Yv = nh[106],
    Uv = nh[524] + nh[22] + nh[526], Wv = nh[524] + nh[22] + nh[527] + nh[22] + nh[528], qv = nh[13] + nh[71] + nh[516],
    Xv = nh[22] + nh[529], Vv = nh[93] + nh[1] + nh[2], Zv = nh[36] + nh[1] + nh[2] + nh[1] + nh[530],
    Kv = nh[3] + nh[181] + nh[166], Jv = nh[22] + nh[531], Qv = nh[22] + nh[15] + nh[77],
    tb = nh[524] + nh[22] + nh[532], ib = nh[533], nb = nh[523] + nh[1] + nh[534] + nh[53] + nh[535],
    eb = nh[446] + nh[1] + nh[534] + nh[53] + nh[535], sb = nh[22] + nh[446] + nh[228] + nh[447],
    rb = nh[198] + nh[1] + nh[534] + nh[53] + nh[535],
    hb = nh[27] + nh[53] + nh[183] + nh[24] + nh[25] + nh[1] + nh[534],
    ab = nh[319] + nh[1] + nh[534] + nh[53] + nh[535],
    ob = nh[519] + nh[181] + nh[222] + nh[1] + nh[534] + nh[53] + nh[535], fb = nh[26] + nh[330],
    cb = nh[22] + nh[536] + nh[181] + nh[222] + nh[42] + nh[274], ub = nh[3] + nh[28] + nh[537], _b = nh[141] + nh[538],
    db = nh[198] + nh[539], lb = nh[22] + nh[540] + nh[1] + nh[534] + nh[44] + nh[476], vb = nh[541],
    bb = nh[114] + nh[194], yb = nh[93] + nh[44] + nh[542] + nh[1] + nh[96], gb = nh[163] + nh[24] + nh[543],
    xb = nh[172] + nh[449] + nh[544], pb = nh[309] + nh[92] + nh[545], Eb = nh[546], mb = nh[547],
    wb = nh[106] + nh[28] + nh[29] + nh[44] + nh[476], Tb = nh[11] + nh[28] + nh[29] + nh[44] + nh[476],
    kb = nh[548] + nh[22] + nh[549] + nh[22] + nh[550] + nh[22] + nh[551], Mb = nh[552] + nh[22] + nh[549],
    Ob = nh[553] + nh[65] + nh[554] + nh[65] + nh[555] + nh[65] + nh[556], Ib = nh[557],
    Sb = nh[558] + nh[559] + nh[37] + nh[560],
    Ab = nh[561] + nh[65] + nh[562] + nh[65] + nh[563] + nh[65] + nh[564] + nh[65] + nh[565] + nh[65] + nh[566] + nh[65] + nh[567] + nh[65] + nh[568] + nh[65],
    jb = nh[65] + nh[553] + nh[65] + nh[554] + nh[65] + nh[555] + nh[65] + nh[556],
    Pb = nh[114] + nh[569] + nh[28] + nh[29], Cb = nh[22] + nh[76] + nh[77] + nh[570] + nh[181] + nh[571],
    Lb = nh[141] + nh[572], Db = nh[22] + nh[573], Rb = nh[566], Nb = nh[563], Bb = nh[141] + nh[468],
    $b = nh[22] + nh[93] + nh[574] + nh[29], Fb = nh[22] + nh[575] + nh[28] + nh[29],
    Gb = nh[22] + nh[36] + nh[28] + nh[29], zb = nh[36], Hb = nh[141] + nh[576] + nh[24] + nh[577] + nh[114] + nh[470],
    Yb = nh[141] + nh[36] + nh[44] + nh[578] + nh[24] + nh[577] + nh[42] + nh[579],
    Ub = nh[22] + nh[580] + nh[28] + nh[29], Wb = nh[581] + nh[75], qb = nh[581],
    Xb = nh[22] + nh[76] + nh[77] + nh[486] + nh[44] + nh[578] + nh[24] + nh[577] + nh[114] + nh[470], Vb = nh[582],
    Zb = nh[141] + nh[583] + nh[1] + nh[329] + nh[1] + nh[534], Kb = nh[22] + nh[584],
    Jb = nh[141] + nh[580] + nh[44] + nh[578] + nh[24] + nh[577],
    Qb = nh[141] + nh[580] + nh[228] + nh[585] + nh[114] + nh[569] + nh[28] + nh[29], ty = nh[22] + nh[214],
    iy = nh[166] + nh[37] + nh[586], ny = nh[587] + nh[37] + nh[586], ey = nh[141] + nh[588], sy = nh[589],
    ry = nh[22] + nh[590], hy = nh[22] + nh[591], ay = nh[592],
    oy = nh[141] + nh[593] + nh[114] + nh[569] + nh[28] + nh[29], fy = nh[594], cy = nh[22] + nh[595], uy = nh[562],
    _y = nh[141] + nh[76] + nh[77] + nh[486] + nh[1] + nh[596], dy = nh[22] + nh[597], ly = nh[564],
    vy = nh[22] + nh[195] + nh[53] + nh[598] + nh[1] + nh[596], by = nh[141] + nh[420], yy = nh[565], gy = nh[561],
    xy = nh[599], py = nh[600], Ey = nh[601], my = nh[602], wy = nh[603], Ty = nh[604] + nh[117],
    ky = nh[604] + nh[118], My = nh[22] + nh[580] + nh[53] + nh[605] + nh[53] + nh[606],
    Oy = nh[141] + nh[76] + nh[77] + nh[596] + nh[114] + nh[470], Iy = nh[607], Sy = nh[141] + nh[608], Ay = nh[609],
    jy = nh[584] + nh[75], Py = nh[584], Cy = nh[597] + nh[75], Ly = nh[597], Dy = nh[22] + nh[610] + nh[24] + nh[457],
    Ry = nh[611] + nh[75], Ny = nh[611], By = nh[22] + nh[612] + nh[53] + nh[605] + nh[181] + nh[480],
    $y = nh[591] + nh[75], Fy = nh[591], Gy = nh[3] + nh[1] + nh[570] + nh[37] + nh[613], zy = nh[595] + nh[75],
    Hy = nh[595], Yy = nh[22] + nh[507] + nh[188] + nh[37] + nh[614], Uy = nh[3] + nh[53] + nh[183],
    Wy = nh[165] + nh[24] + nh[615],
    qy = nh[3] + nh[28] + nh[164] + nh[59] + nh[82] + nh[228] + nh[559] + nh[28] + nh[29],
    Xy = nh[22] + nh[616] + nh[53] + nh[183], Vy = nh[36] + nh[28] + nh[164] + nh[102] + nh[617],
    Zy = nh[22] + nh[618] + nh[44] + nh[619], Ky = nh[36] + nh[1] + nh[530], Jy = nh[22] + nh[206] + nh[620],
    Qy = nh[22] + nh[621] + nh[181] + nh[622],
    tg = nh[22] + nh[76] + nh[77] + nh[623] + nh[181] + nh[624] + nh[44] + nh[619],
    ig = nh[141] + nh[36] + nh[28] + nh[29], ng = nh[141] + nh[575] + nh[28] + nh[29], eg = nh[625],
    sg = nh[22] + nh[621] + nh[181] + nh[624], rg = nh[22] + nh[580] + nh[117], hg = nh[22] + nh[580] + nh[118],
    ag = nh[626], og = nh[627] + nh[118], fg = nh[628] + nh[53] + nh[629] + nh[117], cg = nh[630] + nh[1] + nh[329],
    ug = nh[92] + nh[155] + nh[92] + nh[631] + nh[92] + nh[632],
    _g = nh[92] + nh[155] + nh[92] + nh[631] + nh[92] + nh[633], dg = nh[92] + nh[155] + nh[92] + nh[634],
    lg = nh[92] + nh[155] + nh[92] + nh[635], vg = nh[92] + nh[160] + nh[92] + nh[631] + nh[92] + nh[632],
    bg = nh[92] + nh[160] + nh[92] + nh[631] + nh[92] + nh[633], yg = nh[92] + nh[160] + nh[92] + nh[634],
    gg = nh[92] + nh[160] + nh[92] + nh[635], xg = nh[636], pg = nh[219],
    Eg = nh[637] + nh[64] + nh[198] + nh[98] + nh[236] + nh[449] + nh[638] + nh[99] + nh[639] + nh[640] + nh[65] + nh[641] + nh[642] + nh[643] + nh[218] + nh[644] + nh[645] + nh[646] + nh[647] + nh[648] + nh[649] + nh[650] + nh[15] + nh[259] + nh[449] + nh[77] + nh[647] + nh[651] + nh[510] + nh[75] + nh[652] + nh[218] + nh[166] + nh[75] + nh[42] + nh[82] + nh[653] + nh[77] + nh[641] + nh[654] + nh[510] + nh[655] + nh[76] + nh[656] + nh[507] + nh[657] + nh[349] + nh[658] + nh[659] + nh[660] + nh[317] + nh[661] + nh[449] + nh[24] + nh[662] + nh[24] + nh[643] + nh[228] + nh[663] + nh[664] + nh[15] + nh[665] + nh[82] + nh[666] + nh[76] + nh[449] + nh[667] + nh[668] + nh[669] + nh[670] + nh[660] + nh[40] + nh[671] + nh[271] + nh[649] + nh[117] + nh[223] + nh[672] + nh[673] + nh[674] + nh[278] + nh[675] + nh[643] + nh[206] + nh[1] + nh[676] + nh[677] + nh[507] + nh[40] + nh[206] + nh[82] + nh[40] + nh[223] + nh[678] + nh[130] + nh[647] + nh[118] + nh[643] + nh[59] + nh[679] + nh[680] + nh[681] + nh[682] + nh[683] + nh[684] + nh[649] + nh[685] + nh[649] + nh[686] + nh[259] + nh[647] + nh[449] + nh[223] + nh[687] + nh[688] + nh[689] + nh[690] + nh[349] + nh[691] + nh[692] + nh[259] + nh[47] + nh[259] + nh[65] + nh[636],
    mg = nh[22] + nh[76] + nh[77] + nh[693], wg = nh[22] + nh[39] + nh[694], Tg = nh[22] + nh[36] + nh[37] + nh[695],
    kg = nh[22] + nh[696], Mg = nh[22] + nh[76] + nh[77] + nh[697], Og = nh[218] + nh[71] + nh[218],
    Ig = nh[195] + nh[114] + nh[569] + nh[37] + nh[698], Sg = nh[401] + nh[102] + nh[112], Ag = nh[102] + nh[112],
    jg = nh[24] + nh[152], Pg = nh[181] + nh[699], Cg = nh[28] + nh[29],
    Lg = nh[24] + nh[25] + nh[1] + nh[534] + nh[28] + nh[29], Dg = nh[44] + nh[45] + nh[28] + nh[29],
    Rg = nh[443] + nh[700], Ng = nh[53] + nh[535], Bg = nh[24] + nh[269], $g = nh[53] + nh[183],
    Fg = nh[53] + nh[183] + nh[228] + nh[447], Gg = nh[701] + nh[476], zg = nh[149] + nh[441], Hg = nh[149] + nh[135],
    Yg = nh[149] + nh[138], Ug = nh[195] + nh[228] + nh[702] + nh[130] + nh[131],
    Wg = nh[53] + nh[605] + nh[37] + nh[698], qg = nh[703], Xg = nh[106] + nh[704] + nh[103], Vg = nh[705],
    Zg = nh[706], Kg = nh[280] + nh[71] + nh[217], Jg = nh[280], Qg = nh[437], tx = nh[707],
    ix = nh[707] + nh[71] + nh[443], nx = nh[707] + nh[71] + nh[32], ex = nh[708], sx = nh[708] + nh[71] + nh[443],
    rx = nh[708] + nh[71] + nh[32], hx = nh[708] + nh[71] + nh[443] + nh[71] + nh[32],
    ax = nh[708] + nh[71] + nh[32] + nh[71] + nh[443], ox = nh[709] + nh[71] + nh[297], fx = nh[709] + nh[71] + nh[123],
    cx = nh[709] + nh[71] + nh[80], ux = nh[709] + nh[71] + nh[79], _x = nh[710], dx = nh[711], lx = nh[712],
    vx = nh[713], bx = nh[714], yx = nh[715], gx = nh[716], xx = nh[717], px = nh[718], Ex = nh[719], mx = nh[720],
    wx = nh[721], Tx = nh[722], kx = nh[723], Mx = nh[724], Ox = nh[725], Ix = nh[726] + nh[71] + nh[727],
    Sx = nh[726] + nh[71] + nh[206], Ax = nh[726] + nh[71] + nh[75], jx = nh[726] + nh[71] + nh[325],
    Px = nh[726] + nh[71] + nh[223], Cx = nh[726] + nh[71] + nh[349], Lx = nh[726] + nh[71] + nh[188],
    Dx = nh[726] + nh[71] + nh[350], Rx = nh[726] + nh[71] + nh[259], Nx = nh[726] + nh[71] + nh[143],
    Bx = nh[728] + nh[22] + nh[729] + nh[22] + nh[363] + nh[22] + nh[730], $x = nh[731],
    Fx = nh[728] + nh[22] + nh[729] + nh[22] + nh[363] + nh[22] + nh[383],
    Gx = nh[728] + nh[22] + nh[729] + nh[22] + nh[363] + nh[22] + nh[732], zx = nh[185], Hx = nh[733],
    Yx = nh[367] + nh[22] + nh[387] + nh[22] + nh[363] + nh[22] + nh[734], Ux = nh[332], Wx = nh[735],
    qx = nh[456] + nh[22] + nh[363] + nh[22] + nh[736], Xx = nh[456] + nh[22] + nh[737],
    Vx = nh[456] + nh[22] + nh[736] + nh[22] + nh[738], Zx = nh[456] + nh[22] + nh[247],
    Kx = nh[456] + nh[22] + nh[363], Jx = nh[737] + nh[22] + nh[739], Qx = nh[740] + nh[22] + nh[741],
    tp = nh[728] + nh[22] + nh[225], ip = nh[742] + nh[24] + nh[158] + nh[102] + nh[159],
    np = nh[163] + nh[1] + nh[314], ep = nh[264], sp = nh[743] + nh[114] + nh[161],
    rp = nh[744] + nh[1] + nh[745] + nh[114] + nh[161], hp = nh[746] + nh[1] + nh[745] + nh[114] + nh[161],
    ap = nh[26] + nh[208] + nh[24] + nh[152] + nh[206], op = nh[26] + nh[208] + nh[24] + nh[152] + nh[75],
    fp = nh[224] + nh[22] + nh[479], cp = nh[747] + nh[1] + nh[438], up = nh[224] + nh[22] + nh[748],
    _p = nh[501] + nh[22] + nh[749] + nh[22] + nh[750], dp = nh[22] + nh[751], lp = nh[319] + nh[317] + nh[229],
    vp = nh[181] + nh[182] + nh[47] + nh[149] + nh[47] + nh[137] + nh[237], bp = nh[22] + nh[76] + nh[77] + nh[82],
    yp = nh[40] + nh[752], gp = nh[753], xp = nh[11] + nh[1] + nh[88], pp = nh[22] + nh[293], Ep = nh[754],
    mp = nh[224] + nh[22] + nh[501] + nh[22] + nh[170], wp = nh[22] + nh[83], Tp = nh[22] + nh[150],
    kp = nh[437] + nh[14] + nh[121] + nh[117], Mp = nh[437] + nh[14] + nh[121] + nh[118],
    Op = nh[747] + nh[1] + nh[438] + nh[59] + nh[755] + nh[228] + nh[229], Ip = nh[22] + nh[31] + nh[1] + nh[756],
    Sp = nh[757] + nh[37] + nh[586], Ap = nh[236], jp = nh[149], Pp = nh[333] + nh[181] + nh[182],
    Cp = nh[0] + nh[181] + nh[182], Lp = nh[758], Dp = nh[759], Rp = nh[163] + nh[44] + nh[760] + nh[271] + nh[272],
    Np = nh[761] + nh[22] + nh[363] + nh[22] + nh[762], Bp = nh[106] + nh[1] + nh[438] + nh[37] + nh[297],
    $p = nh[761] + nh[22] + nh[363] + nh[22] + nh[249], Fp = nh[249] + nh[22] + nh[761] + nh[22] + nh[371],
    Gp = nh[763] + nh[22] + nh[249] + nh[22] + nh[761],
    zp = nh[763] + nh[22] + nh[249] + nh[22] + nh[761] + nh[22] + nh[371],
    Hp = nh[763] + nh[22] + nh[762] + nh[22] + nh[761], Yp = nh[676], Up = nh[764], Wp = nh[673],
    qp = nh[765] + nh[22] + nh[766] + nh[22] + nh[392], Xp = nh[765] + nh[22] + nh[767] + nh[22] + nh[392],
    Vp = nh[765] + nh[22] + nh[768], Zp = nh[195] + nh[317] + nh[769], Kp = nh[24] + nh[200] + nh[37] + nh[770],
    Jp = nh[525] + nh[22] + nh[765] + nh[22] + nh[363], Qp = nh[446] + nh[37] + nh[771] + nh[59] + nh[440],
    tE = nh[446] + nh[37] + nh[771] + nh[14] + nh[121] + nh[118], iE = nh[456] + nh[22] + nh[363] + nh[22] + nh[737],
    nE = nh[446] + nh[59] + nh[772], eE = nh[446] + nh[1] + nh[438], sE = nh[773], rE = nh[446] + nh[114] + nh[299],
    hE = nh[773] + nh[37] + nh[210],
    aE = nh[63] + nh[64] + nh[218] + nh[65] + nh[218] + nh[65] + nh[218] + nh[65] + nh[218] + nh[66],
    oE = nh[177] + nh[1] + nh[438], fE = nh[177] + nh[271] + nh[272], cE = nh[22] + nh[177] + nh[271] + nh[272],
    uE = nh[199] + nh[42] + nh[774] + nh[1] + nh[438], _E = nh[199] + nh[53] + nh[284] + nh[1] + nh[434],
    dE = nh[199] + nh[53] + nh[284] + nh[435] + nh[436], lE = nh[22] + nh[775] + nh[37] + nh[168],
    vE = nh[22] + nh[776] + nh[24] + nh[777] + nh[108] + nh[109], bE = nh[22] + nh[776] + nh[24] + nh[777],
    yE = nh[778], gE = nh[779], xE = nh[780] + nh[71] + nh[781], pE = nh[782] + nh[71] + nh[781],
    EE = nh[243] + nh[22] + nh[244] + nh[22] + nh[783], mE = nh[784], wE = nh[604],
    TE = nh[243] + nh[22] + nh[244] + nh[22] + nh[785], kE = nh[786], ME = nh[787], OE = nh[733] + nh[44] + nh[788],
    IE = nh[67] + nh[206] + nh[1] + nh[188] + nh[59] + nh[77] + nh[53], SE = nh[67] + nh[789],
    AE = nh[67] + nh[790] + nh[28] + nh[259] + nh[59], jE = nh[67] + nh[791] + nh[59] + nh[792],
    PE = nh[67] + nh[791] + nh[40] + nh[793], CE = nh[67] + nh[794] + nh[1] + nh[795], LE = nh[67] + nh[796],
    DE = nh[67] + nh[206] + nh[53] + nh[188] + nh[1] + nh[77] + nh[42], RE = nh[67] + nh[797] + nh[59] + nh[218],
    NE = nh[67] + nh[798] + nh[799], BE = nh[67] + nh[206] + nh[42] + nh[188] + nh[800] + nh[75],
    $E = nh[67] + nh[791] + nh[40] + nh[801], FE = nh[67] + nh[802], GE = nh[67] + nh[75] + nh[660] + nh[259] + nh[803],
    zE = nh[67] + nh[804], HE = nh[67] + nh[805] + nh[53] + nh[795], YE = nh[67] + nh[806],
    UE = nh[67] + nh[206] + nh[42] + nh[807] + nh[40] + nh[223], WE = nh[67] + nh[808] + nh[809] + nh[75],
    qE = nh[67] + nh[810] + nh[811], XE = nh[67] + nh[206] + nh[28] + nh[188] + nh[812] + nh[218],
    VE = nh[67] + nh[813], ZE = nh[67] + nh[15] + nh[350] + nh[15] + nh[259] + nh[15] + nh[259],
    KE = nh[67] + nh[188] + nh[40] + nh[814], JE = nh[67] + nh[223] + nh[42] + nh[223] + nh[1] + nh[223] + nh[59],
    QE = nh[67] + nh[815], tm = nh[67] + nh[816], im = nh[67] + nh[188] + nh[42] + nh[188] + nh[28] + nh[188] + nh[42],
    nm = nh[67] + nh[223] + nh[1] + nh[817], em = nh[67] + nh[818],
    sm = nh[67] + nh[350] + nh[53] + nh[350] + nh[53] + nh[350] + nh[53], rm = nh[67] + nh[819], hm = nh[67] + nh[820],
    am = nh[67] + nh[821], om = nh[67] + nh[77] + nh[28] + nh[77] + nh[53] + nh[77] + nh[53],
    fm = nh[67] + nh[40] + nh[350] + nh[40] + nh[349] + nh[40] + nh[223],
    cm = nh[67] + nh[40] + nh[77] + nh[40] + nh[188] + nh[40] + nh[349],
    um = nh[67] + nh[40] + nh[350] + nh[40] + nh[223] + nh[40] + nh[325], _m = nh[67] + nh[822],
    dm = nh[67] + nh[28] + nh[77] + nh[823], lm = nh[67] + nh[77] + nh[824] + nh[218] + nh[764] + nh[218],
    vm = nh[67] + nh[508] + nh[77] + nh[825], bm = nh[67] + nh[325] + nh[660] + nh[325] + nh[764] + nh[826],
    ym = nh[67] + nh[59] + nh[75] + nh[827], gm = nh[67] + nh[75] + nh[28] + nh[259] + nh[828], xm = nh[67] + nh[829],
    pm = nh[67] + nh[510] + nh[349] + nh[510] + nh[349] + nh[510] + nh[188], Em = nh[414] + nh[92],
    mm = nh[22] + nh[76] + nh[77] + nh[673], wm = nh[22] + nh[76] + nh[77] + nh[756], Tm = nh[22] + nh[830],
    km = nh[22] + nh[207] + nh[218], Mm = nh[22] + nh[831] + nh[181] + nh[182], Om = nh[832],
    Im = nh[34] + nh[59] + nh[833], Sm = nh[34] + nh[59] + nh[834],
    Am = nh[181] + nh[326] + nh[47] + nh[835] + nh[47] + nh[836] + nh[71], jm = nh[34], Pm = nh[837],
    Cm = nh[317] + nh[838] + nh[47] + nh[764] + nh[47] + nh[837] + nh[47] + nh[839] + nh[71],
    Lm = nh[34] + nh[94] + nh[840], Dm = nh[841] + nh[42] + nh[274], Rm = nh[842], Nm = nh[780] + nh[102] + nh[843],
    Bm = nh[841] + nh[37] + nh[168], $m = nh[844] + nh[1] + nh[438],
    Fm = nh[775] + nh[40] + nh[845] + nh[102] + nh[159], Gm = nh[846], zm = nh[483] + nh[181] + nh[847], Hm = nh[848],
    Ym = nh[849] + nh[443] + nh[850], Um = nh[849] + nh[53] + nh[183], Wm = nh[851], qm = nh[852], Xm = nh[853],
    Vm = nh[854], Zm = nh[855] + nh[1] + nh[229], Km = nh[856], Jm = nh[178] + nh[114] + nh[299], Qm = nh[857],
    tw = nh[858], iw = nh[859], nw = nh[860], ew = nh[123] + nh[24] + nh[78], sw = nh[861],
    rw = nh[292] + nh[37] + nh[168], hw = nh[862] + nh[228] + nh[632] + nh[1] + nh[229] + nh[37] + nh[168],
    aw = nh[863], ow = nh[178], fw = nh[864], cw = nh[94] + nh[865] + nh[47] + nh[866] + nh[515] + nh[218] + nh[81],
    uw = nh[867], _w = nh[567], dw = nh[842] + nh[130] + nh[131], lw = nh[868] + nh[130] + nh[131],
    vw = nh[869] + nh[1] + nh[229], bw = nh[176] + nh[32] + nh[870] + nh[237], yw = nh[871],
    gw = nh[176] + nh[24] + nh[872] + nh[47] + nh[53] + nh[873] + nh[237], xw = nh[874] + nh[53] + nh[873],
    pw = nh[875], Ew = nh[876],
    mw = nh[510] + nh[218] + nh[15] + nh[259] + nh[877] + nh[878] + nh[879] + nh[880] + nh[881] + nh[882] + nh[510] + nh[883] + nh[510] + nh[218] + nh[166] + nh[884] + nh[764] + nh[885] + nh[886] + nh[887] + nh[15] + nh[77] + nh[15] + nh[259] + nh[166] + nh[888] + nh[889] + nh[890] + nh[510] + nh[891] + nh[166] + nh[892] + nh[660] + nh[206] + nh[15] + nh[206] + nh[15] + nh[893] + nh[764] + nh[218] + nh[660] + nh[894] + nh[660] + nh[895] + nh[896] + nh[792] + nh[660] + nh[897] + nh[508] + nh[898] + nh[899] + nh[900] + nh[764] + nh[901] + nh[166] + nh[902] + nh[903] + nh[904] + nh[508] + nh[325] + nh[764] + nh[905] + nh[510] + nh[325] + nh[906] + nh[907] + nh[764] + nh[908] + nh[166] + nh[909],
    ww = nh[176] + nh[44] + nh[910] + nh[47] + nh[93] + nh[515], Tw = nh[911], kw = nh[912], Mw = nh[913],
    Ow = nh[167] + nh[114], Iw = nh[914], Sw = nh[350] + nh[71], Aw = nh[915], jw = nh[78], Pw = nh[916],
    Cw = nh[1] + nh[314], Lw = nh[102] + nh[917], Dw = nh[1] + nh[918], Rw = nh[919], Nw = nh[920], Bw = nh[163],
    $w = nh[921], Fw = nh[922], Gw = nh[923], zw = nh[924], Hw = nh[925], Yw = nh[926], Uw = nh[485], Ww = nh[927],
    qw = nh[928], Xw = nh[414] + nh[697], Vw = nh[47] + nh[294] + nh[47] + nh[929] + nh[349], Zw = nh[930],
    Kw = nh[931], Jw = nh[932] + nh[108] + nh[933], Qw = nh[934], tT = nh[935] + nh[71] + nh[936] + nh[71] + nh[206],
    iT = nh[37] + nh[85], nT = nh[937], eT = nh[28] + nh[164], sT = nh[318], rT = nh[938],
    hT = nh[939] + nh[108] + nh[933], aT = nh[940], oT = nh[178] + nh[75] + nh[53], fT = nh[507] + nh[114] + nh[178],
    cT = nh[941], uT = nh[218] + nh[47] + nh[218], _T = nh[414] + nh[92] + nh[1] + nh[314],
    dT = nh[414] + nh[92] + nh[1] + nh[314] + nh[24] + nh[615], lT = nh[414] + nh[92] + nh[271] + nh[415],
    vT = nh[22] + nh[942] + nh[349], bT = nh[22] + nh[76] + nh[77] + nh[507], yT = nh[22] + nh[943] + nh[44] + nh[45],
    gT = nh[22] + nh[944], xT = nh[22] + nh[945], pT = nh[167] + nh[24] + nh[946], ET = nh[22] + nh[538] + nh[947],
    mT = nh[265], wT = nh[22] + nh[536], TT = nh[948], kT = nh[944], MT = nh[22] + nh[949] + nh[694],
    OT = nh[22] + nh[76] + nh[77] + nh[166], IT = nh[22] + nh[950], ST = nh[230] + nh[102] + nh[951],
    AT = nh[141] + nh[952], jT = nh[22] + nh[953], PT = nh[22] + nh[954], CT = nh[506],
    LT = nh[673] + nh[181] + nh[222], DT = nh[747], RT = nh[22] + nh[76] + nh[77] + nh[955], NT = nh[22] + nh[956],
    BT = nh[22] + nh[536] + nh[1] + nh[314] + nh[37] + nh[168] + nh[42] + nh[274], $T = nh[93] + nh[138],
    FT = nh[957] + nh[228] + nh[958], GT = nh[959], zT = nh[960],
    HT = nh[141] + nh[76] + nh[77] + nh[961] + nh[228] + nh[958], YT = nh[960] + nh[64],
    UT = nh[22] + nh[76] + nh[77] + nh[570] + nh[228] + nh[958], WT = nh[962], qT = nh[963],
    XT = nh[3] + nh[28] + nh[164] + nh[59] + nh[82] + nh[181] + nh[166], VT = nh[22] + nh[964],
    ZT = nh[965] + nh[28] + nh[164], KT = nh[966] + nh[181] + nh[222], JT = nh[22] + nh[967],
    QT = nh[93] + nh[44] + nh[968], tk = nh[455] + nh[181] + nh[166], ik = nh[141] + nh[969],
    nk = nh[22] + nh[536] + nh[59] + nh[226] + nh[42] + nh[274], ek = nh[22] + nh[76] + nh[970], sk = nh[288],
    rk = nh[294] + nh[28] + nh[295] + nh[102] + nh[453], hk = nh[22] + nh[971] + nh[1] + nh[314],
    ak = nh[3] + nh[271] + nh[972] + nh[59] + nh[226], ok = nh[22] + nh[944] + nh[1] + nh[973],
    fk = nh[165] + nh[59] + nh[226], ck = nh[22] + nh[93] + nh[181] + nh[974] + nh[102] + nh[112],
    uk = nh[22] + nh[76] + nh[77] + nh[947], _k = nh[432] + nh[22] + nh[975], dk = nh[976] + nh[71] + nh[609],
    lk = nh[432] + nh[22] + nh[977], vk = nh[976] + nh[71] + nh[978], bk = nh[432] + nh[22] + nh[363],
    yk = nh[22] + nh[979], gk = nh[22] + nh[297] + nh[1] + nh[314], xk = nh[22] + nh[980],
    pk = nh[22] + nh[223] + nh[239], Ek = nh[22] + nh[952] + nh[42] + nh[981],
    mk = nh[195] + nh[59] + nh[322] + nh[28] + nh[408], wk = nh[22] + nh[982], Tk = nh[22] + nh[325] + nh[764],
    kk = nh[22] + nh[944] + nh[1] + nh[489], Mk = nh[22] + nh[983] + nh[40] + nh[984],
    Ok = nh[983] + nh[40] + nh[239] + nh[1] + nh[985], Ik = nh[215] + nh[114] + nh[161],
    Sk = nh[265] + nh[32] + nh[986], Ak = nh[22] + nh[15] + nh[223], jk = nh[22] + nh[987] + nh[1] + nh[489],
    Pk = nh[988] + nh[71] + nh[192], Ck = nh[141] + nh[223] + nh[641], Lk = nh[747] + nh[37] + nh[586],
    Dk = nh[93] + nh[1] + nh[314], Rk = nh[989], Nk = nh[265] + nh[102] + nh[990], Bk = nh[455] + nh[1] + nh[991],
    $k = nh[992] + nh[37] + nh[993] + nh[317] + nh[994], Fk = nh[995], Gk = nh[36] + nh[24] + nh[25] + nh[1] + nh[534],
    zk = nh[265] + nh[53] + nh[183], Hk = nh[22] + nh[76] + nh[996],
    Yk = nh[22] + nh[354] + nh[59] + nh[322] + nh[181] + nh[273] + nh[42] + nh[274], Uk = nh[22] + nh[997],
    Wk = nh[191] + nh[28] + nh[321] + nh[59] + nh[322],
    qk = nh[294] + nh[28] + nh[295] + nh[59] + nh[82] + nh[53] + nh[998] + nh[42] + nh[999], Xk = nh[141] + nh[1e3],
    Vk = nh[3] + nh[181] + nh[222] + nh[59] + nh[82] + nh[181] + nh[166], Zk = nh[394] + nh[228] + nh[447],
    Kk = nh[395] + nh[539], Jk = nh[134] + nh[491] + nh[47] + nh[1001], Qk = nh[944] + nh[59] + nh[226],
    tM = nh[22] + nh[1002] + nh[228] + nh[447], iM = nh[22] + nh[971] + nh[228],
    nM = nh[22] + nh[212] + nh[59] + nh[226], eM = nh[26] + nh[30], sM = nh[0] + nh[28] + nh[321], rM = nh[455],
    hM = nh[22] + nh[1003], aM = nh[360] + nh[395] + nh[947], oM = nh[11] + nh[395],
    fM = nh[414] + nh[71] + nh[28] + nh[164], cM = nh[1004], uM = nh[296] + nh[37] + nh[1005], _M = nh[1006],
    dM = nh[414] + nh[71] + nh[28] + nh[321], lM = nh[22] + nh[76] + nh[77] + nh[510], vM = nh[1007],
    bM = nh[407] + nh[28] + nh[408], yM = nh[398] + nh[71] + nh[1008],
    gM = nh[1009] + nh[24] + nh[200] + nh[1] + nh[534], xM = nh[106] + nh[24] + nh[200] + nh[37] + nh[770],
    pM = nh[1010], EM = nh[93], mM = nh[26] + nh[236], wM = nh[414] + nh[92] + nh[497], TM = nh[26] + nh[1011],
    kM = nh[1012] + nh[37] + nh[1013], MM = nh[36] + nh[24] + nh[946] + nh[1] + nh[489],
    OM = nh[22] + nh[76] + nh[77] + nh[15], IM = nh[1011], SM = nh[262], AM = nh[0] + nh[42] + nh[1014],
    jM = nh[93] + nh[42] + nh[1014], PM = nh[22] + nh[262], CM = nh[414] + nh[71] + nh[317] + nh[229],
    LM = nh[1015] + nh[71] + nh[106], DM = nh[1015] + nh[71] + nh[11], RM = nh[268] + nh[24] + nh[269],
    NM = nh[1016] + nh[22] + nh[1017], BM = nh[414] + nh[71] + nh[37] + nh[277] + nh[317] + nh[229],
    $M = nh[11] + nh[24] + nh[200] + nh[37] + nh[770], FM = nh[1018], GM = nh[414] + nh[71] + nh[59] + nh[1019],
    zM = nh[477] + nh[37] + nh[993] + nh[317] + nh[994], HM = nh[1020] + nh[22] + nh[363],
    YM = nh[1020] + nh[22] + nh[363] + nh[22] + nh[336], UM = nh[1020] + nh[22] + nh[1021],
    WM = nh[1020] + nh[22] + nh[1022] + nh[22] + nh[170], qM = nh[265] + nh[42] + nh[274], XM = nh[1023],
    VM = nh[1020] + nh[22] + nh[1024], ZM = nh[414] + nh[71] + nh[271] + nh[1025], KM = nh[1026] + nh[37] + nh[168],
    JM = nh[1023] + nh[114] + nh[299], QM = nh[1023] + nh[181] + nh[182], tO = nh[271] + nh[1025],
    iO = nh[414] + nh[71] + nh[114] + nh[178], nO = nh[114] + nh[178], eO = nh[67] + nh[1027],
    sO = nh[26] + nh[268] + nh[24] + nh[152], rO = nh[446] + nh[37] + nh[771] + nh[14] + nh[121] + nh[117],
    hO = nh[446] + nh[59] + nh[1028] + nh[1] + nh[438], aO = nh[177] + nh[102] + nh[112],
    oO = nh[1029] + nh[37] + nh[1030], fO = nh[1029] + nh[37] + nh[1030] + nh[37] + nh[1005], cO = nh[26] + nh[1031],
    uO = nh[264] + nh[117], _O = nh[264] + nh[118], dO = nh[280] + nh[1] + nh[438],
    lO = nh[280] + nh[44] + nh[285] + nh[53] + nh[284],
    vO = nh[280] + nh[44] + nh[285] + nh[53] + nh[284] + nh[14] + nh[121], bO = nh[26] + nh[275] + nh[1] + nh[438],
    yO = nh[26] + nh[275] + nh[271] + nh[272], gO = nh[26] + nh[265] + nh[53] + nh[183], xO = nh[180],
    pO = nh[191] + nh[37] + nh[168], EO = nh[36] + nh[59] + nh[226] + nh[1] + nh[489],
    mO = nh[26] + nh[265] + nh[44] + nh[238], wO = nh[26] + nh[265] + nh[32] + nh[986], TO = nh[26] + nh[963],
    kO = nh[26] + nh[1032] + nh[28] + nh[283], MO = nh[1012] + nh[443] + nh[1033] + nh[114] + nh[403],
    OO = nh[36] + nh[53] + nh[183] + nh[1] + nh[489], IO = nh[22] + nh[1034], SO = nh[32] + nh[986],
    AO = nh[44] + nh[238], jO = nh[40] + nh[258] + nh[24] + nh[152], PO = nh[37] + nh[168],
    CO = nh[59] + nh[1028] + nh[271] + nh[272], LO = nh[102] + nh[263], DO = nh[3] + nh[24] + nh[25],
    RO = nh[446] + nh[71] + nh[780], NO = nh[446] + nh[71] + nh[280],
    BO = nh[456] + nh[22] + nh[736] + nh[22] + nh[391] + nh[22] + nh[117], $O = nh[446] + nh[71] + nh[202],
    FO = nh[1035] + nh[22] + nh[247], GO = nh[747] + nh[71] + nh[780],
    zO = nh[1035] + nh[22] + nh[247] + nh[22] + nh[243] + nh[22] + nh[244], HO = nh[1036], YO = nh[1031],
    UO = nh[736] + nh[22] + nh[738], WO = nh[437] + nh[71] + nh[1037], qO = nh[736] + nh[22] + nh[247],
    XO = nh[437] + nh[71] + nh[780], VO = nh[437] + nh[71] + nh[264] + nh[71] + nh[81],
    ZO = nh[437] + nh[71] + nh[264] + nh[71] + nh[82], KO = nh[334] + nh[22] + nh[1038],
    JO = nh[1039] + nh[71] + nh[197], QO = nh[334] + nh[22] + nh[1038] + nh[22] + nh[366],
    tI = nh[1039] + nh[71] + nh[197] + nh[71] + nh[97],
    iI = nh[334] + nh[22] + nh[728] + nh[22] + nh[1040] + nh[22] + nh[247],
    nI = nh[1039] + nh[71] + nh[199] + nh[71] + nh[1041],
    eI = nh[334] + nh[22] + nh[728] + nh[22] + nh[1042] + nh[22] + nh[391],
    sI = nh[334] + nh[22] + nh[1040] + nh[22] + nh[247], rI = nh[1039] + nh[71] + nh[177] + nh[71] + nh[780],
    hI = nh[1039] + nh[71] + nh[177] + nh[71] + nh[1043], aI = nh[334] + nh[22] + nh[1044],
    oI = nh[1039] + nh[71] + nh[773], fI = nh[334] + nh[22] + nh[1044] + nh[22] + nh[366],
    cI = nh[1039] + nh[71] + nh[773] + nh[71] + nh[97], uI = nh[1045] + nh[22] + nh[375] + nh[22] + nh[1046],
    _I = nh[257] + nh[71] + nh[86] + nh[71] + nh[398], dI = nh[275] + nh[71] + nh[780],
    lI = nh[1047] + nh[22] + nh[761], vI = nh[275] + nh[71] + nh[1043], bI = nh[737], yI = nh[280] + nh[71] + nh[83],
    gI = nh[737] + nh[22] + nh[247], xI = nh[280] + nh[71] + nh[780],
    pI = nh[737] + nh[22] + nh[728] + nh[22] + nh[1042], EI = nh[280] + nh[71] + nh[199] + nh[71] + nh[1041],
    mI = nh[737] + nh[22] + nh[728] + nh[22] + nh[1042] + nh[22] + nh[391], wI = nh[280] + nh[71] + nh[513],
    TI = nh[1021], kI = nh[199] + nh[71] + nh[1048], MI = nh[728] + nh[22] + nh[1049], OI = nh[199] + nh[71] + nh[304],
    II = nh[728] + nh[22] + nh[1042] + nh[22] + nh[729], SI = nh[199] + nh[71] + nh[1041] + nh[71] + nh[1048],
    AI = nh[728] + nh[22] + nh[1042] + nh[22] + nh[1049], jI = nh[199] + nh[71] + nh[1041] + nh[71] + nh[304],
    PI = nh[224] + nh[22] + nh[1047] + nh[22] + nh[247], CI = nh[224] + nh[22] + nh[737],
    LI = nh[236] + nh[71] + nh[280] + nh[71] + nh[83], DI = nh[224] + nh[22] + nh[737] + nh[22] + nh[247],
    RI = nh[236] + nh[71] + nh[280] + nh[71] + nh[97],
    NI = nh[224] + nh[22] + nh[737] + nh[22] + nh[728] + nh[22] + nh[1042],
    BI = nh[224] + nh[22] + nh[737] + nh[22] + nh[728] + nh[22] + nh[1042] + nh[22] + nh[391],
    $I = nh[224] + nh[22] + nh[739], FI = nh[224] + nh[22] + nh[737] + nh[22] + nh[739],
    GI = nh[236] + nh[71] + nh[513], zI = nh[224] + nh[22] + nh[1021], HI = nh[236] + nh[71] + nh[232],
    YI = nh[224] + nh[22] + nh[652] + nh[22] + nh[527], UI = nh[236] + nh[71] + nh[673] + nh[71] + nh[13],
    WI = nh[224] + nh[22] + nh[1050], qI = nh[236] + nh[71] + nh[1051], XI = nh[224] + nh[22] + nh[1036],
    VI = nh[236] + nh[71] + nh[1031], ZI = nh[1052] + nh[22] + nh[1053], KI = nh[856] + nh[71] + nh[203],
    JI = nh[1052] + nh[22] + nh[1054], QI = nh[856] + nh[71] + nh[282], tS = nh[856] + nh[71] + nh[963],
    iS = nh[1052] + nh[22] + nh[247], nS = nh[856] + nh[71] + nh[780],
    eS = nh[856] + nh[71] + nh[458] + nh[71] + nh[948], sS = nh[856] + nh[71] + nh[458] + nh[71] + nh[1055],
    rS = nh[1052] + nh[22] + nh[169] + nh[22] + nh[366], hS = nh[856] + nh[71] + nh[458] + nh[71] + nh[97],
    aS = nh[1052] + nh[22] + nh[1021], oS = nh[856] + nh[71] + nh[232],
    fS = nh[1052] + nh[22] + nh[740] + nh[22] + nh[741], cS = nh[856] + nh[71] + nh[267] + nh[71] + nh[83],
    uS = nh[856] + nh[71] + nh[267], _S = nh[1052] + nh[22] + nh[739], dS = nh[856] + nh[71] + nh[513],
    lS = nh[856] + nh[71] + nh[264] + nh[71] + nh[81], vS = nh[1052] + nh[22] + nh[391] + nh[22] + nh[118],
    bS = nh[856] + nh[71] + nh[264] + nh[71] + nh[82], yS = nh[1052] + nh[22] + nh[170],
    gS = nh[856] + nh[71] + nh[948], xS = nh[1052] + nh[22] + nh[1056] + nh[22] + nh[1054],
    pS = nh[856] + nh[71] + nh[1057] + nh[71] + nh[282], ES = nh[1052] + nh[22] + nh[737],
    mS = nh[856] + nh[71] + nh[280], wS = nh[1052] + nh[22] + nh[737] + nh[22] + nh[366],
    TS = nh[856] + nh[71] + nh[280] + nh[71] + nh[97], kS = nh[1052] + nh[22] + nh[1047] + nh[22] + nh[247],
    MS = nh[1052] + nh[22] + nh[1047] + nh[22] + nh[761], OS = nh[1052] + nh[22] + nh[1058],
    IS = nh[856] + nh[71] + nh[260], SS = nh[856] + nh[71] + nh[437] + nh[71] + nh[1037],
    AS = nh[1052] + nh[22] + nh[736] + nh[22] + nh[247], jS = nh[856] + nh[71] + nh[437] + nh[71] + nh[780],
    PS = nh[1052] + nh[22] + nh[736] + nh[22] + nh[391] + nh[22] + nh[117],
    CS = nh[856] + nh[71] + nh[673] + nh[71] + nh[13], LS = nh[1052] + nh[22] + nh[1059] + nh[22] + nh[377],
    DS = nh[856] + nh[71] + nh[36] + nh[71] + nh[297], RS = nh[1020] + nh[22] + nh[1038],
    NS = nh[1023] + nh[71] + nh[197], BS = nh[1020] + nh[22] + nh[1038] + nh[22] + nh[366],
    $S = nh[1023] + nh[71] + nh[197] + nh[71] + nh[780],
    FS = nh[1020] + nh[22] + nh[1038] + nh[22] + nh[728] + nh[22] + nh[1042],
    GS = nh[1020] + nh[22] + nh[1038] + nh[22] + nh[728] + nh[22] + nh[1042] + nh[22] + nh[391],
    zS = nh[367] + nh[22] + nh[387] + nh[22] + nh[1052] + nh[22] + nh[1053],
    HS = nh[367] + nh[22] + nh[387] + nh[22] + nh[1052] + nh[22] + nh[1060] + nh[22] + nh[1054],
    YS = nh[367] + nh[22] + nh[387] + nh[22] + nh[1052] + nh[22] + nh[169] + nh[22] + nh[170],
    US = nh[367] + nh[22] + nh[387] + nh[22] + nh[1052] + nh[22] + nh[169] + nh[22] + nh[490],
    WS = nh[367] + nh[22] + nh[387] + nh[22] + nh[1052] + nh[22] + nh[169] + nh[22] + nh[366],
    qS = nh[367] + nh[22] + nh[387] + nh[22] + nh[1052] + nh[22] + nh[1021],
    XS = nh[367] + nh[22] + nh[387] + nh[22] + nh[1052] + nh[22] + nh[739],
    VS = nh[367] + nh[22] + nh[387] + nh[22] + nh[1052] + nh[22] + nh[391] + nh[22] + nh[118],
    ZS = nh[367] + nh[22] + nh[387] + nh[22] + nh[1052] + nh[22] + nh[737],
    KS = nh[367] + nh[22] + nh[387] + nh[22] + nh[1052] + nh[22] + nh[737] + nh[22] + nh[366],
    JS = nh[354] + nh[71] + nh[83], QS = nh[367] + nh[22] + nh[247], tA = nh[354] + nh[71] + nh[780],
    iA = nh[354] + nh[71] + nh[773], nA = nh[367] + nh[22] + nh[1044] + nh[22] + nh[366],
    eA = nh[354] + nh[71] + nh[773] + nh[71] + nh[97], sA = nh[354] + nh[71] + nh[199] + nh[71] + nh[1041],
    rA = nh[354] + nh[71] + nh[84] + nh[71] + nh[264], hA = nh[354] + nh[71] + nh[93] + nh[71] + nh[264],
    aA = nh[367] + nh[22] + nh[1040] + nh[22] + nh[247], oA = nh[354] + nh[71] + nh[177] + nh[71] + nh[780],
    fA = nh[354] + nh[71] + nh[407] + nh[71] + nh[202], cA = nh[354] + nh[71] + nh[407] + nh[71] + nh[1061],
    uA = nh[354] + nh[71] + nh[1062] + nh[71] + nh[1063], _A = nh[354] + nh[71] + nh[709],
    dA = nh[354] + nh[71] + nh[1064] + nh[71] + nh[1065], lA = nh[367] + nh[22] + nh[374] + nh[22] + nh[376],
    vA = nh[354] + nh[71] + nh[46] + nh[71] + nh[1066], bA = nh[367] + nh[22] + nh[374] + nh[22] + nh[502],
    yA = nh[354] + nh[71] + nh[46] + nh[71] + nh[356], gA = nh[367] + nh[22] + nh[382],
    xA = nh[354] + nh[71] + nh[1067], pA = nh[354] + nh[71] + nh[1067] + nh[71] + nh[513],
    EA = nh[367] + nh[22] + nh[390] + nh[22] + nh[1068] + nh[22] + nh[367],
    mA = nh[354] + nh[71] + nh[84] + nh[71] + nh[1069] + nh[71] + nh[354],
    wA = nh[367] + nh[22] + nh[392] + nh[22] + nh[1068] + nh[22] + nh[367],
    TA = nh[354] + nh[71] + nh[93] + nh[71] + nh[1069] + nh[71] + nh[354],
    kA = nh[367] + nh[22] + nh[390] + nh[22] + nh[393], MA = nh[354] + nh[71] + nh[84] + nh[71] + nh[1070],
    OA = nh[354] + nh[71] + nh[93] + nh[71] + nh[1070], IA = nh[347] + nh[22] + nh[390], SA = nh[726] + nh[71] + nh[84],
    AA = nh[347] + nh[22] + nh[390] + nh[22] + nh[170], jA = nh[726] + nh[71] + nh[84] + nh[71] + nh[948],
    PA = nh[726] + nh[71] + nh[84] + nh[71] + nh[264], CA = nh[726] + nh[71] + nh[84] + nh[71] + nh[197],
    LA = nh[347] + nh[22] + nh[390] + nh[22] + nh[1038] + nh[22] + nh[366],
    DA = nh[347] + nh[22] + nh[390] + nh[22] + nh[1044], RA = nh[726] + nh[71] + nh[84] + nh[71] + nh[773],
    NA = nh[347] + nh[22] + nh[390] + nh[22] + nh[1044] + nh[22] + nh[366],
    BA = nh[347] + nh[22] + nh[390] + nh[22] + nh[728] + nh[22] + nh[1042],
    $A = nh[726] + nh[71] + nh[84] + nh[71] + nh[199] + nh[71] + nh[1041],
    FA = nh[347] + nh[22] + nh[390] + nh[22] + nh[728] + nh[22] + nh[1042] + nh[22] + nh[391],
    GA = nh[347] + nh[22] + nh[390] + nh[22] + nh[1040] + nh[22] + nh[761],
    zA = nh[347] + nh[22] + nh[390] + nh[22] + nh[728] + nh[22] + nh[729],
    HA = nh[726] + nh[71] + nh[84] + nh[71] + nh[199] + nh[71] + nh[1048],
    YA = nh[347] + nh[22] + nh[390] + nh[22] + nh[728] + nh[22] + nh[1049],
    UA = nh[726] + nh[71] + nh[84] + nh[71] + nh[199] + nh[71] + nh[304], WA = nh[347] + nh[22] + nh[392],
    qA = nh[726] + nh[71] + nh[93], XA = nh[347] + nh[22] + nh[392] + nh[22] + nh[170],
    VA = nh[726] + nh[71] + nh[93] + nh[71] + nh[948], ZA = nh[347] + nh[22] + nh[392] + nh[22] + nh[391],
    KA = nh[726] + nh[71] + nh[93] + nh[71] + nh[264], JA = nh[726] + nh[71] + nh[93] + nh[71] + nh[197],
    QA = nh[347] + nh[22] + nh[392] + nh[22] + nh[1038] + nh[22] + nh[366],
    tj = nh[347] + nh[22] + nh[392] + nh[22] + nh[1044], ij = nh[726] + nh[71] + nh[93] + nh[71] + nh[773],
    nj = nh[347] + nh[22] + nh[392] + nh[22] + nh[728] + nh[22] + nh[1042],
    ej = nh[726] + nh[71] + nh[93] + nh[71] + nh[199] + nh[71] + nh[1041],
    sj = nh[347] + nh[22] + nh[392] + nh[22] + nh[728] + nh[22] + nh[1042] + nh[22] + nh[391],
    rj = nh[347] + nh[22] + nh[392] + nh[22] + nh[1040] + nh[22] + nh[247],
    hj = nh[726] + nh[71] + nh[93] + nh[71] + nh[177] + nh[71] + nh[780],
    aj = nh[347] + nh[22] + nh[392] + nh[22] + nh[728] + nh[22] + nh[729],
    oj = nh[726] + nh[71] + nh[93] + nh[71] + nh[199] + nh[71] + nh[1048],
    fj = nh[347] + nh[22] + nh[392] + nh[22] + nh[728] + nh[22] + nh[1049],
    cj = nh[726] + nh[71] + nh[93] + nh[71] + nh[199] + nh[71] + nh[304],
    uj = nh[1052] + nh[22] + nh[1060] + nh[22] + nh[1054], _j = nh[780], dj = nh[275] + nh[1] + nh[438],
    lj = nh[1032] + nh[14] + nh[76] + nh[114] + nh[1071], vj = nh[736] + nh[22] + nh[391] + nh[22] + nh[117],
    bj = nh[736] + nh[22] + nh[391] + nh[22] + nh[118], yj = nh[1052] + nh[22] + nh[169] + nh[22] + nh[490],
    gj = nh[458] + nh[37] + nh[210], xj = nh[1057] + nh[24] + nh[269], pj = nh[267] + nh[108] + nh[109],
    Ej = nh[1052] + nh[22] + nh[740], mj = nh[1032] + nh[24] + nh[279], wj = nh[280] + nh[102] + nh[281],
    Tj = nh[1052] + nh[22] + nh[391] + nh[22] + nh[117], kj = nh[260],
    Mj = nh[1052] + nh[22] + nh[736] + nh[22] + nh[738], Oj = nh[1052] + nh[22] + nh[652] + nh[22] + nh[527],
    Ij = nh[22] + nh[75] + nh[647], Sj = nh[22] + nh[76] + nh[1072], Aj = nh[1051] + nh[114] + nh[299],
    jj = nh[334] + nh[22] + nh[1040] + nh[22] + nh[761], Pj = nh[334] + nh[22] + nh[728] + nh[22] + nh[1042],
    Cj = nh[728] + nh[22] + nh[729], Lj = nh[224] + nh[22] + nh[1047] + nh[22] + nh[761],
    Dj = nh[1073] + nh[59] + nh[1074], Rj = nh[22] + nh[223] + nh[82],
    Nj = nh[1020] + nh[22] + nh[1047] + nh[22] + nh[247], Bj = nh[1039],
    $j = nh[1020] + nh[22] + nh[1047] + nh[22] + nh[761], Fj = nh[22] + nh[223] + nh[641],
    Gj = nh[367] + nh[22] + nh[741], zj = nh[84] + nh[40] + nh[1075], Hj = nh[93] + nh[40] + nh[1075],
    Yj = nh[84] + nh[40] + nh[239] + nh[28] + nh[321], Uj = nh[367] + nh[22] + nh[1044],
    Wj = nh[367] + nh[22] + nh[728] + nh[22] + nh[1042],
    qj = nh[367] + nh[22] + nh[728] + nh[22] + nh[1042] + nh[22] + nh[391],
    Xj = nh[84] + nh[40] + nh[1075] + nh[37] + nh[168], Vj = nh[84] + nh[40] + nh[1075] + nh[14] + nh[121],
    Zj = nh[347] + nh[22] + nh[390] + nh[22] + nh[1038], Kj = nh[84] + nh[40] + nh[1075] + nh[37] + nh[196],
    Jj = nh[84] + nh[40] + nh[1075] + nh[37] + nh[196] + nh[37] + nh[210],
    Qj = nh[84] + nh[40] + nh[1075] + nh[14] + nh[1076],
    tP = nh[347] + nh[22] + nh[390] + nh[22] + nh[1040] + nh[22] + nh[247],
    iP = nh[84] + nh[40] + nh[1075] + nh[42] + nh[774] + nh[1] + nh[438],
    nP = nh[84] + nh[40] + nh[1075] + nh[44] + nh[285] + nh[53] + nh[284],
    eP = nh[84] + nh[40] + nh[1075] + nh[44] + nh[285] + nh[435] + nh[436],
    sP = nh[84] + nh[40] + nh[1075] + nh[44] + nh[285] + nh[1] + nh[434],
    rP = nh[93] + nh[40] + nh[1075] + nh[37] + nh[168], hP = nh[93] + nh[40] + nh[1075] + nh[14] + nh[121],
    aP = nh[93] + nh[40] + nh[1075] + nh[37] + nh[196],
    oP = nh[93] + nh[40] + nh[1075] + nh[37] + nh[196] + nh[37] + nh[210],
    fP = nh[93] + nh[40] + nh[1075] + nh[14] + nh[1076],
    cP = nh[347] + nh[22] + nh[392] + nh[22] + nh[1044] + nh[22] + nh[366],
    uP = nh[93] + nh[40] + nh[1075] + nh[14] + nh[1076] + nh[37] + nh[210],
    _P = nh[93] + nh[40] + nh[1075] + nh[42] + nh[774] + nh[1] + nh[438],
    dP = nh[347] + nh[22] + nh[392] + nh[22] + nh[1040] + nh[22] + nh[761],
    lP = nh[93] + nh[40] + nh[1075] + nh[42] + nh[774] + nh[271] + nh[272],
    vP = nh[93] + nh[40] + nh[1075] + nh[44] + nh[285] + nh[53] + nh[284],
    bP = nh[93] + nh[40] + nh[1075] + nh[44] + nh[285] + nh[435] + nh[436],
    yP = nh[93] + nh[40] + nh[1075] + nh[44] + nh[285] + nh[1] + nh[434], gP = nh[407] + nh[44] + nh[1077],
    xP = nh[367] + nh[22] + nh[387] + nh[22] + nh[1052] + nh[22] + nh[740] + nh[22] + nh[741],
    pP = nh[367] + nh[22] + nh[387] + nh[22] + nh[1052] + nh[22] + nh[740],
    EP = nh[367] + nh[22] + nh[387] + nh[22] + nh[1052] + nh[22] + nh[1047] + nh[22] + nh[247],
    mP = nh[265] + nh[37] + nh[277], wP = nh[347] + nh[22] + nh[392] + nh[22] + nh[1038], TP = nh[22] + nh[1078],
    kP = nh[265] + nh[37] + nh[168], MP = nh[26] + nh[265] + nh[1] + nh[88], OP = nh[11] + nh[59] + nh[1079],
    IP = nh[36] + nh[59] + nh[1079] + nh[24] + nh[25] + nh[1] + nh[534], SP = nh[445] + nh[44] + nh[1077],
    AP = nh[445] + nh[59] + nh[1079] + nh[24] + nh[70], jP = nh[360] + nh[24] + nh[70],
    PP = nh[106] + nh[59] + nh[1079], CP = nh[106] + nh[1] + nh[88], LP = nh[1012] + nh[32] + nh[1080],
    DP = nh[191] + nh[1] + nh[2], RP = nh[26] + nh[446] + nh[59] + nh[772],
    NP = nh[26] + nh[437] + nh[14] + nh[121] + nh[118],
    BP = nh[26] + nh[446] + nh[37] + nh[771] + nh[14] + nh[121] + nh[118],
    $P = nh[26] + nh[446] + nh[37] + nh[771] + nh[59] + nh[440], FP = nh[395] + nh[22] + nh[1081] + nh[22] + nh[1082],
    GP = nh[26] + nh[265] + nh[59] + nh[226], zP = nh[753] + nh[1] + nh[489], HP = nh[26] + nh[747] + nh[1] + nh[438],
    YP = nh[26] + nh[747] + nh[1] + nh[438] + nh[59] + nh[755] + nh[228] + nh[229],
    UP = nh[26] + nh[437] + nh[1] + nh[438], WP = nh[26] + nh[437] + nh[59] + nh[440],
    qP = nh[26] + nh[437] + nh[14] + nh[121] + nh[117], XP = nh[106] + nh[102] + nh[112],
    VP = nh[402] + nh[114] + nh[403] + nh[1] + nh[2], ZP = nh[776], KP = nh[22] + nh[76] + nh[77] + nh[667],
    JP = nh[22] + nh[1083], QP = nh[67] + nh[1084], tC = nh[26] + nh[948], iC = nh[26] + nh[265] + nh[37] + nh[586],
    nC = nh[167] + nh[228] + nh[1085] + nh[59] + nh[226], eC = nh[22] + nh[776] + nh[59] + nh[226],
    sC = nh[26] + nh[265] + nh[42] + nh[774] + nh[271] + nh[272], rC = nh[26] + nh[177] + nh[271] + nh[272],
    hC = nh[26] + nh[1051] + nh[114] + nh[299], aC = nh[26] + nh[199] + nh[108] + nh[109],
    oC = nh[26] + nh[177] + nh[1] + nh[438], fC = nh[37] + nh[586], cC = nh[42] + nh[774] + nh[271] + nh[272],
    uC = nh[26] + nh[458] + nh[42] + nh[459], _C = nh[26] + nh[458] + nh[37] + nh[210], dC = nh[26] + nh[458],
    lC = nh[26] + nh[458] + nh[37] + nh[168], vC = nh[1056] + nh[22] + nh[1054], bC = nh[42] + nh[918],
    yC = nh[26] + nh[265] + nh[42] + nh[918], gC = nh[26] + nh[265] + nh[42] + nh[1086] + nh[40] + nh[1075],
    xC = nh[26] + nh[773], pC = nh[398] + nh[59] + nh[226],
    EC = nh[26] + nh[265] + nh[114] + nh[161] + nh[40] + nh[1075], mC = nh[191] + nh[42] + nh[1086] + nh[40] + nh[1075],
    wC = nh[191] + nh[114] + nh[161] + nh[40] + nh[1075], TC = nh[26] + nh[84] + nh[40] + nh[1075],
    kC = nh[26] + nh[84] + nh[40] + nh[1075] + nh[14] + nh[121], MC = nh[84] + nh[40] + nh[1075] + nh[44] + nh[238],
    OC = nh[26] + nh[84] + nh[40] + nh[1075] + nh[37] + nh[277],
    IC = nh[26] + nh[84] + nh[40] + nh[1075] + nh[37] + nh[168], SC = nh[84] + nh[40] + nh[1075] + nh[37] + nh[1005],
    AC = nh[84] + nh[40] + nh[1075] + nh[42] + nh[774] + nh[271] + nh[272], jC = nh[271] + nh[272],
    PC = nh[1087] + nh[271] + nh[272], CC = nh[26] + nh[93] + nh[40] + nh[1075] + nh[14] + nh[121],
    LC = nh[93] + nh[40] + nh[1075] + nh[44] + nh[238], DC = nh[26] + nh[93] + nh[40] + nh[1075] + nh[37] + nh[277],
    RC = nh[26] + nh[93] + nh[40] + nh[1075] + nh[37] + nh[168], NC = nh[93] + nh[40] + nh[1075] + nh[37] + nh[1005],
    BC = nh[40] + nh[1075] + nh[37] + nh[196], $C = nh[40] + nh[1075] + nh[37] + nh[196] + nh[37] + nh[210],
    FC = nh[40] + nh[1075] + nh[37] + nh[1005], GC = nh[40] + nh[1075] + nh[44] + nh[285] + nh[53] + nh[284],
    zC = nh[40] + nh[1075] + nh[44] + nh[285] + nh[53] + nh[284] + nh[14] + nh[121],
    HC = nh[40] + nh[1075] + nh[42] + nh[774] + nh[1] + nh[438],
    YC = nh[40] + nh[1075] + nh[42] + nh[774] + nh[271] + nh[272],
    UC = nh[40] + nh[1075] + nh[44] + nh[285] + nh[1] + nh[434],
    WC = nh[40] + nh[1075] + nh[44] + nh[285] + nh[435] + nh[436], qC = nh[40] + nh[1075] + nh[14] + nh[1076],
    XC = nh[40] + nh[1075] + nh[14] + nh[1076] + nh[37] + nh[210], VC = nh[26] + nh[93] + nh[40] + nh[1075],
    ZC = nh[233] + nh[40] + nh[1075], KC = nh[42] + nh[1086] + nh[40] + nh[1075],
    JC = nh[114] + nh[161] + nh[40] + nh[1075], QC = nh[1088], tL = nh[233] + nh[28] + nh[321],
    iL = nh[367] + nh[22] + nh[363] + nh[22] + nh[1089], nL = nh[3] + nh[122] + nh[121],
    eL = nh[3] + nh[59] + nh[322] + nh[44] + nh[1077], sL = nh[163] + nh[59] + nh[322] + nh[44] + nh[1077],
    rL = nh[1090], hL = nh[1073] + nh[59] + nh[322] + nh[44] + nh[1077],
    aL = nh[367] + nh[22] + nh[363] + nh[22] + nh[479], oL = nh[709] + nh[71],
    fL = nh[106] + nh[37] + nh[770] + nh[114] + nh[299], cL = nh[1069] + nh[28] + nh[396],
    uL = nh[398] + nh[37] + nh[397], _L = nh[1091] + nh[24] + nh[1092], dL = nh[753] + nh[59] + nh[226],
    lL = nh[3] + nh[44] + nh[1093] + nh[59] + nh[226], vL = nh[203] + nh[118],
    bL = nh[3] + nh[44] + nh[1093] + nh[24] + nh[1092],
    yL = nh[456] + nh[22] + nh[736] + nh[22] + nh[391] + nh[22] + nh[118], gL = nh[67] + nh[1094],
    xL = nh[367] + nh[22] + nh[382] + nh[22] + nh[739],
    pL = nh[367] + nh[22] + nh[387] + nh[22] + nh[1052] + nh[22] + nh[1054],
    EL = nh[367] + nh[22] + nh[387] + nh[22] + nh[1052] + nh[22] + nh[247], mL = nh[67] + nh[1095] + nh[889] + nh[349],
    wL = nh[67] + nh[1096] + nh[28] + nh[218], TL = nh[22] + nh[1097], kL = nh[466], ML = nh[1098] + nh[32] + nh[1099],
    OL = nh[1100], IL = nh[1101], SL = nh[56] + nh[28] + nh[29], AL = nh[3] + nh[53] + nh[1102] + nh[181] + nh[480],
    jL = nh[1103] + nh[40] + nh[1104], PL = nh[317] + nh[229], CL = nh[163] + nh[114] + nh[178],
    LL = nh[37] + nh[277] + nh[317] + nh[229], DL = nh[163] + nh[271] + nh[1025], RL = nh[163] + nh[317] + nh[229],
    NL = nh[1105], BL = nh[107] + nh[24] + nh[70], $L = nh[545], FL = nh[36] + nh[28] + nh[164] + nh[1] + nh[1106],
    GL = nh[36] + nh[181] + nh[624] + nh[28] + nh[29], zL = nh[1107],
    HL = nh[1073] + nh[44] + nh[1108] + nh[59] + nh[226], YL = nh[1109], UL = nh[1110],
    WL = nh[1111] + nh[44] + nh[1112] + nh[42] + nh[1086], qL = nh[1111] + nh[44] + nh[1112] + nh[114] + nh[161],
    XL = nh[195] + nh[53] + nh[1113] + nh[14] + nh[15], VL = nh[917] + nh[24] + nh[152] + nh[28] + nh[1114],
    ZL = nh[106] + nh[28] + nh[164], KL = nh[3] + nh[53] + nh[54] + nh[37] + nh[210], JL = nh[1115] + nh[59] + nh[226],
    QL = nh[56] + nh[40] + nh[41], tD = nh[22] + nh[76] + nh[77] + nh[1116] + nh[59] + nh[226],
    iD = nh[631] + nh[40] + nh[239], nD = nh[631] + nh[181] + nh[76], eD = nh[631] + nh[14] + nh[482],
    sD = nh[1026] + nh[37] + nh[586], rD = nh[195] + nh[181] + nh[273], hD = nh[582] + nh[114] + nh[161],
    aD = nh[6] + nh[44] + nh[1117], oD = nh[631] + nh[114] + nh[161] + nh[14] + nh[1118],
    fD = nh[631] + nh[40] + nh[41], cD = nh[1119] + nh[22] + nh[1120], uD = nh[214] + nh[37] + nh[695],
    _D = nh[22] + nh[1121] + nh[40] + nh[41], dD = nh[1122], lD = nh[1123], vD = nh[1124] + nh[37] + nh[1125],
    bD = nh[1126] + nh[37] + nh[1125] + nh[40] + nh[1127], yD = nh[167] + nh[44] + nh[238],
    gD = nh[195] + nh[228] + nh[1128], xD = nh[22] + nh[1109] + nh[42] + nh[981], pD = nh[1124] + nh[28] + nh[1129],
    ED = nh[106] + nh[1] + nh[623] + nh[181] + nh[624], mD = nh[1130] + nh[929],
    wD = nh[53] + nh[1131] + nh[47] + nh[28] + nh[1132] + nh[237], TD = nh[11] + nh[37] + nh[1030],
    kD = nh[1133] + nh[22] + nh[1134], MD = nh[37] + nh[277], OD = nh[1133] + nh[22] + nh[1135], ID = nh[44] + nh[285],
    SD = nh[37] + nh[1005], AD = nh[163] + nh[28] + nh[321], jD = nh[28] + nh[321], PD = nh[354] + nh[1136] + nh[991],
    CD = nh[472] + nh[24] + nh[70], LD = nh[627] + nh[28] + nh[1137],
    DD = nh[1138] + nh[28] + nh[283] + nh[44] + nh[1077],
    RD = nh[44] + nh[1077] + nh[47] + nh[1] + nh[1139] + nh[133] + nh[239] + nh[47] + nh[28] + nh[283],
    ND = nh[1140] + nh[28] + nh[321], BD = nh[1141], $D = nh[1142] + nh[28] + nh[537], FD = nh[1091] + nh[24] + nh[457],
    GD = nh[1143], zD = nh[1144], HD = nh[472] + nh[228] + nh[229], YD = nh[359] + nh[1] + nh[534] + nh[53] + nh[535],
    UD = nh[22] + nh[1115] + nh[59] + nh[226], WD = nh[1145] + nh[102] + nh[1146], qD = nh[1147] + nh[102] + nh[1146],
    XD = nh[1148] + nh[22] + nh[1149], VD = nh[1020] + nh[22] + nh[1022] + nh[22] + nh[741],
    ZD = nh[1020] + nh[22] + nh[1022] + nh[22] + nh[225], KD = nh[22] + nh[942] + nh[77],
    JD = nh[22] + nh[942] + nh[259], QD = nh[1020] + nh[22] + nh[363] + nh[22] + nh[335],
    tR = nh[1020] + nh[22] + nh[363] + nh[22] + nh[713], iR = nh[1150] + nh[509], nR = nh[271] + nh[1025] + nh[395],
    eR = nh[1151] + nh[154],
    sR = nh[1152] + nh[47] + nh[218] + nh[71] + nh[75] + nh[947] + nh[47] + nh[1153] + nh[92] + nh[632], rR = nh[866],
    hR = nh[71] + nh[414] + nh[92] + nh[271] + nh[415] + nh[92] + nh[317] + nh[416] + nh[47] + nh[418],
    aR = nh[1152] + nh[98] + nh[206] + nh[99] + nh[275] + nh[92] + nh[780] + nh[515] + nh[63] + nh[64] + nh[218] + nh[426] + nh[218] + nh[426] + nh[218] + nh[426] + nh[218] + nh[71] + nh[349] + nh[66],
    oR = nh[71] + nh[414] + nh[92] + nh[271] + nh[415] + nh[92] + nh[317] + nh[416],
    fR = nh[1152] + nh[98] + nh[218] + nh[99], cR = nh[1154],
    uR = nh[98] + nh[1152] + nh[47] + nh[325] + nh[947] + nh[47] + nh[1155] + nh[92] + nh[746] + nh[64] + nh[218] + nh[71] + nh[259] + nh[426] + nh[218] + nh[426] + nh[218] + nh[71] + nh[259] + nh[426] + nh[206] + nh[66],
    _R = nh[71] + nh[414] + nh[92] + nh[271] + nh[415] + nh[92] + nh[317] + nh[416] + nh[98] + nh[442],
    dR = nh[1152] + nh[98] + nh[206] + nh[99],
    lR = nh[98] + nh[1152] + nh[47] + nh[218] + nh[71] + nh[325] + nh[947] + nh[47] + nh[782], vR = nh[1156],
    bR = nh[22] + nh[123], yR = nh[22] + nh[418], gR = nh[22] + nh[424],
    xR = nh[71] + nh[414] + nh[92] + nh[271] + nh[415] + nh[92] + nh[37] + nh[560] + nh[59] + nh[1157],
    pR = nh[1158] + nh[515] + nh[75] + nh[154] + nh[1159] + nh[282] + nh[515] + nh[941] + nh[99] + nh[1160] + nh[92] + nh[1161] + nh[515] + nh[280] + nh[92] + nh[1160] + nh[99] + nh[1160] + nh[92] + nh[437] + nh[1162] + nh[221] + nh[47] + nh[218] + nh[154] + nh[47] + nh[218] + nh[154] + nh[47] + nh[206] + nh[154] + nh[1159] + nh[275] + nh[92] + nh[780] + nh[515] + nh[63] + nh[64] + nh[1163] + nh[65] + nh[1163] + nh[65] + nh[1163] + nh[65] + nh[218] + nh[71] + nh[325] + nh[1164] + nh[280] + nh[92] + nh[513] + nh[515] + nh[223] + nh[154] + nh[99] + nh[1158] + nh[515] + nh[206] + nh[154] + nh[99],
    ER = nh[71] + nh[414] + nh[92] + nh[271] + nh[415] + nh[92] + nh[37] + nh[560] + nh[59] + nh[1157] + nh[71] + nh[442] + nh[1165] + nh[414] + nh[92] + nh[271] + nh[415] + nh[92] + nh[37] + nh[560] + nh[59] + nh[1157] + nh[98] + nh[442],
    mR = nh[1158] + nh[92] + nh[80] + nh[515] + nh[259] + nh[154] + nh[99],
    wR = nh[1158] + nh[92] + nh[79] + nh[515] + nh[259] + nh[154] + nh[99],
    TR = nh[71] + nh[414] + nh[92] + nh[271] + nh[415] + nh[92] + nh[37] + nh[560] + nh[24] + nh[425],
    kR = nh[98] + nh[1152] + nh[47] + nh[325] + nh[947] + nh[47] + nh[1155] + nh[92] + nh[746] + nh[64] + nh[218] + nh[71] + nh[259] + nh[426] + nh[218] + nh[426] + nh[218] + nh[71] + nh[259] + nh[426] + nh[206] + nh[1164],
    MR = nh[22] + nh[505] + nh[53] + nh[605] + nh[37] + nh[698],
    OR = nh[22] + nh[504] + nh[53] + nh[605] + nh[37] + nh[698],
    IR = nh[414] + nh[92] + nh[271] + nh[415] + nh[92] + nh[37] + nh[560] + nh[24] + nh[425], SR = nh[22] + nh[1166],
    AR = nh[992] + nh[181] + nh[1167], jR = nh[59] + nh[1168], PR = nh[1070],
    CR = nh[11] + nh[59] + nh[82] + nh[181] + nh[166], LR = nh[1169], DR = nh[1170] + nh[22] + nh[1171],
    RR = nh[1170] + nh[22] + nh[363], NR = nh[141] + nh[288] + nh[102] + nh[990],
    BR = nh[36] + nh[40] + nh[41] + nh[37] + nh[460], $R = nh[36] + nh[40] + nh[41] + nh[28] + nh[396],
    FR = nh[22] + nh[93] + nh[1172], GR = nh[22] + nh[84] + nh[1172], zR = nh[22] + nh[84] + nh[1173],
    HR = nh[22] + nh[93] + nh[1173], YR = nh[22] + nh[84] + nh[37] + nh[586], UR = nh[22] + nh[93] + nh[37] + nh[586],
    WR = nh[93] + nh[181] + nh[974], qR = nh[757] + nh[114] + nh[289], XR = nh[1174] + nh[114] + nh[299],
    VR = nh[478] + nh[22] + nh[1053] + nh[22] + nh[1175] + nh[22] + nh[170] + nh[22] + nh[1176],
    ZR = nh[106] + nh[53] + nh[183] + nh[24] + nh[25] + nh[1] + nh[534] + nh[44] + nh[476],
    KR = nh[198] + nh[24] + nh[25] + nh[1] + nh[534] + nh[53] + nh[535], JR = nh[988],
    QR = nh[11] + nh[53] + nh[183] + nh[24] + nh[25] + nh[1] + nh[534] + nh[44] + nh[476],
    tN = nh[11] + nh[53] + nh[1177], iN = nh[1169] + nh[44] + nh[45], nN = nh[22] + nh[1178] + nh[181] + nh[166],
    eN = nh[1179], sN = nh[128], rN = nh[195] + nh[1] + nh[1180] + nh[24] + nh[200], hN = nh[97] + nh[53] + nh[1181],
    aN = nh[3] + nh[53] + nh[54] + nh[53] + nh[1181] + nh[37] + nh[1005], oN = nh[477] + nh[24] + nh[152],
    fN = nh[106] + nh[53] + nh[1177], cN = nh[627], uN = nh[22] + nh[1182],
    _N = nh[22] + nh[76] + nh[77] + nh[570] + nh[24] + nh[389], dN = nh[1183] + nh[24] + nh[152],
    lN = nh[1184] + nh[22] + nh[1185] + nh[22] + nh[550], vN = nh[627] + nh[24] + nh[389], bN = nh[22] + nh[1186],
    yN = nh[765] + nh[22] + nh[1187] + nh[22] + nh[392], gN = nh[765] + nh[22] + nh[728] + nh[22] + nh[392],
    xN = nh[163] + nh[28] + nh[321] + nh[59] + nh[82] + nh[181] + nh[624], pN = nh[1188], EN = nh[1189],
    mN = nh[233] + nh[102] + nh[112], wN = nh[233] + nh[24] + nh[152], TN = nh[1190],
    kN = nh[67] + nh[206] + nh[53] + nh[1191], MN = nh[67] + nh[28] + nh[1192],
    ON = nh[163] + nh[37] + nh[277] + nh[59] + nh[82] + nh[181] + nh[624],
    IN = nh[1] + nh[1193] + nh[37] + nh[277] + nh[181] + nh[624],
    SN = nh[1] + nh[1193] + nh[44] + nh[285] + nh[181] + nh[624], AN = nh[28] + nh[321] + nh[395],
    jN = nh[233] + nh[102] + nh[1194] + nh[44] + nh[285],
    PN = nh[1] + nh[1193] + nh[37] + nh[1195] + nh[28] + nh[321] + nh[181] + nh[624],
    CN = nh[1052] + nh[22] + nh[1196] + nh[22] + nh[1197] + nh[22] + nh[1198] + nh[22] + nh[1199] + nh[22] + nh[1200],
    LN = nh[1201], DN = nh[414] + nh[92] + nh[44] + nh[1077] + nh[28] + nh[1202],
    RN = nh[1203] + nh[1204] + nh[1205] + nh[28] + nh[47] + nh[206] + nh[154], NN = nh[349] + nh[154], BN = nh[1206],
    $N = nh[36] + nh[32] + nh[33] + nh[1] + nh[534], FN = nh[1207], GN = nh[58] + nh[28] + nh[1137],
    zN = nh[36] + nh[37] + nh[168] + nh[1] + nh[534], HN = nh[56] + nh[28] + nh[1137], YN = nh[163] + nh[929],
    UN = nh[56] + nh[28] + nh[1137] + nh[108] + nh[1208] + nh[1] + nh[596] + nh[14] + nh[76] + nh[108] + nh[933],
    WN = nh[36] + nh[1] + nh[596] + nh[14] + nh[76] + nh[108] + nh[933], qN = nh[167] + nh[114] + nh[178],
    XN = nh[195] + nh[28] + nh[1209], VN = nh[44] + nh[1077] + nh[28] + nh[1202], ZN = nh[856] + nh[28] + nh[1202],
    KN = nh[1210], JN = nh[195] + nh[28] + nh[1114], QN = nh[0] + nh[28] + nh[321] + nh[59] + nh[322],
    tB = nh[367] + nh[22] + nh[387], iB = nh[1211] + nh[37] + nh[993] + nh[317] + nh[994],
    nB = nh[992] + nh[53] + nh[1212] + nh[1] + nh[596] + nh[114] + nh[161] + nh[14] + nh[1118],
    eB = nh[1213] + nh[37] + nh[586], sB = nh[219] + nh[114] + nh[161] + nh[1] + nh[985],
    rB = nh[11] + nh[37] + nh[1030] + nh[59] + nh[82] + nh[181] + nh[624], hB = nh[1214] + nh[130] + nh[131],
    aB = nh[1215] + nh[181] + nh[182], oB = nh[1215] + nh[47] + nh[236] + nh[237], fB = nh[47] + nh[81] + nh[47],
    cB = nh[572] + nh[28] + nh[1132], uB = nh[477] + nh[53] + nh[1216] + nh[28] + nh[164],
    _B = nh[195] + nh[32] + nh[454], dB = nh[1133] + nh[22] + nh[766] + nh[22] + nh[1217],
    lB = nh[219] + nh[28] + nh[1132], vB = nh[323] + nh[108] + nh[1218],
    bB = nh[1133] + nh[22] + nh[766] + nh[22] + nh[1219], yB = nh[1220], gB = nh[1221] + nh[22] + nh[1217],
    xB = nh[1221] + nh[22] + nh[1219], pB = nh[212] + nh[114] + nh[161] + nh[44] + nh[1222], EB = nh[22] + nh[1223],
    mB = nh[22] + nh[1224] + nh[24] + nh[1225], wB = nh[22] + nh[76] + nh[77] + nh[1139] + nh[28] + nh[1137],
    TB = nh[195] + nh[28] + nh[396] + nh[24] + nh[152] + nh[28] + nh[1114],
    kB = nh[195] + nh[28] + nh[396] + nh[24] + nh[152], MB = nh[195] + nh[1] + nh[1226] + nh[24] + nh[152],
    OB = nh[233] + nh[44] + nh[285], IB = nh[195] + nh[42] + nh[1086], SB = nh[67] + nh[1227],
    AB = nh[167] + nh[1] + nh[570] + nh[28] + nh[164], jB = nh[195] + nh[37] + nh[1228],
    PB = nh[195] + nh[24] + nh[152] + nh[102] + nh[1229],
    CB = nh[11] + nh[24] + nh[200] + nh[37] + nh[770] + nh[59] + nh[82] + nh[181] + nh[222],
    LB = nh[195] + nh[24] + nh[152] + nh[40] + nh[1230], DB = nh[385] + nh[22] + nh[525], RB = nh[1065],
    NB = nh[517] + nh[24] + nh[457], BB = nh[587] + nh[37] + nh[770], $B = nh[495] + nh[37] + nh[770],
    FB = nh[517] + nh[317] + nh[178] + nh[24] + nh[457], GB = nh[385] + nh[22] + nh[766] + nh[22] + nh[1217],
    zB = nh[22] + nh[76] + nh[1231], HB = nh[1008], YB = nh[1065] + nh[181] + nh[222], UB = nh[385] + nh[22] + nh[1232],
    WB = nh[628] + nh[53] + nh[629] + nh[118], qB = nh[456] + nh[22] + nh[1233] + nh[22] + nh[1038] + nh[22] + nh[247],
    XB = nh[456] + nh[22] + nh[1233] + nh[22] + nh[1040] + nh[22] + nh[247],
    VB = nh[1233] + nh[22] + nh[456] + nh[22] + nh[244] + nh[22] + nh[1234], ZB = nh[1235], KB = nh[1236],
    JB = nh[22] + nh[917], QB = nh[22] + nh[15] + nh[223] + nh[114] + nh[470], t$ = nh[1237] + nh[22] + nh[1219],
    i$ = nh[1238] + nh[37] + nh[1030] + nh[228] + nh[229], n$ = nh[294] + nh[28] + nh[295] + nh[32] + nh[454] + nh[395],
    e$ = nh[632] + nh[102] + nh[1239], s$ = nh[1237] + nh[22] + nh[1240],
    r$ = nh[1233] + nh[22] + nh[456] + nh[22] + nh[244],
    h$ = nh[1233] + nh[22] + nh[456] + nh[22] + nh[244] + nh[22] + nh[1241], a$ = nh[22] + nh[1151] + nh[181] + nh[166],
    o$ = nh[1242], f$ = nh[1243], c$ = nh[22] + nh[203] + nh[24] + nh[152], u$ = nh[22] + nh[1244],
    _$ = nh[1245] + nh[92] + nh[466], d$ = nh[1246] + nh[92] + nh[466], l$ = nh[1247] + nh[92] + nh[466],
    v$ = nh[63] + nh[64] + nh[218] + nh[426] + nh[1248] + nh[426] + nh[218] + nh[426] + nh[206] + nh[66],
    b$ = nh[67] + nh[1249] + nh[218], y$ = nh[22] + nh[203] + nh[443] + nh[1250] + nh[44] + nh[1251],
    g$ = nh[195] + nh[102] + nh[1252], x$ = nh[195] + nh[102] + nh[1253], p$ = nh[22] + nh[76] + nh[77] + nh[278],
    E$ = nh[22] + nh[1254], m$ = nh[1053] + nh[22] + nh[1217], w$ = nh[1255] + nh[22] + nh[1217],
    T$ = nh[776] + nh[59] + nh[226], k$ = nh[1256], M$ = nh[1257], O$ = nh[102] + nh[1258] + nh[181] + nh[624],
    I$ = nh[167] + nh[37] + nh[1030], S$ = nh[195] + nh[37] + nh[1259],
    A$ = nh[1260] + nh[114] + nh[161] + nh[114] + nh[1071], j$ = nh[1237], P$ = nh[463] + nh[40] + nh[1127],
    C$ = nh[1261] + nh[22] + nh[1262], L$ = nh[1263] + nh[22] + nh[1264], D$ = nh[414] + nh[92] + nh[114] + nh[1265],
    R$ = nh[1266] + nh[22] + nh[391] + nh[22] + nh[117], N$ = nh[67] + nh[1267],
    B$ = nh[206] + nh[154] + nh[47] + nh[1203] + nh[1204] + nh[53] + nh[77] + nh[53] + nh[77] + nh[53] + nh[77],
    $$ = nh[75] + nh[154] + nh[47] + nh[223] + nh[154], F$ = nh[1268], G$ = nh[1032],
    z$ = nh[1269] + nh[1270] + nh[1271], H$ = nh[172] + nh[1] + nh[1272],
    Y$ = nh[1266] + nh[22] + nh[391] + nh[22] + nh[118], U$ = nh[22] + nh[1273],
    W$ = nh[3] + nh[114] + nh[1265] + nh[28] + nh[164], q$ = nh[1098], X$ = nh[932], V$ = nh[167] + nh[114] + nh[1265],
    Z$ = nh[56] + nh[114] + nh[470], K$ = nh[1261] + nh[22] + nh[552], J$ = nh[627] + nh[114] + nh[470],
    Q$ = nh[992] + nh[114] + nh[1265], tF = nh[1004] + nh[114] + nh[299], iF = nh[1004] + nh[53] + nh[598],
    nF = nh[1004] + nh[53] + nh[1274], eF = nh[288] + nh[40] + nh[1104],
    sF = nh[992] + nh[108] + nh[1275] + nh[652] + nh[1276],
    rF = nh[631] + nh[59] + nh[82] + nh[228] + nh[559] + nh[28] + nh[29],
    hF = nh[988] + nh[71] + nh[219] + nh[71] + nh[627], aF = nh[1133] + nh[22] + nh[1232],
    oF = nh[988] + nh[71] + nh[1277], fF = nh[988] + nh[71] + nh[219] + nh[71] + nh[917],
    cF = nh[988] + nh[71] + nh[1278], uF = nh[988] + nh[71] + nh[1279],
    _F = nh[1065] + nh[71] + nh[219] + nh[71] + nh[627], dF = nh[1065] + nh[71] + nh[1277],
    lF = nh[385] + nh[22] + nh[766] + nh[22] + nh[1219], vF = nh[1065] + nh[71] + nh[219] + nh[71] + nh[917],
    bF = nh[1065] + nh[71] + nh[106], yF = nh[385] + nh[22] + nh[526], gF = nh[1065] + nh[71] + nh[11],
    xF = nh[466] + nh[71] + nh[627], pF = nh[1280], EF = nh[466] + nh[71] + nh[917], mF = nh[203] + nh[71] + nh[627],
    wF = nh[1281], TF = nh[1053] + nh[22] + nh[1219], kF = nh[203] + nh[71] + nh[917], MF = nh[1121] + nh[71] + nh[627],
    OF = nh[1121] + nh[71] + nh[917], IF = nh[1023] + nh[71] + nh[995], SF = nh[354] + nh[71] + nh[407],
    AF = nh[1237] + nh[22] + nh[1217], jF = nh[463] + nh[71] + nh[627], PF = nh[463] + nh[71] + nh[1282],
    CF = nh[463] + nh[71] + nh[917], LF = nh[576] + nh[71] + nh[564], DF = nh[22] + nh[472] + nh[37] + nh[698],
    RF = nh[22] + nh[36] + nh[28] + nh[164] + nh[1] + nh[530], NF = nh[22] + nh[1283] + nh[181] + nh[624],
    BF = nh[22] + nh[76] + nh[206] + nh[1] + nh[623] + nh[181] + nh[624] + nh[44] + nh[476],
    $F = nh[477] + nh[181] + nh[624] + nh[228] + nh[229], FF = nh[22] + nh[76] + nh[77] + nh[570] + nh[228] + nh[229],
    GF = nh[3] + nh[181] + nh[624] + nh[181] + nh[1284], zF = nh[333] + nh[181] + nh[622],
    HF = nh[478] + nh[22] + nh[244] + nh[22] + nh[1285], YF = nh[1286],
    UF = nh[478] + nh[22] + nh[244] + nh[22] + nh[456], WF = nh[1287], qF = nh[1288],
    XF = nh[163] + nh[71] + nh[1289] + nh[71] + nh[354],
    VF = nh[478] + nh[22] + nh[244] + nh[22] + nh[1290] + nh[22] + nh[367], ZF = nh[163] + nh[71] + nh[354],
    KF = nh[478] + nh[22] + nh[244] + nh[22] + nh[1290] + nh[22] + nh[334], JF = nh[163] + nh[71] + nh[1039],
    QF = nh[478] + nh[22] + nh[244] + nh[22] + nh[1290] + nh[22] + nh[728], tG = nh[163] + nh[71] + nh[199],
    iG = nh[478] + nh[22] + nh[244] + nh[22] + nh[1290] + nh[22] + nh[1291] + nh[22] + nh[367],
    nG = nh[478] + nh[22] + nh[244] + nh[22] + nh[1292], eG = nh[24] + nh[1139] + nh[181] + nh[624],
    sG = nh[37] + nh[1030] + nh[181] + nh[624], rG = nh[228] + nh[1293] + nh[181] + nh[624],
    hG = nh[108] + nh[1275] + nh[652] + nh[1276] + nh[181] + nh[624],
    aG = nh[53] + nh[1212] + nh[1] + nh[596] + nh[181] + nh[624], oG = nh[28] + nh[1294] + nh[181] + nh[624],
    fG = nh[114] + nh[1265] + nh[181] + nh[624], cG = nh[102] + nh[1239] + nh[37] + nh[1030] + nh[181] + nh[624],
    uG = nh[102] + nh[1239] + nh[37] + nh[1030] + nh[181] + nh[624] + nh[59] + nh[82] + nh[102] + nh[1295] + nh[59] + nh[417],
    _G = nh[24] + nh[457] + nh[181] + nh[624], dG = nh[44] + nh[1296], lG = nh[3] + nh[1297] + nh[226], vG = nh[1298],
    bG = nh[1299], yG = nh[1300], gG = nh[1301], xG = nh[3] + nh[44] + nh[353] + nh[102] + nh[1302],
    pG = nh[1303] + nh[22] + nh[373], EG = nh[1303] + nh[22] + nh[372], mG = nh[1303] + nh[22] + nh[511],
    wG = nh[1303] + nh[22] + nh[372] + nh[22] + nh[378], TG = nh[1303] + nh[22] + nh[378] + nh[22] + nh[372],
    kG = nh[1303] + nh[22] + nh[377] + nh[22] + nh[372], MG = nh[1303] + nh[22] + nh[377] + nh[22] + nh[373],
    OG = nh[1304], IG = nh[1305] + nh[71] + nh[1306], SG = nh[1304] + nh[71] + nh[647],
    AG = nh[1304] + nh[71] + nh[1307], jG = nh[1045] + nh[22] + nh[363] + nh[22] + nh[1308],
    PG = nh[1045] + nh[22] + nh[363] + nh[22] + nh[1308] + nh[22] + nh[370],
    CG = nh[1045] + nh[22] + nh[363] + nh[22] + nh[1308] + nh[22] + nh[371],
    LG = nh[1045] + nh[22] + nh[363] + nh[22] + nh[1309] + nh[22] + nh[1310],
    DG = nh[195] + nh[443] + nh[1311] + nh[53] + nh[1312], RG = nh[31] + nh[37] + nh[168],
    NG = nh[195] + nh[44] + nh[1313], BG = nh[319] + nh[1] + nh[2] + nh[53] + nh[1312], $G = nh[22] + nh[942] + nh[325],
    FG = nh[3] + nh[1314] + nh[434], GG = nh[3] + nh[1315] + nh[434], zG = nh[647] + nh[271] + nh[434],
    HG = nh[1307] + nh[271] + nh[434], YG = nh[257] + nh[114] + nh[299], UG = nh[1316],
    WG = nh[22] + nh[76] + nh[77] + nh[647], qG = nh[1012] + nh[44] + nh[353], XG = nh[319] + nh[59] + nh[226],
    VG = nh[22] + nh[1317], ZG = nh[22] + nh[1318] + nh[44] + nh[238], KG = nh[22] + nh[896], JG = nh[262] + nh[1319],
    QG = nh[262] + nh[1320], tz = nh[22] + nh[1321] + nh[24] + nh[946] + nh[1] + nh[2] + nh[53] + nh[1312],
    iz = nh[497] + nh[117], nz = nh[497] + nh[118], ez = nh[22] + nh[1322], sz = nh[497], rz = nh[22] + nh[1323],
    hz = nh[257] + nh[102] + nh[453], az = nh[22] + nh[76] + nh[1324], oz = nh[257] + nh[53] + nh[493],
    fz = nh[477] + nh[228] + nh[1325] + nh[317] + nh[324], cz = nh[265] + nh[44] + nh[353] + nh[53] + nh[493],
    uz = nh[1326], _z = nh[56], dz = nh[1213] + nh[44] + nh[353] + nh[53] + nh[493],
    lz = nh[3] + nh[228] + nh[1327] + nh[181] + nh[1328], vz = nh[354] + nh[1] + nh[329], bz = nh[1329],
    yz = nh[195] + nh[102] + nh[1330], gz = nh[22] + nh[667] + nh[77] + nh[509], xz = nh[627] + nh[40] + nh[1331],
    pz = nh[59] + nh[1332] + nh[44] + nh[1296], Ez = nh[1333], mz = nh[1334], wz = nh[231], Tz = nh[1335],
    kz = nh[1336] + nh[22] + nh[1337] + nh[22] + nh[1338], Mz = nh[739] + nh[22] + nh[244] + nh[22] + nh[1339],
    Oz = nh[3] + nh[317] + nh[229] + nh[37] + nh[168], Iz = nh[3] + nh[271] + nh[434], Sz = nh[1340],
    Az = nh[3] + nh[102] + nh[281], jz = nh[22] + nh[76] + nh[77] + nh[161], Pz = nh[497] + nh[75],
    Cz = nh[367] + nh[22] + nh[387] + nh[22] + nh[1024], Lz = nh[497] + nh[206],
    Dz = nh[22] + nh[536] + nh[59] + nh[1341] + nh[42] + nh[274], Rz = nh[1111] + nh[59] + nh[1342],
    Nz = nh[367] + nh[22] + nh[387] + nh[22] + nh[1343], Bz = nh[22] + nh[1344],
    $z = nh[195] + nh[24] + nh[1345] + nh[14] + nh[1346], Fz = nh[22] + nh[952] + nh[181] + nh[76] + nh[59] + nh[322],
    Gz = nh[1347], zz = nh[1348] + nh[181] + nh[1349], Hz = nh[1350] + nh[118], Yz = nh[195] + nh[181] + nh[1351],
    Uz = nh[1352], Wz = nh[1350], qz = nh[1213], Xz = nh[1350] + nh[117], Vz = nh[1022] + nh[22] + nh[502],
    Zz = nh[1353], Kz = nh[1354], Jz = nh[257] + nh[228] + nh[1355], Qz = nh[1356],
    tH = nh[22] + nh[1357] + nh[42] + nh[1358], iH = nh[1356] + nh[40] + nh[357], nH = nh[1359],
    eH = nh[477] + nh[28] + nh[1360], sH = nh[124] + nh[37] + nh[695], rH = nh[37] + nh[1361] + nh[44] + nh[1296],
    hH = nh[1153] + nh[14] + nh[482] + nh[37] + nh[1362], aH = nh[517] + nh[44] + nh[1363], oH = nh[22] + nh[1364],
    fH = nh[22] + nh[1365], cH = nh[330] + nh[47] + nh[137],
    uH = nh[294] + nh[28] + nh[295] + nh[59] + nh[82] + nh[114] + nh[1366] + nh[53] + nh[998] + nh[42] + nh[999] + nh[37] + nh[1367],
    _H = nh[294] + nh[28] + nh[295] + nh[59] + nh[82] + nh[114] + nh[1366] + nh[59] + nh[1368] + nh[42] + nh[999] + nh[37] + nh[1367],
    dH = nh[22] + nh[84], lH = nh[93] + nh[1] + nh[438], vH = nh[195] + nh[1369], bH = nh[195] + nh[14] + nh[1370],
    yH = nh[195] + nh[271] + nh[1371], gH = nh[195] + nh[42] + nh[1372], xH = nh[195] + nh[37] + nh[1373],
    pH = nh[195] + nh[1] + nh[1374], EH = nh[195] + nh[228] + nh[1375], mH = nh[53] + nh[54] + nh[37] + nh[1005],
    wH = nh[53] + nh[1376], TH = nh[1] + nh[1377], kH = nh[271] + nh[1378], MH = nh[59] + nh[96] + nh[395],
    OH = nh[28] + nh[164] + nh[395], IH = nh[317] + nh[229] + nh[395], SH = nh[181] + nh[182] + nh[395],
    AH = nh[24] + nh[200], jH = nh[181] + nh[624] + nh[28] + nh[29], PH = nh[271] + nh[415] + nh[228] + nh[447],
    CH = nh[28] + nh[321] + nh[59] + nh[322], LH = nh[414] + nh[1379] + nh[47] + nh[294] + nh[47] + nh[929] + nh[349],
    DH = nh[75] + nh[71] + nh[218], RH = nh[75] + nh[71] + nh[350] + nh[71] + nh[188] + nh[47] + nh[1031] + nh[325],
    NH = nh[414] + nh[1379] + nh[237] + nh[53] + nh[1380] + nh[47] + nh[1] + nh[1381] + nh[47] + nh[294] + nh[47] + nh[929] + nh[349] + nh[449] + nh[1] + nh[314],
    BH = nh[694] + nh[1177], $H = nh[1382] + nh[449] + nh[188] + nh[449] + nh[1383], FH = 0;
  if (t.navigator) {
    var GH = navigator[bl], zH = /opera/i[Zo](GH), HH = !zH && /msie/i[Zo](GH), YH = /rv:11.0/i[Zo](GH),
      UH = /MSIE 10./i[Zo](GH);
    if (/Edge/i[Zo](GH), YH && (HH = !0), /msie\s[6,7,8]/i[Zo](GH)) throw new Error("your browser is not supported");
    var WH = /webkit|khtml/i[Zo](GH), qH = !WH && /gecko/i[Zo](GH), XH = /firefox\//i[Zo](GH), VH = /Chrome\//i[Zo](GH),
      ZH = !VH && /Safari\//i[Zo](GH), KH = /Macintosh;/i[Zo](GH), JH = /(iPad|iPhone|iPod)/g.test(GH),
      QH = /Android/g[Zo](GH), tY = /Windows Phone/g.test(GH), iY = (JH || QH || tY) && V_ in t,
      nY = GH[U_](/AppleWebKit\/([0-9\.]*)/);
    if (nY && nY[rh] > 1) {
      parseFloat(nY[1])
    }
    QH && parseFloat(GH.match(/Android\s([0-9\.]*)/)[1])
  }
  t[Sh] || (t[Sh] = t[yl] || t.mozRequestAnimationFrame || t[gl] || t.msRequestAnimationFrame || function (i) {
    return t[xl](function () {
      i()
    }, 1e3 / 60)
  }), t[pl] || (t[pl] = t[El] || t[ml] || t.oCancelAnimationFrame || t.msCancelAnimationFrame || function (i) {
    return t.clearTimeout(i)
  });
  var eY = {SELECTION_TOLERANCE: iY ? 7 : 4, LABEL_COLOR: wl};
  K(eY, {
    FONT_STYLE: {
      get: function () {
        return this[Tl] || (this[Tl] = kl)
      }, set: function (t) {
        this._fontStyle != t && (this[Tl] = t, this._fontChanged = !0)
      }
    }, FONT_SIZE: {
      get: function () {
        return this[Ml] || (this[Ml] = 12)
      }, set: function (t) {
        this[Ml] != t && (this[Ml] = t, this[Ol] = !0)
      }
    }, FONT_FAMILY: {
      get: function () {
        return this[Il] || (this._fontFamily = "Verdana,helvetica,arial,sans-serif")
      }, set: function (t) {
        this[Il] != t && (this._fontFamily = t, this[Ol] = !0)
      }
    }, FONT: {
      get: function () {
        return (this._fontChanged || this[Ol] === n) && (this[Ol] = !1, this[Sl] = this[Al] + Ph + this[_o] + lo + this[jl]), this[Sl]
      }
    }
  });
  var sY = function (t) {
    this._je = [], this._ln = {}, t && this.add(t)
  };
  sY[yh] = {
    _je: null, _ln: null, get: function (t) {
      return this[Pl](t)
    }, getById: function (t) {
      return this._ln[t]
    }, getByIndex: function (t) {
      return this._je[t]
    }, forEach: function (t, i, n) {
      return l(this._je, t, i, n)
    }, forEachReverse: function (t, i, n) {
      return b(this._je, t, i, n)
    }, size: function () {
      return this._je[rh]
    }, contains: function (t) {
      return this.containsById(t.id)
    }, containsById: function (t) {
      return this._ln.hasOwnProperty(t)
    }, setIndex: function (t, i) {
      var n = this._je[lh](t);
      if (0 > n) throw new Error(Fa + t.id + Cl);
      return n == i ? !1 : (this._je[oh](n, 1), this._je[oh](i, 0, t), !0)
    }, setIndexAfter: function (t, i) {
      var n = this._je.indexOf(t);
      if (0 > n) throw new Error(Fa + t.id + Cl);
      return n == i ? i : n == i + 1 ? i + 1 : (n > i && (i += 1), this._je.splice(n, 1), this._je.splice(i, 0, t), i)
    }, setIndexBefore: function (t, i) {
      var n = this._je[lh](t);
      if (0 > n) throw new Error(Fa + t.id + Cl);
      return n == i ? i : n == i - 1 ? i - 1 : (i > n && (i -= 1), this._je.splice(n, 1), this._je[oh](i, 0, t), i)
    }, indexOf: function (t) {
      return this._je[lh](t)
    }, getIndexById: function (t) {
      var i = this.getById(t);
      return i ? this._je[lh](i) : -1
    }, add: function (t, i) {
      return $(t) ? this._f7(t, i) : this._ki(t, i)
    }, addFirst: function (t) {
      return this.add(t, 0)
    }, _f7: function (t, i) {
      if (0 == t[rh]) return !1;
      var e = !1, s = i >= 0;
      t = t._je || t;
      for (var r = 0, h = t[rh]; h > r; r++) {
        var a = t[r];
        null !== a && a !== n && this._ki(a, i, !0) && (e = !0, s && i++)
      }
      return e
    }, _ki: function (t, i) {
      var e = t.id;
      return e === n || this[Ll](e) ? !1 : (g(this._je, t, i), this._ln[e] = t, t)
    }, remove: function (t) {
      return $(t) ? this[Dl](t) : t.id ? this._f9(t.id, t) : this.removeById(t)
    }, _nc6: function (t) {
      if (0 == t[rh]) return !1;
      var i = !1;
      t = t._je || t;
      for (var e = 0, s = t[rh]; s > e; e++) {
        var r = t[e];
        if (null !== r && r !== n) {
          r.id === n && (r = this._ln[r]);
          var h = r.id;
          this._f9(h, r, !0) && (i = !0)
        }
      }
      return i
    }, _f9: function (t, i) {
      return t !== n && this.containsById(t) ? ((null === i || i === n) && (i = this[Rl](t)), delete this._ln[t], x(this._je, i), !0) : !1
    }, removeById: function (t) {
      var i = this._ln[t];
      return i ? this._f9(t, i) : !1
    }, set: function (t) {
      if (!t || 0 == t) return this[Qa]();
      if (this.isEmpty() || !$(t)) return this[Qa](), this.add(t);
      var i = [], n = {}, e = 0;
      if (l(t, function (t) {
        this._ln[t.id] ? (n[t.id] = t, e++) : i[Io](t)
      }, this), e != this.length) {
        var s = [];
        this[Dc](function (t) {
          n[t.id] || s[Io](t)
        }, this), s[rh] && this[Dl](s)
      }
      return i.length && this._f7(i), !0
    }, clear: function () {
      return this.isEmpty() ? !1 : (this._je[rh] = 0, this._ln = {}, !0)
    }, toDatas: function () {
      return this._je[ch](0)
    }, isEmpty: function () {
      return 0 == this._je.length
    }, valueOf: function () {
      return this._je[rh]
    }, clone: function (t) {
      var i = new sY;
      return i.add(t ? y(this._je) : this[Nl]()), i
    }
  }, K(sY[yh], {
    datas: {
      get: function () {
        return this._je
      }
    }, random: {
      get: function () {
        return this._je && this._je[rh] ? this._je[H(this._je[rh])] : null
      }
    }, length: {
      get: function () {
        return this._je ? this._je[rh] : 0
      }
    }
  });
  var rY = (2 * Math.PI, .5 * Math.PI), hY = function (t, i) {
    i = i[_a]();
    for (var n = HH ? t.firstChild : t[Bl]; n && (1 != n.nodeType || n[$l] && n[$l][_a]() != i);) n = HH ? n.nextSibling : n[Fl];
    return n && 1 == n[Gl] && n.tagName && n[$l][_a]() == i ? n : null
  }, aY = function (t, i, n) {
    t instanceof aY && (i = t.y, t = t.x, n = t[Go]), this.set(t, i, n)
  }, oY = function (t, i, n, e) {
    var s = t - n, r = i - e;
    return Math.sqrt(s * s + r * r)
  };
  aY[yh] = {
    x: 0, y: 0, rotate: n, set: function (t, i, n) {
      this.x = t || 0, this.y = i || 0, this[Go] = n || 0
    }, negate: function () {
      this.x = -this.x, this.y = -this.y
    }, offset: function (t, i) {
      this.x += t, this.y += i
    }, equals: function (t) {
      return this.x == t.x && this.y == t.y
    }, distanceTo: function (t) {
      return oY(this.x, this.y, t.x, t.y)
    }, toString: function () {
      return zl + this.x + Hl + this.y + Uh
    }, clone: function () {
      return new aY(this.x, this.y)
    }
  }, Object[mh](aY.prototype, Yl, {
    get: function () {
      return Math[To](this.x * this.x + this.y * this.y)
    }
  });
  var fY = function (t, i, e, s) {
    t !== n && this._mx(t, i, e, s)
  };
  fY[yh] = {
    _n7: null, _n2: null, _n5: null, _n3: null, _nc: null, _na: null, _n9: 1, _mx: function (t, i, n, e) {
      this._n7 = t, this._n2 = i, this._n5 = n, this._n3 = e, t == n ? (this._nc = -1, this._n9 = 0, this._na = t) : (this._nc = (i - e) / (t - n), this._na = i - this._nc * t, this._n9 = 1), this._l2 = Math[ta](this._n3 - this._n2, this._n5 - this._n7), this._n9os = Math.cos(this._l2), this[Ul] = Math.sin(this._l2)
    }, _d0: function (t) {
      return 0 == this._n9 ? Number.NaN : this._nc * t + this._na
    }, _d2: function (t) {
      return 0 == this._nc ? Number.NaN : (t - this._na) / this._nc
    }, _$c: function (t) {
      var i, n, e, s, r, h = t[0], a = t[2], o = t[4], f = t[1], c = t[3], u = t[5], _ = this._nc, d = this._na,
        l = this._n9;
      if (0 == l ? (e = Math[To]((-_ * _ * h - _ * d) * o + _ * _ * a * a + 2 * _ * d * a - _ * d * h), s = -_ * a + _ * h, r = _ * o - 2 * _ * a + _ * h) : (e = Math.sqrt((-f + _ * h + d) * u + c * c + (-2 * _ * a - 2 * d) * c + (_ * o + d) * f + (-_ * _ * h - _ * d) * o + _ * _ * a * a + 2 * _ * d * a - _ * d * h), s = -c + f + _ * a - _ * h, r = u - 2 * c + f - _ * o + 2 * _ * a - _ * h), 0 != r) {
        i = (e + s) / r, n = (-e + s) / r;
        var v, b;
        return i >= 0 && 1 >= i && (v = Yi(i, t)), n >= 0 && 1 >= n && (b = Yi(n, t)), v && b ? [v, b] : v ? v : b ? b : void 0
      }
    }, _3s: function (t, i, n) {
      if (this._nc == t._nc || 0 == this._n9 && 0 == t._n9) return null;
      var e, s;
      if (e = 0 == this._n9 ? this._na : 0 == t._n9 ? t._na : (t._na - this._na) / (this._nc - t._nc), s = 0 == this._nc ? this._na : 0 == t._nc ? t._na : this._n9 ? this._nc * e + this._na : t._nc * e + t._na, !i) return {
        x: e,
        y: s
      };
      var r, h, a;
      if (n) r = -i / 2, h = -r; else {
        r = -oY(this._n7, this._n2, e, s), h = oY(this._n5, this._n3, e, s);
        var o = -r + h;
        if (o > i) {
          var f = i / o;
          r *= f, h *= f
        } else a = (i - o) / 2
      }
      var c = this._73(e, s, r), u = this._73(e, s, h);
      return a && (c[Hf] = a, u[Hf] = a), [c, u]
    }, _73: function (t, i, n) {
      return 0 == this._n9 ? {x: t, y: i + n} : {x: t + n * this._n9os, y: i + n * this[Ul]}
    }
  };
  var cY = function (t, i) {
    this[ha] = t, this.height = i
  };
  cY[yh] = {
    width: 0, height: 0, isEmpty: function () {
      return this[ha] <= 0 || this.height <= 0
    }, clone: function () {
      return new cY(this.width, this.height)
    }, toString: function () {
      return Wl + this[ha] + Hl + this[Ja] + Uh
    }
  };
  var uY = function (t, i, e, s) {
    t instanceof Object && !R(t) && (i = t.y, e = t.width, s = t[Ja], t = t.x), e === n && (e = -1), s === n && (s = -1), this.x = t || 0, this.y = i || 0, this[ha] = e, this.height = s
  };
  uY[yh] = {
    x: 0, y: 0, width: -1, height: -1, setByRect: function (t) {
      this.x = t.x || 0, this.y = t.y || 0, this.width = t[ha] || 0, this[Ja] = t[Ja] || 0
    }, set: function (t, i, n, e) {
      this.x = t || 0, this.y = i || 0, this[ha] = n || 0, this[Ja] = e || 0
    }, offset: function (t, i) {
      return this.x += t, this.y += i, this
    }, contains: function (t, i, n, e) {
      if (1 == arguments[rh]) {
        if (vh == typeof t && fi(t)) return this[B_](t.x, t.y, t.width, t[Ja]);
        throw{message: ql}
      }
      return 2 == arguments[rh] ? t >= this.x && t <= this.x + this[ha] && i >= this.y && i <= this.y + this[Ja] : ai(this.x, this.y, this[ha], this[Ja], t, i, n || 0, e || 0)
    }, intersectsPoint: function (t, i, n) {
      return this[ha] <= 0 && this[Ja] <= 0 ? !1 : n ? this[Xl](t - n, i - n, 2 * n, 2 * n) : t >= this.x && t <= this.x + this[ha] && i >= this.y && i <= this.y + this[Ja]
    }, intersectsRect: function (t, i, n, e) {
      return ri(this.x, this.y, this[ha], this[Ja], t, i, n, e)
    }, intersects: function (t, i) {
      return R(t[ha]) ? this[Xl](t.x, t.y, t[ha], t[Ja]) : this[jo](t, i)
    }, intersection: function (t, i, n, e) {
      var s = this.x, r = this.y, h = s;
      h += this[ha];
      var a = r;
      a += this.height;
      var o = t;
      o += n;
      var f = i;
      return f += e, t > s && (s = t), i > r && (r = i), h > o && (h = o), a > f && (a = f), h -= s, a -= r, 0 > h || 0 > a ? null : new uY(s, r, h, a)
    }, addPoint: function (t) {
      this.add(t.x, t.y)
    }, add: function (t, i) {
      if (R(t.width)) return this.addRect(t.x, t.y, t.width, t[Ja]);
      if (R(t.x) && (i = t.y, t = t.x), this.width < 0 || this[Ja] < 0) return this.x = t, this.y = i, void (this[ha] = this.height = 0);
      var n = this.x, e = this.y, s = this.width, r = this[Ja];
      s += n, r += e, n > t && (n = t), e > i && (e = i), t > s && (s = t), i > r && (r = i), s -= n, r -= e, s > Number.MAX_VALUE && (s = Number[Vl]), r > Number[Vl] && (r = Number.MAX_VALUE), this.set(n, e, s, r)
    }, addRect: function (t, i, n, e) {
      var s = this[ha], r = this[Ja];
      (0 > s || 0 > r) && this.set(t, i, n, e);
      var h = n, a = e;
      if (!(0 > h || 0 > a)) {
        var o = this.x, f = this.y;
        s += o, r += f;
        var c = t, u = i;
        h += c, a += u, o > c && (o = c), f > u && (f = u), h > s && (s = h), a > r && (r = a), s -= o, r -= f, s > Number[Vl] && (s = Number[Vl]), r > Number[Vl] && (r = Number.MAX_VALUE), this.set(o, f, s, r)
      }
    }, shrink: function (t, i, n, e) {
      return R(t) ? 1 == arguments[rh] ? e = i = n = t || 0 : 2 == arguments[rh] ? (n = t || 0, e = i || 0) : (t = t || 0, i = i || 0, n = n || 0, e = e || 0) : (i = t.left || 0, n = t[ea] || 0, e = t[na] || 0, t = t.top || 0), this.x += i, this.y += t, this[ha] -= i + e, this[Ja] -= t + n, this
    }, grow: function (t, i, n, e) {
      return R(t) ? 1 == arguments[rh] ? e = i = n = t || 0 : 2 == arguments[rh] ? (n = t || 0, e = i || 0) : (t = t || 0, i = i || 0, n = n || 0, e = e || 0) : (i = t[Ca] || 0, n = t[ea] || 0, e = t[na] || 0, t = t.top || 0), this.x -= i, this.y -= t, this[ha] += i + e, this[Ja] += t + n, this
    }, scale: function (t) {
      return this.x *= t, this.y *= t, this[ha] *= t, this[Ja] *= t, this
    }, isEmpty: function () {
      return this[ha] <= 0 && this[Ja] <= 0
    }, toString: function () {
      return this.x + Zl + this.y + Zl + this[ha] + Zl + this[Ja]
    }, union: function (t) {
      var i = this[ha], n = this[Ja];
      if (0 > i || 0 > n) return new uY(t.x, t.y, t.width, t[Ja]);
      var e = t[ha], s = t[Ja];
      if (0 > e || 0 > s) return new uY(this.x, this.y, this[ha], this[Ja]);
      var r = this.x, h = this.y;
      i += r, n += h;
      var a = t.x, o = t.y;
      return e += a, s += o, r > a && (r = a), h > o && (h = o), e > i && (i = e), s > n && (n = s), i -= r, n -= h, i > Number[Vl] && (i = Number.MAX_VALUE), n > Number[Vl] && (n = Number.MAX_VALUE), new uY(r, h, i, n)
    }, clear: function () {
      this.set(0, 0, -1, -1)
    }, equals: function (t) {
      return t && this.x == t.x && this.y == t.y && this.width == t.width && this[Ja] == t[Ja]
    }, clone: function (t, i) {
      return new uY(this.x + (t || 0), this.y + (i || 0), this[ha], this[Ja])
    }, toArray: function () {
      return [this.x, this.y, this[ha], this[Ja]]
    }, getIntersectionPoint: function (t, i, n, e) {
      return si(this, t, i, n, e)
    }
  }, m(uY, cY), uY.equals = function (t, i) {
    return t == i || t && i && t.x == i.x && t.y == i.y && t[ha] == i[ha] && t[Ja] == i[Ja]
  }, K(uY[yh], {
    left: {
      get: function () {
        return this.x
      }
    }, top: {
      get: function () {
        return this.y
      }
    }, bottom: {
      get: function () {
        return this.y + this[Ja]
      }
    }, right: {
      get: function () {
        return this.x + this.width
      }
    }, cx: {
      get: function () {
        return this.x + this.width / 2
      }
    }, cy: {
      get: function () {
        return this.y + this[Ja] / 2
      }
    }, center: {
      get: function () {
        return new aY(this.cx, this.cy)
      }
    }
  }), uY[p_] = ri, uY[$f] = oi, uY.intersectsPoint = hi;
  var _Y = function (t, i, n, e) {
    1 == arguments.length ? i = n = e = t : 2 == arguments[rh] && (n = t, e = i), this.set(t, i, n, e)
  };
  _Y.prototype = {
    top: 0, bottom: 0, left: 0, right: 0, set: function (t, i, n, e) {
      this.top = t || 0, this[Ca] = i || 0, this[ea] = n || 0, this[na] = e || 0
    }, clone: function () {
      return new _Y(this.top, this[Ca], this.bottom, this[na])
    }, equals: function (t) {
      return t && this.top == t.top && this.bottom == t[ea] && this[Ca] == t[Ca] && this[na] == t[na]
    }
  };
  var dY = function (t, i) {
    this.horizontalPosition = t, this[Kl] = i
  };
  dY[yh] = {
    verticalPosition: !1, horizontalPosition: !1, toString: function () {
      return (this[Jl] || "") + (this[Kl] || "")
    }
  }, Z(dY.prototype, Ql, {
    get: function () {
      return (this[Jl] || "") + (this[Kl] || "")
    }
  });
  var lY = tv, vY = iv, bY = nv, yY = If, gY = ev, xY = sv;
  dY[rv] = new dY(lY, yY), dY[hv] = new dY(lY, gY), dY.LEFT_BOTTOM = new dY(lY, xY), dY[av] = new dY(vY, yY), dY[ov] = new dY(vY, gY), dY[fv] = new dY(vY, xY), dY[cv] = new dY(bY, yY), dY.RIGHT_MIDDLE = new dY(bY, gY), dY.RIGHT_BOTTOM = new dY(bY, xY);
  var pY = [dY.LEFT_TOP, dY.LEFT_MIDDLE, dY[uv], dY[av], dY[ov], dY[fv], dY[cv], dY[_v], dY[dv]];
  Z(dY, zh, {
    get: function () {
      return pY[H(pY[rh])]
    }
  }), dY[aa] = function (t) {
    for (var i in dY) {
      var n = dY[i];
      if (n && zh != i && n instanceof dY && n.toString() == t) return n
    }
    throw new Error("Position not be supported - " + t)
  };
  var EY = function (t, i, n, e, s) {
    this.set(t, i, n, e), this[lv] = s
  };
  EY.prototype = {
    radius: 0, classify: function (t, i, n, e) {
      return i > t ? 0 : i + e > t ? 1 : n - e > t ? 2 : n > t ? 3 : 4
    }, intersectsRect: function (t, i, n, e) {
      if (T(this, EY, Xl, arguments) === !1) return !1;
      var s = this.x, r = this.y, h = s + this[ha], a = r + this.height, o = 2 * radius, f = 2 * radius,
        c = Math.min(this[ha], Math.abs(o)) / 2, u = Math.min(this[Ja], Math.abs(f)) / 2, _ = this[vv](t, s, h, c),
        d = this[vv](t + n, s, h, c), l = this[vv](i, r, a, u), v = this.classify(i + e, r, a, u);
      return 2 == _ || 2 == d || 2 == l || 2 == v ? !0 : 2 > _ && d > 2 || 2 > l && v > 2 ? !0 : (t = 1 == d ? t = t + n - (s + c) : t -= h - c, i = 1 == v ? i = i + e - (r + u) : i -= a - u, t /= c, i /= u, 1 >= t * t + i * i)
    }, intersectsPoint: function (t, i) {
      if (T(this, EY, jo, arguments) === !1) return !1;
      var n = this.x, e = this.y, s = n + this[ha], r = e + this.height;
      if (n > t || e > i || t >= s || i >= r) return !1;
      var h = 2 * radius, a = 2 * radius, o = Math.min(this[ha], Math.abs(h)) / 2,
        f = Math.min(this.height, Math.abs(a)) / 2;
      return t >= (n += o) && t < (n = s - o) ? !0 : i >= (e += f) && i < (e = r - f) ? !0 : (t = (t - n) / o, i = (i - e) / f, 1 >= t * t + i * i)
    }, clone: function () {
      return new EY(this.x, this.y, this.width, this.height, this[lv])
    }
  }, m(EY, uY);
  var mY = function (t, i, n, e) {
    this.source = t, this[Fo] = i, this.kind = n, this.value = e
  };
  mY[yh] = {
    source: null, type: null, kind: null, value: null, toString: function () {
      return bv + this[mf] + yv + this[Fo] + gv + this.kind
    }
  };
  var wY = function (t, i, n, e, s) {
    this[mf] = t, this[rl] = i, this.oldValue = e, this[Xu] = n, this[xv] = s
  };
  wY[yh] = {
    type: pv, propertyType: null, toString: function () {
      return bv + this[mf] + yv + this.type + Ev + this[rl] + mv + this[wv] + Tv + this.value
    }
  }, m(wY, mY), Z(wY[yh], kv, {
    get: function () {
      return this[rl]
    }, set: function (t) {
      this[rl] = t
    }
  });
  var TY = function (t, i, n) {
    this.source = t, this[wv] = t.parent, this.value = i, this[Mv] = n, this.oldValue && (this.oldIndex = this.oldValue[Ov](t))
  };
  TY.prototype = {kind: su}, m(TY, wY);
  var kY = function (t, i) {
    this.source = t, this[Xu] = i
  };
  kY[yh].kind = Iv, m(kY, wY);
  var MY = function (t, i) {
    this[mf] = t, this[Xu] = i
  };
  MY.prototype[rl] = Sv, m(MY, wY);
  var OY = function (t, i, n, e) {
    this.source = i, this[wv] = n, this[Xu] = e, this[su] = t, this[Av] = i, this[jv] = n, this.newIndex = e
  };
  OY[yh].kind = Pv, m(OY, wY);
  var IY = function () {
  };
  IY.prototype = {
    listener: null, beforeEvent: function (t) {
      return null != this[Cv] && this.listener.beforeEvent ? this.listener[Th](t) : !0
    }, onEvent: function (t) {
      null != this.listener && this[Cv][Lv] && this[Cv][Lv](t)
    }
  };
  var SY = function () {
    w(this, SY, arguments), this[Dv] = {}, this[Rv] = []
  }, AY = function (t, i) {
    this[Cv] = t, this[Nv] = i, t instanceof Function ? this.onEvent = t : (this.onEvent = t[Lv], this[Th] = t[Th]), this.equals = function (t) {
      return t && this[Cv] == t[Cv] && this[Nv] == t[Nv]
    }
  };
  AY[yh] = {
    equals: function (t) {
      return t && this.listener == t.listener && this[Nv] == t[Nv]
    }, destroy: function () {
      delete this[Nv], delete this[Cv]
    }
  }, SY[yh] = {
    listeners: null, _nc0: function () {
      return this[Rv] && this[Rv][rh] > 0
    }, _76: function (t, i) {
      return t instanceof SY ? t : new AY(t, i)
    }, _9m: function (t, i) {
      if (t instanceof SY) return this.listeners.indexOf(t);
      for (var n = this[Rv], e = 0, s = n[rh]; s > e; e++) {
        var r = n[e];
        if (r[Cv] == t && r[Nv] == i) return e
      }
      return -1
    }, contains: function (t, i) {
      return this._9m(t, i) >= 0
    }, addListener: function (t, i) {
      return this[B_](t, i) ? !1 : void this[Rv][Io](this._76(t, i))
    }, removeListener: function (t, i) {
      var n = this._9m(t, i);
      n >= 0 && this[Rv].splice(n, 1)
    }, on: function (t, i) {
      this[ol](t, i)
    }, un: function (t, i, n) {
      this[Bv](t, i, n)
    }, onEvent: function (t) {
      return this[Rv] ? void l(this.listeners, function (i) {
        i[Lv] && (i.scope ? i.onEvent.call(i[Nv], t) : i.onEvent(t))
      }, this) : !1
    }, beforeEvent: function (t) {
      return this[Rv] ? l(this[Rv], function (i) {
        return i.beforeEvent ? i.scope ? i[Th][ah](i[Nv], t) : i[Th](t) : !0
      }, this) : !0
    }, _dp: function (t) {
      return this[Th](t) === !1 ? !1 : (this[Lv](t), !0)
    }, clear: function () {
      this.listeners = []
    }, destroy: function () {
      this[Qa]()
    }
  }, m(SY, IY);
  var jY = {
    onEvent: function () {
    }, beforeEvent: function () {
    }
  }, PY = function (t, i, n, e, s) {
    this[mf] = t, this[Fo] = $v, this[rl] = i, this[Ro] = n, this[Mf] = e, this.oldIndex = s
  };
  PY.prototype = {
    index: -1, oldIndex: -1, toString: function () {
      return bv + this.source + yv + this.type + gv + this[rl] + Fv + this[Ro] + Gv + this[Mf] + zv + this.oldIndex
    }
  }, m(PY, mY), PY[Hv] = Yv, PY[Uv] = _h, PY.KIND_CLEAR = Qa, PY[Wv] = qv;
  var CY = function () {
    this.id = ++FH, this[Xv] = {}
  };
  CY.prototype = {
    _naz: null, id: null, get: function (t) {
      return this[Xv][t]
    }, set: function (t, i) {
      var n = this.get(t);
      if (n === i) return !1;
      var e = new wY(this, t, i, n);
      return e[xv] = gU[i_], this._nc1(t, i, e, this[Xv])
    }, _nc1: function (t, i, e, s) {
      return this[Th](e) === !1 ? !1 : (s || (s = this[Xv]), i === n ? delete s[t] : s[t] = i, this[Lv](e), !0)
    }, remove: function (t) {
      this.set(t, null)
    }, valueOf: function () {
      return this.id
    }, toString: function () {
      return this.id
    }, _dg: function (t, i) {
      if (i === n && (i = -1), this == t || t == this._jy) return !1;
      if (t && this == t._jy && !t._dg(null)) return !1;
      var e = new TY(this, t, i);
      if (!this.beforeEvent(e)) return !1;
      var s, r, h = this._jy;
      return t && (s = new kY(t, this), !t.beforeEvent(s)) ? !1 : null == h || (r = new MY(h, this), h[Th](r)) ? (this._jy = t, null != t && _i(t, this, i), null != h && di(h, this), this[Lv](e), null != t && t[Lv](s), null != h && h[Lv](r), this.onParentChanged(h, t), !0) : !1
    }, addChild: function (t, i) {
      var n = t._dg(this, i);
      return n && this[fa](t, i), n
    }, removeChild: function (t) {
      if (!this._fi || !this._fi[B_](t)) return !1;
      var i = t._dg(null);
      return this.onChildRemove(t), i
    }, toChildren: function () {
      return this._fi ? this._fi[Nl]() : null
    }, clearChildren: function () {
      if (this._fi && this._fi.length) {
        var t = this[Vv]();
        l(t, function (t) {
          t._dg(null)
        }, this), this[Zv](t)
      }
    }, forEachChild: function (t, i) {
      return this[eh]() ? this._fi[Dc](t, i) : !1
    }, onChildAdd: function () {
    }, onChildRemove: function () {
    }, onChildrenClear: function () {
    }, onParentChanged: function () {
    }, getChildIndex: function (t) {
      return this._fi && this._fi.length ? this._fi[lh](t) : -1
    }, setChildIndex: function (t, i) {
      if (!this._fi || !this._fi[rh]) return !1;
      var n = this._fi.indexOf(t);
      if (0 > n || n == i) return !1;
      var e = new OY(this, t, n, i);
      return this.beforeEvent(e) === !1 ? !1 : (this._fi.remove(t) && this._fi.add(t, i), this.onEvent(e), !0)
    }, hasChildren: function () {
      return this._fi && this._fi[rh] > 0
    }, getChildAt: function (t) {
      return null == this._fi ? null : this._fi._je[t]
    }, isDescendantOf: function (t) {
      if (!t[eh]()) return !1;
      for (var i = this.parent; null != i;) {
        if (t == i) return !0;
        i = i[su]
      }
      return !1
    }, firePropertyChangeEvent: function (t, i, n, e) {
      this[Lv](new wY(this, t, i, n, e))
    }
  }, m(CY, IY), K(CY[yh], {
    childrenCount: {
      get: function () {
        return this._fi ? this._fi.length : 0
      }
    }, children: {
      get: function () {
        return this._fi || (this._fi = new sY), this._fi
      }
    }, parent: {
      get: function () {
        return this._jy
      }, set: function (t) {
        this._dg(t, -1)
      }
    }, properties: {
      get: function () {
        return this[Xv]
      }, set: function (t) {
        this._naz != t && (this._naz = t)
      }
    }
  });
  var LY = function () {
    this._je = [], this._ln = {}, this._1d = new SY
  };
  LY.prototype = {
    beforeEvent: function (t) {
      return null != this._1d && this._1d[Th] ? this._1d[Th](t) : !0
    }, onEvent: function (t) {
      return this._1d instanceof Function ? void this._1d(t) : void (null != this._1d && this._1d.onEvent && this._1d[Lv](t))
    }, _1d: null, setIndex: function (t, i) {
      if (!this[B_](t)) throw new Error(Fa + t[Kv]() + Cl);
      var n = this[lh](t);
      if (n == i) return !1;
      var e = new PY(this, PY[Wv], t, i, n);
      return this[Th](e) === !1 ? !1 : (this._je.remove(t) >= 0 && this._je.add(i, t), this[Lv](e), !0)
    }, _f7: function (t, i) {
      if (0 == t.length) return !1;
      var e = !1, s = i >= 0, r = new PY(this, PY[Hv], t, i);
      if (this[Th](r) === !1) return !1;
      var h = [];
      t = t._je || t;
      for (var a = 0, o = t[rh]; o > a; a++) {
        var f = t[a];
        null !== f && f !== n && this._ki(f, i, !0) && (h.push(f), e = !0, s && i++)
      }
      return r[Ro] = h, this[Lv](r), e
    }, _ki: function (t, i, n) {
      if (this[du](t) === !1) return !1;
      if (n) return T(this, LY, Jv, arguments);
      var e = new PY(this, PY[Hv], t, i);
      return this[Th](e) === !1 ? !1 : T(this, LY, Jv, arguments) ? (this._lg(t, e), t) : !1
    }, _lg: function (t, i) {
      this.onEvent(i)
    }, _nc6: function (t) {
      if (0 == t.length) return !1;
      var i = new PY(this, PY[Uv], t);
      if (this.beforeEvent(i) === !1) return !1;
      var e = [], s = !1;
      t = t._je || t;
      for (var r = 0, h = t[rh]; h > r; r++) {
        var a = t[r];
        if (null !== a && a !== n) {
          var o = a.id || a;
          a.id === n && (a = null), this._f9(o, a, !0) && (e.push(a), s = !0)
        }
      }
      return i[Ro] = e, this[Lv](i), s
    }, _f9: function (t, i, n) {
      if (n) return T(this, LY, Qv, arguments);
      var e = new PY(this, PY[Uv], i);
      return this.beforeEvent(e) === !1 ? !1 : T(this, LY, Qv, arguments) ? (this[Lv](e), !0) : !1
    }, clear: function () {
      if (this[gc]()) return !1;
      var t = new PY(this, PY[tb], this[Nl]());
      return this[Th](t) === !1 ? !1 : T(this, LY, Qa) ? (this[Lv](t), !0) : !1
    }, accept: function (t) {
      return this.filter && this[ib](t) === !1 ? !1 : !0
    }
  }, m(LY, sY), Z(LY[yh], nb, {
    get: function () {
      return this._1d
    }
  });
  var DY = function () {
    w(this, DY, arguments), this[eb] = new SY, this._selectionModel = new RY(this), this[sb]._1d = this.selectionChangeDispatcher, this[rb] = new SY, this[rb][ol]({
      beforeEvent: this[hb],
      onEvent: this.onDataPropertyChanged
    }, this), this[ab] = new SY, this[ob] = new SY, this[fb] = new sY;
    var t = this;
    this[fb][yu] = function (i, n) {
      if (!t.$roots.contains(i)) throw new Error(Fa + i.id + Cl);
      var e = t.$roots._je[lh](i);
      if (e == n) return !1;
      t[fb]._je.splice(e, 1), t[fb]._je[oh](n, 0, i), t[cb] = !0;
      var s = new OY(t, i, e, n);
      return t._26(s), !0
    }
  };
  DY[yh] = {
    selectionModel: null,
    selectionChangeDispatcher: null,
    dataChangeDispatcher: null,
    parentChangeDispatcher: null,
    roots: null,
    _lg: function (t, i) {
      t[Cv] = this.dataChangeDispatcher, t[su] || this.$roots.add(t), this.onEvent(i)
    },
    _f9: function (t, i) {
      if (T(this, DY, Qv, arguments)) {
        if (i instanceof wq) i.disconnect(); else if (i instanceof Tq) {
          var n = i[ub]();
          this[_h](n)
        }
        var e = i[su];
        return null == e ? this[fb].remove(i) : (e.removeChild(i), e[_b] = !0), i.hasChildren() && this[_h](i[Vv]()), i[Cv] = null, !0
      }
      return !1
    },
    _5i: function (t) {
      var i = t[mf];
      this[B_](i) && (null == i.parent ? this[fb].add(i) : null == t[wv] && this[fb][_h](i), this[ab].onEvent(t))
    },
    _26: function (t) {
      this.childIndexChangeDispatcher.onEvent(t)
    },
    beforeDataPropertyChange: function (t) {
      return t instanceof TY ? this[ab].beforeEvent(t) : !0
    },
    onDataPropertyChanged: function (t) {
      return t instanceof TY ? (this[cb] = !0, t[mf]._naqIndexFlag = !0, void this._5i(t)) : void (t instanceof OY && (this._naqIndexFlag = !0, t.source[cb] = !0, this._26(t)))
    },
    toRoots: function () {
      return this[fb].toDatas()
    },
    _fc: function (t) {
      var i, n = t._jy;
      i = n ? n._fi : this[fb];
      var e = i[lh](t);
      if (0 > e) throw new Error(db + t + "' not exist in the box");
      return 0 == e ? n : i[Pl](e - 1)
    },
    _fe: function (t) {
      var i, n = t._jy;
      i = n ? n._fi : this.$roots;
      var e = i.indexOf(t);
      if (0 > e) throw new Error(db + t + "' not exist in the box");
      return e == i[rh] - 1 ? n ? this._fe(n) : null : i[Pl](e + 1)
    },
    forEachByDepthFirst: function (t, i, n) {
      return this.$roots.length ? r(this[fb], t, i, n) : !1
    },
    forEachByDepthFirstReverse: function (t, i, n) {
      return this[fb][rh] ? o(this[fb], t, i, n) : !1
    },
    forEachByBreadthFirst: function (t, i) {
      return this.$roots[rh] ? u(this.$roots, t, i) : !1
    },
    forEachByBreadthFirstReverse: function (t, i) {
      return this.$roots.length ? _(this.$roots, t, i) : !1
    },
    clear: function () {
      return T(this, DY, Qa) ? (this[fb].clear(), this[wd].clear(), !0) : !1
    }
  }, m(DY, LY), K(DY.prototype, {
    selectionModel: {
      get: function () {
        return this._selectionModel
      }
    }, roots: {
      get: function () {
        return this.$roots
      }
    }
  });
  var RY = function (t) {
    w(this, RY), this.box = t, this._naoxChangeListener = {
      onEvent: function (t) {
        PY[Uv] == t[rl] ? null != t.data ? this[_h](t.data) : null != t[al] && this[_h](t.datas) : PY.KIND_CLEAR == t[rl] && this[Qa]()
      }
    }, this.box[nb][ol](this[lb], this)
  };
  RY[yh] = {
    box: null, isSelected: function (t) {
      return this[Ll](t.id || t)
    }, select: function (t) {
      return this.add(t)
    }, unselect: function (t) {
      return this[_h](t)
    }, reverseSelect: function (t) {
      return this[B_](t) ? this[_h](t) : this.add(t)
    }, accept: function (t) {
      return this.box[B_](t)
    }
  }, m(RY, LY);
  var NY = null, BY = null, $Y = function () {
    if (!i[oo]) return function (t) {
      return t
    };
    var t = i[oo](W_), e = t[va], s = {};
    return function (t) {
      if (s[t]) return s[t];
      var i = li(t);
      return e[i] !== n || BY && e[i = li(BY + i)] !== n ? (s[t] = i, i) : t
    }
  }(), FY = {};
  !function () {
    if (!i[vb]) return !1;
    for (var e = i[vb], s = "Webkit Moz O ms Khtml"[jh](Ph), r = 0; r < s[rh]; r++) if (e[va][s[r] + bb] !== n) {
      BY = da + s[r][yb]() + da;
      break
    }
    var h = i[oo](va);
    t[gb] || h.appendChild(i.createTextNode("")), h[Fo] = xb, h.id = pb, e.appendChild(h), NY = h[Eb];
    var a, o;
    for (var f in FY) {
      var c = FY[f];
      a = f, o = "";
      for (var u in c) o += $Y(u) + ba + c[u] + mb;
      xi(a, o)
    }
  }();
  var GY = function (t, i, n, e, s) {
    if (s) {
      var r = function (t) {
        r.handle[ah](r[Nv], t)
      };
      return r[Nv] = s, r.handle = n, t.addEventListener(i, r, e), r
    }
    return t[wb](i, n, e), n
  }, zY = function (t, i, n) {
    t[Tb](i, n)
  };
  if (eY[kb] = 200, eY.LONG_PRESS_INTERVAL = 800, eY[Mb] = !0, t.navigator && navigator[bl]) {
    var HY, YY = /mobile|tablet|ip(ad|hone|od)|android/i, UY = V_ in t, WY = UY && YY[Zo](navigator.userAgent);
    if (WY) HY = Ob; else {
      var qY = Ib in t ? "mousewheel" : Sb;
      HY = Ab + qY, UY && (HY += jb)
    }
    HY = HY[jh](/[\s,]+/);
    var XY = function (i) {
      return t[Pb] && i instanceof t[Pb]
    }, VY = function () {
      return eY.DOUBLE_CLICK_INTERVAL_TIME
    }, ZY = function () {
      return eY.LONG_PRESS_INTERVAL
    }, F = function (t) {
      t[Nh] ? t[Nh]() : t.returnValue = !1
    }, G = function (t) {
      t.stopPropagation && t.stopPropagation(), t.cancelBubble = !0
    }, z = function (t) {
      F(t), G(t)
    }, KY = function (t) {
      return t.defaultPrevented || t[Bh] === !1
    }, JY = function (t) {
      eU._n9urrentItem && eU[Cb]._onWindowMouseMove(t)
    }, QY = function (t) {
      if (eU[Cb]) {
        var i = eU[Cb];
        i._onWindowMouseUp(t), tU(null)
      }
    }, tU = function (t) {
      eU[Cb] != t && (eU[Cb] && (eU[Cb][Lb] = !1), eU[Cb] = t)
    }, iU = function (i, n) {
      HY[Dc](function (t) {
        i.addEventListener(t, n, !1)
      }), iY || eU[Db] || (eU[Db] = !0, t.addEventListener(Rb, JY, !0), t.addEventListener(Nb, QY, !0))
    }, nU = function (t, i) {
      HY.forEach(function (n) {
        t[Tb](n, i, !1)
      })
    };
    wi.prototype = {
      _install: function () {
        this.__ncction || (this[Bb] = function (t) {
          this._ncction(t)
        }.bind(this), iU(this._lz, this[Bb]))
      }, _uninstall: function () {
        this.__ncction && nU(this._lz, this[Bb])
      }, _ncction: function (t) {
        t = this[$b](t);
        var i = t[Fo];
        this[Fb](t, i) === !1 && this[Gb](t, zb + i)
      }, _n9ancelLongPressTimer: function () {
        this.__longPressTimer && (clearTimeout(this[Hb]), this.__longPressTimer = null)
      }, __knLongPress: function (t) {
        this[Yb] || (this[Yb] = function () {
          this[Ub] && (this.__n9ancelClick = !0, this._knEvent.button ? this._onEvent(this[Ub], Wb) : this._onEvent(this[Ub], qb))
        }[el](this)), this[Xb](), this[Hb] = setTimeout(this[Yb], ZY(t))
      }, __fixTouchEvent: function (t) {
        for (var i, n, e = 0, s = 0, r = t[Ma][rh], h = 0; r > h;) {
          var a = t[Ma][h++], o = a[Oa], f = a[Ia];
          if (2 == h) {
            var c = n[0] - o, u = n[1] - f;
            i = Math[To](c * c + u * u)
          }
          n = [o, f], e += o, s += f
        }
        t.cx = e / r, t.cy = s / r, t[Vb] = {x: t.cx, y: t.cy, clientX: t.cx, clientY: t.cy}, t[Yl] = i
      }, __touchCountChange: function (t) {
        this._dragPoints[Qa](), this._98 = mi(t, this._lz)
      }, _handleTouchEvent: function (t, i) {
        switch (i) {
          case"touchstart":
            G(t), this.__fixTouchEvent(t), this[Zb](t);
            var n = t.touches[rh];
            this[Ub] || (this._knEvent = t, this[Kb](t), this.__n9ancelClick = !1, this[Jb](t)), 1 == n && (this[Qb] = null), n >= 2 && !this.__knMulTouchEvent && (this[Qb] = {
              cx: t.cx,
              cy: t.cy,
              distance: t[Yl]
            });
            break;
          case"touchmove":
            z(t), this.__fixTouchEvent(t);
            var n = t[Ma][rh];
            if (n >= 2 && this[Qb]) {
              var e = this.__knMulTouchEvent[Yl];
              t[ty] = t.distance / e, t[iy] = this[Qb][ny] ? t[ty] / this[Qb].prevScale : t[ty], this[Qb][ny] = t[ty], this[ey] || (this[ey] = !0, this[Gb](t, sy))
            }
            this[Lb] || (this.__dragging = !0, this[ry](t)), this[hy](t), this[ey] && this[Gb](t, ay);
            break;
          case"touchend":
            z(t);
            var n = t[Ma][rh];
            n && (this[oy](t), this[Zb](t)), 1 >= n && (this[ey] && (this[ey] = !1, this[Gb](t, fy)), this[Qb] = null), 0 == n && (this[Lb] ? (this[cy](t), this[Lb] = !1) : t[La] - this[Ub][La] < .8 * VY(t) && this.__onclick(this._knEvent), this._onrelease(t));
            break;
          case"touchcancel":
            this.__dragging = !1, this.__pinching = !1, this.__knMulTouchEvent = null
        }
        return !1
      }, _handleEvent: function (t, i) {
        if (XY(t)) return this._handleTouchEvent(t, i);
        if (uy == i) G(t), tU(this), this._98 = mi(t, this._lz), this[Ub] || (this[Ub] = t, this._onstart(t)), this[_y] = !1, this[Jb](t); else if (Nb == i) tU(), this[dy](t); else if (ly == i) {
          if (this[_y]) return !0;
          if (this[vy]()) return this[by](t), !0
        } else if (yy == i) {
          if (this._isDelayClick()) return !0
        } else {
          if (gy == i) return this[Gb](t, xy), this[Ub] && KY(t) && tU(this), !0;
          if (i == qY) {
            var e = t.wheelDelta;
            if (e !== n ? e /= 120 : e = -t[py], !e) return;
            return t[Ey] = e, this[Gb](t, Ib)
          }
        }
        return !1
      }, _onEvent: function (t, i) {
        if (this[Ra]) {
          var n = this._handler;
          if (i = i || t[Fo], n instanceof Function) return n(t, i);
          if (!(n.accept instanceof Function && n[du](i, t) === !1)) return n.onevent instanceof Function && n[my](i, t, this._scope || this._lz), n[i] instanceof Function ? n[i].call(n, t, this._scope || this._lz) : void 0
        }
      }, _toQEvent: function (t) {
        return t
      }, _onWindowMouseUp: function (t) {
        this.__dragging && (z(t), this.__dragging = !1, t = this[$b](t), this[cy](t), this._onrelease(t), this[Gb](t, wy))
      }, _knDragDistance: 4, _onWindowMouseMove: function (t) {
        if (this._knEvent) {
          if (z(t), !this[Lb]) {
            var i = this[Ub][Ty] - t.screenX, n = this[Ub][ky] - t[ky], e = i * i + n * n;
            if (e < this[My]) return;
            this[Lb] = !0, this[ry](t)
          }
          this[hy](this._toQEvent(t))
        }
      }, _isDelayClick: function () {
        return eY.DELAY_CLICK
      }, __onclick: function (t) {
        if (!this[_y]) {
          var i = VY(t);
          this.__n9lickTimer ? this.__dblclicked || (clearTimeout(this[Oy]), this[Oy] = null, this._onEvent(t, Iy), this[Sy] = !0) : (this.__dblclicked = !1, this.__n9lickTimer = setTimeout(function (t) {
            this[Oy] = null, this[Sy] || this._onEvent(t, Z_)
          }.bind(this, t, i), i))
        }
      }, _onstart: function (t) {
        t[Ay] ? this[Gb](t, jy) : this._onEvent(t, Py)
      }, _onrelease: function (t) {
        this[Ub] && (this[Xb](), t[Ay] ? this[Gb](t, Cy) : this[Gb](t, Ly), this._knEvent = null, this._98 = null)
      }, _ncppendDragInfo: function (t) {
        var i = this._98;
        this._98 = mi(t, this._lz), this[Dy].add(i, this._98, t)
      }, _kndrag: function () {
        this.__n9ancelClick = !0, this[Xb](), this._knEvent.button ? this[Gb](this[Ub], Ry) : this[Gb](this[Ub], Ny)
      }, _ondrag: function (t) {
        this[By](t), this[Ub][Ay] ? this[Gb](t, $y) : this[Gb](t, Fy)
      }, _enddrag: function (t) {
        if (t.timeStamp - this._98[La] < 100) {
          var i = this[Dy][Gy]();
          i && (t.vx = i.x, t.vy = i.y)
        }
        this._knEvent[Ay] ? this._onEvent(t, zy) : this[Gb](t, Hy), this._dragPoints[Qa]()
      }, _ie: function () {
        this[Yy]()
      }, _l6Status: function () {
        eU._n9urrentItem == this && delete eU._n9urrentItem, this[Xb](), delete this._98, this[Ub] && (delete this[Ub][Uy], delete this._knEvent[A_], delete this[Ub])
      }
    };
    var eU = I(function (t) {
      this._kp = t, wi[bh](this, [t[Wy], null, t])
    }, {
      "super": wi, _mvData: function (t) {
        return this._kp[qy](t)
      }, _le: function (t) {
        return this._kp.getUIByMouseEvent(t)
      }, _toQEvent: function (i) {
        return (i instanceof MouseEvent || t.TouchEvent && i instanceof t.TouchEvent) && (i[Uy] = this[Xy][el](this, i), i.getUI = this._le[el](this, i)), i
      }, _onElementRemoved: function (t) {
        this._irListeners(function (i) {
          i[Vy] instanceof Function && i.onElementRemoved(t, this._kp)
        })
      }, _onElementClear: function () {
        this[Zy](function (t) {
          t.onClear instanceof Function && t[Ky](this._kp)
        })
      }, _ie: function (t) {
        this[Jy] && this[Qy](this[Jy], t), this[tg] && this._ieInteractions(this[tg], t), this[Yy]()
      }, _kp: null, _1zs: null, _n9ustomInteractionListeners: null, _mxInteraction: function (t) {
        return this[Jy] == t ? !1 : (this[Jy] && this._1zs.length && this._ieInteractions(this._1zs), void (this[Jy] = t))
      }, _n1CustomInteractionListener: function (t) {
        this[tg] || (this[tg] = []), this[tg][Io](t)
      }, _jrCustomInteractionListener: function (t) {
        this[tg] && 0 != this[tg][rh] && x(this._n9ustomInteractionListeners, t)
      }, _onEvent: function (t, i, n) {
        this._kp[i] instanceof Function && this._kp[i][ah](this._kp, t, n), this[Jy] && this[ig](t, i, this[Jy], n), this[tg] && this[ig](t, i, this[tg], n)
      }, _irListeners: function (t) {
        this[Jy] && l(this[Jy], t, this), this[tg] && l(this._n9ustomInteractionListeners, t, this)
      }, __onEvent: function (t, i, n, e) {
        if (!$(n)) return void this[ng](t, i, n, e);
        for (var s = 0; s < n[rh]; s++) {
          var r = n[s];
          this[ng](t, i, r, e)
        }
      }, __handleEvent: function (t, i, n, e) {
        if (!(n[du] instanceof Function && n.accept(i, t, this._kp, e) === !1)) {
          n.onevent instanceof Function && n[my](i, t, this._kp, e);
          var s = n[i];
          s instanceof Function && s[ah](n, t, this._kp, e)
        }
      }, _ieInteraction: function (t) {
        t.destroy instanceof Function && t[eg][ah](t, this._kp)
      }, _ieInteractions: function (t, i) {
        if (!$(t)) return void this._ieInteraction(t, i);
        for (var n = 0; n < t[rh]; n++) {
          var e = t[n];
          e && this[sg](e, i)
        }
      }
    })
  }
  ki[yh] = {
    limitCount: 10, points: null, add: function (t, i, n) {
      0 == this[$a][rh] && (this[rg] = t.x, this[hg] = t.y);
      var e = i[La] - t[La] || 1;
      n[ag] = e;
      var s = i.x - t.x, r = i.y - t.y;
      n.dx = s, n.dy = r, n.startX = this[rg], n[og] = this[hg], n[fg] = i.x - this[rg], n.totalDeltaY = i.y - this[hg], this[$a][oh](0, 0, {
        interval: e,
        dx: s,
        dy: r
      }), this[$a].length > this[cg] && this[$a].pop()
    }, getCurrentSpeed: function () {
      if (!this.points[rh]) return null;
      for (var t = 0, i = 0, n = 0, e = 0, s = this.points[rh]; s > e; e++) {
        var r = this[$a][e], h = r[ag];
        if (h > 150) {
          t = 0;
          break
        }
        if (t += h, i += r.dx, n += r.dy, t > 300) break
      }
      return 0 == t || 0 == i && 0 == n ? null : {x: i / t, y: n / t}
    }, clear: function () {
      this.points = []
    }
  };
  var sU, rU, hU, aU;
  WH ? (sU = ug, rU = _g, hU = dg, aU = lg) : qH ? (sU = vg, rU = bg, hU = yg, aU = gg) : (sU = xg, rU = xg, hU = Sd, aU = pg);
  var oU = Eg, fU = Math.PI, cU = Math.pow, uU = Math.sin, _U = 1.70158, dU = {
    swing: function (t) {
      return -Math.cos(t * fU) / 2 + .5
    }, easeNone: function (t) {
      return t
    }, easeIn: function (t) {
      return t * t
    }, easeOut: function (t) {
      return (2 - t) * t
    }, easeBoth: function (t) {
      return (t *= 2) < 1 ? .5 * t * t : .5 * (1 - --t * (t - 2))
    }, easeInStrong: function (t) {
      return t * t * t * t
    }, easeOutStrong: function (t) {
      return 1 - --t * t * t * t
    }, easeBothStrong: function (t) {
      return (t *= 2) < 1 ? .5 * t * t * t * t : .5 * (2 - (t -= 2) * t * t * t)
    }, elasticIn: function (t) {
      var i = .3, n = i / 4;
      return 0 === t || 1 === t ? t : -(cU(2, 10 * (t -= 1)) * uU(2 * (t - n) * fU / i))
    }, elasticOut: function (t) {
      var i = .3, n = i / 4;
      return 0 === t || 1 === t ? t : cU(2, -10 * t) * uU(2 * (t - n) * fU / i) + 1
    }, elasticBoth: function (t) {
      var i = .45, n = i / 4;
      return 0 === t || 2 === (t *= 2) ? t : 1 > t ? -.5 * cU(2, 10 * (t -= 1)) * uU(2 * (t - n) * fU / i) : cU(2, -10 * (t -= 1)) * uU(2 * (t - n) * fU / i) * .5 + 1
    }, backIn: function (t) {
      return 1 === t && (t -= .001), t * t * ((_U + 1) * t - _U)
    }, backOut: function (t) {
      return (t -= 1) * t * ((_U + 1) * t + _U) + 1
    }, backBoth: function (t) {
      return (t *= 2) < 1 ? .5 * t * t * (((_U *= 1.525) + 1) * t - _U) : .5 * ((t -= 2) * t * (((_U *= 1.525) + 1) * t + _U) + 2)
    }, bounceIn: function (t) {
      return 1 - dU.bounceOut(1 - t)
    }, bounceOut: function (t) {
      var i, n = 7.5625;
      return i = 1 / 2.75 > t ? n * t * t : 2 / 2.75 > t ? n * (t -= 1.5 / 2.75) * t + .75 : 2.5 / 2.75 > t ? n * (t -= 2.25 / 2.75) * t + .9375 : n * (t -= 2.625 / 2.75) * t + .984375
    }, bounceBoth: function (t) {
      return .5 > t ? .5 * dU.bounceIn(2 * t) : .5 * dU.bounceOut(2 * t - 1) + .5
    }
  }, lU = function (t) {
    this._j9 = t
  };
  lU[yh] = {
    _j9: null, _81: function () {
      this[mg] instanceof Function && (this[mg](), this._n9allback = null)
    }, _kn: function (t) {
      var i = Date.now();
      this._lm(), this[mg] = t, this._requestID = requestAnimationFrame(function n() {
        var t = Date.now(), e = t - i;
        return !e || this._j9 && this._j9(e) !== !1 ? (i = t, void (this[wg] = requestAnimationFrame(n[el](this)))) : void this._lm()
      }[el](this))
    }, _6g: function () {
    }, _lm: function () {
      return this._requestID ? (this._6g(), this._81(), t[pl](this[wg]), void delete this[wg]) : !1
    }, _em: function () {
      return null != this._requestID
    }
  };
  var vU = function (t, i, n, e) {
    this[Tg] = t, this[Da] = i || this, this._30 = e, n && n > 0 && (this._is = n)
  };
  vU.prototype = {
    _is: 1e3, _30: null, _ep: 0, _lm: function () {
      return this._ep = 0, this._n9u = 0, T(this, vU, kg)
    }, _n9u: 0, _j9: function (t) {
      if (this._ep += t, this._ep >= this._is) return this[Tg][ah](this._scope, 1, (1 - this[Mg]) * this._is, t, this._is), !1;
      var i = this._ep / this._is;
      return this._30 && (i = this._30(i)), this._onStep[ah](this._scope, i, (i - this._n9u) * this._is, t, this._is) === !1 ? !1 : void (this[Mg] = i)
    }
  }, m(vU, lU);
  var bU = function (t) {
    ni(t)
  }, yU = {
    version: Og,
    extend: m,
    doSuperConstructor: w,
    doSuper: T,
    createFunction: function (t, i) {
      return i.bind(t)
    },
    setClass: C,
    appendClass: L,
    removeClass: D,
    forEach: l,
    forEachReverse: b,
    isNumber: R,
    isString: N,
    isBoolean: B,
    isArray: $,
    eventPreventDefault: F,
    eventStopPropagation: G,
    stopEvent: z,
    callLater: j,
    nextFrame: P,
    forEachChild: e,
    forEachByDepthFirst: r,
    forEachByDepthFirstReverse: o,
    forEachByBreadthFirst: u,
    randomInt: H,
    randomBool: Y,
    randomColor: W,
    addEventListener: GY,
    getFirstElementChildByTagName: hY
  };
  yU[Ig] = iY, yU.isIOS = JH, yU[jo] = hi, yU[Sg] = ai, yU[Ag] = uY, yU.Size = cY, yU[jg] = aY, yU[Pg] = _Y, yU[Cg] = mY, yU[Lg] = wY, yU[Dg] = PY, yU[Rg] = IY, yU[Ng] = SY, yU[Bg] = dY, yU[$g] = CY, yU.SelectionModel = RY, yU[Fg] = DY, yU[Gg] = jY, yU[zg] = Ii, yU[Hg] = Mi, yU[Yg] = Oi, yU[Ug] = Ti, yU.calculateDistance = oY, yU.HashList = sY, yU[Wg] = wi, yU.alert = function (t) {
    alert(t)
  }, yU.prompt = function (t, i, n, e) {
    var s = prompt(t, i);
    return s != i && n ? n[ah](e, s) : s
  }, yU[qg] = function (t, i, n) {
    var e = confirm(t);
    return e && i ? i[ah](n) : e
  }, yU[Xg] = xi;
  var gU = {
    IMAGE_ADJUST_FLIP: Vg,
    IMAGE_ADJUST_MIRROR: Zg,
    SELECTION_TYPE_BORDER_RECT: Kg,
    SELECTION_TYPE_BORDER: Jg,
    SELECTION_TYPE_SHADOW: Qg,
    NS_SVG: "http://www.w3.org/2000/svg",
    PROPERTY_TYPE_ACCESSOR: 0,
    PROPERTY_TYPE_STYLE: 1,
    PROPERTY_TYPE_CLIENT: 2,
    EDGE_TYPE_DEFAULT: null,
    EDGE_TYPE_ELBOW: tx,
    EDGE_TYPE_ELBOW_HORIZONTAL: ix,
    EDGE_TYPE_ELBOW_VERTICAL: nx,
    EDGE_TYPE_ORTHOGONAL: ex,
    EDGE_TYPE_ORTHOGONAL_HORIZONTAL: sx,
    EDGE_TYPE_ORTHOGONAL_VERTICAL: rx,
    EDGE_TYPE_HORIZONTAL_VERTICAL: hx,
    EDGE_TYPE_VERTICAL_HORIZONTAL: ax,
    EDGE_TYPE_EXTEND_TOP: ox,
    EDGE_TYPE_EXTEND_LEFT: fx,
    EDGE_TYPE_EXTEND_BOTTOM: cx,
    EDGE_TYPE_EXTEND_RIGHT: ux,
    EDGE_TYPE_ZIGZAG: _x,
    EDGE_CORNER_NONE: ed,
    EDGE_CORNER_ROUND: Bf,
    EDGE_CORNER_BEVEL: dx,
    GROUP_TYPE_RECT: hf,
    GROUP_TYPE_CIRCLE: lx,
    GROUP_TYPE_ELLIPSE: vx,
    SHAPE_CIRCLE: bx,
    SHAPE_RECT: hf,
    SHAPE_ROUNDRECT: yx,
    SHAPE_STAR: gx,
    SHAPE_TRIANGLE: xx,
    SHAPE_HEXAGON: px,
    SHAPE_PENTAGON: Ex,
    SHAPE_TRAPEZIUM: mx,
    SHAPE_RHOMBUS: wx,
    SHAPE_PARALLELOGRAM: Tx,
    SHAPE_HEART: kx,
    SHAPE_DIAMOND: Mx,
    SHAPE_CROSS: Ox,
    SHAPE_ARROW_STANDARD: Ix,
    SHAPE_ARROW_1: Sx,
    SHAPE_ARROW_2: Ax,
    SHAPE_ARROW_3: jx,
    SHAPE_ARROW_4: Px,
    SHAPE_ARROW_5: Cx,
    SHAPE_ARROW_6: Lx,
    SHAPE_ARROW_7: Dx,
    SHAPE_ARROW_8: Rx,
    SHAPE_ARROW_OPEN: Nx
  };
  gU[Bx] = $x, gU[Fx] = Bf, gU[Gx] = zx, gU.LINE_JOIN_TYPE_BEVEL = dx, gU.LINE_JOIN_TYPE_ROUND = Bf, gU.LINE_JOIN_TYPE_MITER = Hx, gU[Yx] = Ux, gU[T_] = Wx, eY.SELECTION_TYPE = gU[qx], eY[Pd] = WY ? 8 : 3, eY[Xx] = 2, eY[Vx] = 7, eY[Zx] = V(3422561023), eY[Kx] = gU[qx], eY[Jx] = 10, eY[Qx] = 10, eY.ARROW_SIZE = 10, eY.IMAGE_MAX_SIZE = 200, eY[tp] = 1.2;
  var xU = t[ip] || 1;
  1 > xU && (xU = 1);
  var pU;
  yU[np] = Ri;
  var EU = VH && !iY, mU = 9, wU = function (t, i, n, e) {
    var s = t - n, r = i - e;
    return s * s + r * r
  };
  rn[yh] = {
    equals: function (t) {
      return this.cx == t.cx && this.cy == t.cy && this.r == t.r
    }
  }, rn[Oo] = function (t, i, n) {
    if (!n) return en(t, i);
    var e = wU(t.x, t.y, i.x, i.y), s = wU(t.x, t.y, n.x, n.y), r = wU(n.x, n.y, i.x, i.y);
    if (e + TU >= s + r) return en(t, i, 0, n);
    if (s + TU >= e + r) return en(t, n, 0, i);
    if (r + TU >= e + s) return en(i, n, 0, t);
    var h;
    Math.abs(n.y - i.y) < 1e-4 && (h = t, t = i, i = h), h = n.x * (t.y - i.y) + t.x * (i.y - n.y) + i.x * (-t.y + n.y);
    var a = (n.x * n.x * (t.y - i.y) + (t.x * t.x + (t.y - i.y) * (t.y - n.y)) * (i.y - n.y) + i.x * i.x * (-t.y + n.y)) / (2 * h),
      o = (i.y + n.y) / 2 - (n.x - i.x) / (n.y - i.y) * (a - (i.x + n.x) / 2);
    return new rn(a, o, oY(a, o, t.x, t.y), t, i, n)
  };
  var TU = .01, kU = {
    _nck: function (t, i, e, s, r) {
      if (N(t) && (t = dY.fromString(t)), !t) return {x: 0, y: 0};
      var h = 0, a = 0, o = i._iy;
      if (e = e || 0, t.x === n) {
        var f = t[Jl], c = t[Kl], u = !0;
        switch (f) {
          case bY:
            u = !1;
            break;
          case vY:
            h += o / 2
        }
        switch (c) {
          case yY:
            a -= e / 2;
            break;
          case xY:
            a += e / 2
        }
      } else h = t.x, a = t.y, Math.abs(h) > 0 && Math.abs(h) < 1 && (h *= o);
      r && null != s && (a += s.y, h += Math.abs(s.x) < 1 ? s.x * o : s.x);
      var _ = dn[ah](i, h, a, u);
      return _ ? (r || null == s || _[ep](s), _) : {x: 0, y: 0}
    }, _lk: function (t, i) {
      var n = i.type, e = i[$a];
      switch (n) {
        case KU:
          t[sp](e[0], e[1], e[2], e[3], i._r);
          break;
        case qU:
          t.moveTo(e[0], e[1]);
          break;
        case XU:
          t[ff](e[0], e[1]);
          break;
        case VU:
          t[rp](e[0], e[1], e[2], e[3]);
          break;
        case ZU:
          t[hp](e[0], e[1], e[2], e[3], e[4], e[5]);
          break;
        case JU:
          t.closePath()
      }
    }, _5b: function (t, i, n, e) {
      var s = i.type;
      if (s != qU && s != JU) {
        var r = n[$o], h = i[$a];
        switch (n[Fo] == qU && t.add(r.x, r.y), s) {
          case KU:
            bn(i, r.x, r.y, h[0], h[1], h[2], h[3], h[4]), t.add(h[0], h[1]), t.add(i[Ho], i._p1y), t.add(i._p2x, i[qo]), i[ap] && t.add(i[ap].x, i[ap].y), i[op] && t.add(i[op].x, i.$boundaryPoint2.y);
            break;
          case XU:
            t.add(h[0], h[1]);
            break;
          case VU:
            Wi([r.x, r.y].concat(h), t);
            break;
          case ZU:
            Ji([r.x, r.y][fh](h), t);
            break;
          case JU:
            e && t.add(e.points[0], e[$a][1])
        }
      }
    }, _5d: function (t, i, n) {
      var e = t[Fo];
      if (e == qU) return 0;
      var s = i[$o], r = t.points;
      switch (e == ZU && 4 == r.length && (e = VU), e) {
        case XU:
          return oY(r[0], r[1], s.x, s.y);
        case KU:
          return t._iy;
        case VU:
          var h = Vi([s.x, s.y][fh](r));
          return t._lf = h, h(1);
        case ZU:
          var h = tn([s.x, s.y][fh](r));
          return t._lf = h, h(1) || Qi([s.x, s.y][fh](r));
        case JU:
          if (s && n) return t[$a] = n[$a], oY(n[$a][0], n.points[1], s.x, s.y)
      }
      return 0
    }
  }, MU = /^data:image\/(\w+);base64,/i, OU = /^gif/i, IU = /^svg/i, SU = 10, AU = 11, jU = 12, PU = 20, CU = 30;
  eY.IMAGE_WIDTH = 50, eY[lf] = 30, eY[fp] = {
    draw: function (t, i) {
      En(t, i[ha], i.height, i[cp])
    }
  }, eY[up] = {
    draw: function (t, i) {
      En(t, i[ha], i[Ja], i.renderColor, !0)
    }
  }, eY[_p] = 1e6;
  var LU = 1, DU = 2, RU = 3;
  Tn.prototype = {
    _j5: 0, _63: !0, _kj: null, _jf: null, _lr: null, _lp: null, _nc4: n, _8u: n, _6a: function () {
      return this._j5 == LU
    }, getBounds: function (t) {
      return this._lp == CU ? this._lr[vf](t) : (this._63 && this._f4(), this)
    }, validate: function () {
      this._63 && this._f4()
    }, _f4: function () {
      if (this._63 = !1, this._lp == CU) return this._lr.validate(), void this[Jf](this._lr[Ao]);
      if (this._lp == PU) return void this._93();
      if (this._j5 != LU) try {
        this._dl()
      } catch (t) {
        this._j5 = RU, yU[wf](t)
      }
    }, _55: function () {
      this._dp(), this[dp][Qa](), delete this._dispatcher
    }, _hu: function (t) {
      this._kj[lp] && this._kj.parentNode.removeChild(this._kj), this._j5 = RU, yU[wf](vp + this._lr), this._pixels = null, this._jf = null, this._kj = null, t !== !1 && this._55()
    }, _dl: function () {
      var t = this._lr;
      if (this._j5 = LU, this[dp] = new SY, this._lp == jU) {
        for (var n in oW) this[n] = oW[n];
        return void se(this._lr, this, this[bp], this._hu, this._ez)
      }
      this._kj || (this._kj = i[oo](X_), this._kj.crossOrigin = yp, HH && (this._kj[va].visibility = nd, i[gp][nu](this._kj))), HH ? (this._kj[$c] = function () {
        setTimeout(this._7s[el](this), 100)
      }.bind(this), this._kj[Gc] = this._hu[el](this)) : (this._kj[$c] = this._7s[el](this), this._kj.onerror = this._7s[el](this)), this._kj.src = t
    }, _hy: !0, _7s: function () {
      var t = this._kj[ha], i = this._kj[Ja];
      if (!t || !i) return void this._hu();
      if (this._kj.parentNode && this._kj.parentNode[xp](this._kj), this._j5 = DU, this[ha] = t, this.height = i, HH && this._lp == AU) this._pixels = null, this._hy = !1; else if (this._lp == AU || pn(this._kj.src)) {
        var n = Ri();
        n.width = t, n[Ja] = i, n.g.drawImage(this._kj, 0, 0, t, i), xn(n.g, n) && (this._jf = n, this[pp] = Cn(n))
      }
      this._55()
    }, _93: function () {
      var t = this._lr;
      if (!(t[pf] instanceof Function)) return void this._hu(!1);
      if (t[Ep] === !1 && t.width && t[Ja]) return this.width = t[ha], void (this[Ja] = t[Ja]);
      var i = t[ha] || eY[mp], n = t.height || eY[mp], e = this._da();
      e[ha] = i, e.height = n;
      var s = e.g;
      t[pf](s);
      var r = Gi(s, 0, 0, i, n);
      if (r) {
        var h = Dn(r[Ro], i, n);
        this.x = h._x, this.y = h._y, this.width = h[wp], this[Ja] = h[Tp], e[ha] = this[ha], e[Ja] = this[Ja], s[Rc](r, -this.x, -this.y), this[pp] = h
      }
    }, _da: function () {
      return this._jf || (this._jf = Ri())
    }, draw: function (t, i, n, e, s, r) {
      if (this.width && this[Ja]) {
        i = i || 1, e = e || 1, s = s || 1;
        var h = this[ha] * e, a = this[Ja] * s;
        if (r && n[ld] && (t[ld] = n[ld], t.shadowBlur = (n[bd] || 0) * i, t[kp] = (n.shadowOffsetX || 0) * i, t[Mp] = (n[Mp] || 0) * i), this._j5 == LU) return void (eY.IMAGE_DEFAULT && eY[fp][pf](t, {
          src: this._lr,
          width: h,
          height: a,
          renderColor: n.renderColor
        }));
        if (this._j5 == RU) return void (eY[up] && eY[up][pf](t, {
          src: this._lr,
          width: h,
          height: a,
          renderColor: n.renderColor
        }));
        if (this._lp == CU) return t.scale(e, s), void this._lr[pf](t, i, n);
        var o = this._fs(i, e, s);
        return o ? ((this.x || this.y) && t[sf](this.x * e, this.y * s), t[ef](e / o[ef], s / o[ef]), void o._lk(t, n.renderColor, n[Op])) : void this._iz(t, i, e, s, this.width * e, this.height * s, n)
      }
    }, _iz: function (t, i, n, e, s, r, h) {
      if (this._lp == PU) return 1 != n && 1 != e && t.scale(n, e), void this._lr.draw(t, h);
      if (this._kj) {
        if (!XH) return void t[xd](this._kj, 0, 0, s, r);
        var n = i * s / this[ha], e = i * r / this.height;
        t.scale(1 / n, 1 / e), t[xd](this._kj, 0, 0, s * n, r * e)
      }
    }, _ja: null, _fs: function (t, i, n) {
      if (this._lp == PU && this._lr[Ep] === !1) return null;
      if (this._lp == SU || (t *= Math.max(i, n)) <= 1) return this[Ip] || (this[Ip] = this._fw(this._jf || this._kj, 1, this._hy)), this[Ip];
      var e = this._ja[Sp] || 0;
      if (t = Math[uh](t), e >= t) {
        for (var s = t, r = this._ja[s]; !r && ++s <= e;) r = this._ja[s];
        if (r) return r
      }
      t % 2 && t++;
      var h = this.width * t, a = this.height * t;
      if (h * a > eY.MAX_CACHE_PIXELS) return null;
      var o = Ri(h, a);
      return (this.x || this.y) && o.g[sf](-this.x * t, -this.y * t), this._iz(o.g, 1, t, t, h, a), this._fw(o, t)
    }, _fw: function (t, i) {
      var n = new nW(t, i);
      return this._ja[i] = n, this._ja[Sp] = i, n
    }, hitTest: function (t, i, n) {
      if (this._lp == CU) return this._lr[$_].apply(this._lr, arguments);
      if (!(this[pp] || this._kj && this._kj[pp])) return !0;
      var e = this[pp] || this._kj._pixels;
      return e._it(t, i, n)
    }, _dp: function () {
      this[dp] && this._dispatcher[Lv](new mY(this, Ap, jp, this._kj))
    }, _nca: function (t, i) {
      this[dp] && this[dp].addListener(t, i)
    }, _6l: function (t, i) {
      this[dp] && this[dp][Bv](t, i)
    }, _nat: function (t) {
      this._ja = {}, (t || this[ha] * this[Ja] > 1e5) && (this._kj = null, this._jf = null)
    }
  }, m(Tn, uY);
  var NU = {};
  yU[xd] = Sn, yU[Pp] = kn, yU[Cp] = On, yU.getAllImages = function () {
    var t = [];
    for (var i in NU) t[Io](i);
    return t
  };
  var BU = function (t, i, n, e, s, r) {
    this[Fo] = t, this.colors = i, this[Lp] = n, this[D_] = e || 0, this.tx = s || 0, this.ty = r || 0
  };
  gU.GRADIENT_TYPE_RADIAL = nv, gU.GRADIENT_TYPE_LINEAR = tv, BU[yh] = {
    type: null,
    colors: null,
    positions: null,
    angle: null,
    tx: 0,
    ty: 0,
    position: dY.CENTER_MIDDLE,
    isEmpty: function () {
      return null == this[Dp] || 0 == this[Dp][rh]
    },
    _6c: function () {
      var t = this[Dp][rh];
      if (1 == t) return [0];
      for (var i = [], n = 1 / (t - 1), e = 0; t > e; e++) i[Io](n * e);
      return this.positions || (this[Lp] = i), i
    },
    generatorGradient: function (t) {
      if (null == this[Dp] || 0 == this.colors.length) return null;
      var i, n = Ni();
      if (this[Fo] == gU.GRADIENT_TYPE_LINEAR) {
        var e = this.angle;
        e > Math.PI && (e -= Math.PI);
        var s;
        if (e <= Math.PI / 2) {
          var r = Math[ta](t.height, t[ha]), h = Math.sqrt(t[ha] * t[ha] + t[Ja] * t[Ja]), a = r - e;
          s = Math.cos(a) * h
        } else {
          var r = Math[ta](t.width, t[Ja]), h = Math.sqrt(t.width * t.width + t[Ja] * t[Ja]), a = r - (e - Math.PI / 2);
          s = Math.cos(a) * h
        }
        var o = s / 2, f = o * Math.cos(e), c = o * Math.sin(e), u = t.x + t.width / 2 - f, _ = t.y + t[Ja] / 2 - c,
          d = t.x + t[ha] / 2 + f, l = t.y + t[Ja] / 2 + c;
        i = n[Rp](u, _, d, l)
      } else {
        if (!(this.type = gU[Np])) return null;
        var v = ui(this[vc], t.width, t[Ja]);
        v.x += t.x, v.y += t.y, this.tx && (v.x += Math.abs(this.tx) < 1 ? t.width * this.tx : this.tx), this.ty && (v.y += Math.abs(this.ty) < 1 ? t[Ja] * this.ty : this.ty);
        var b = oY(v.x, v.y, t.x, t.y);
        b = Math.max(b, oY(v.x, v.y, t.x, t.y + t.height)), b = Math.max(b, oY(v.x, v.y, t.x + t[ha], t.y + t.height)), b = Math.max(b, oY(v.x, v.y, t.x + t.width, t.y)), i = n.createRadialGradient(v.x, v.y, 0, v.x, v.y, b)
      }
      var y = this[Dp], g = this[Lp];
      g && g[rh] == y[rh] || (g = this._6c());
      for (var x = 0, p = y[rh]; p > x; x++) i[Bp](g[x], y[x]);
      return i
    }
  };
  var $U = new BU(gU[$p], [V(2332033023), V(1154272460), V(1154272460), V(1442840575)], [.1, .3, .7, .9], Math.PI / 2),
    FU = new BU(gU[$p], [V(2332033023), V(1154272460), V(1154272460), V(1442840575)], [.1, .3, .7, .9], 0),
    GU = (new BU(gU.GRADIENT_TYPE_LINEAR, [V(1154272460), V(1442840575)], [.1, .9], 0), new BU(gU[Np], [V(2298478591), V(1156509422), V(1720223880), V(1147561574)], [.1, .3, .7, .9], 0, -.3, -.3)),
    zU = [V(0), V(4294901760), V(4294967040), V(4278255360), V(4278250239), V(4278190992), V(4294901958), V(0)],
    HU = [0, .12, .28, .45, .6, .75, .8, 1], YU = new BU(gU.GRADIENT_TYPE_LINEAR, zU, HU),
    UU = new BU(gU[$p], zU, HU, Math.PI / 2), WU = new BU(gU[Np], zU, HU);
  BU[Fp] = $U, BU.LINEAR_GRADIENT_HORIZONTAL = FU, BU.RADIAL_GRADIENT = GU, BU[Gp] = YU, BU[zp] = UU, BU[Hp] = WU;
  var qU = ev, XU = tv, VU = Yp, ZU = iv, KU = Up, JU = Wp;
  gU[qp] = qU, gU.SEGMENT_LINE_TO = XU, gU.SEGMENT_QUAD_TO = VU, gU.SEGMENT_CURVE_TO = ZU, gU[Xp] = KU, gU[Vp] = JU;
  var QU = function (t, i) {
    this.id = ++FH, $(t) ? this[$a] = t : (this[Fo] = t, this[$a] = i)
  };
  QU[yh] = {
    toJSON: function () {
      var t = {type: this.type, points: this[$a]};
      return this[Sf] && (t.invalidTerminal = !0), t
    }, parseJSON: function (t) {
      this[Fo] = t.type, this[$a] = t[$a], this[Sf] = t[Sf]
    }, points: null, type: XU, clone: function () {
      return new QU(this.type, this[$a] ? y(this.points) : null)
    }, move: function (t, i) {
      if (this.points) for (var n = 0, e = this[$a].length; e > n; n++) {
        var s = this.points[n];
        yU[Zp](s) && (this.points[n] += n % 2 == 0 ? t : i)
      }
    }
  }, K(QU[yh], {
    lastPoint: {
      get: function () {
        return this[Fo] == KU ? {x: this[Wo], y: this._p2y} : {
          x: this.points[this[$a].length - 2],
          y: this[$a][this.points[rh] - 1]
        }
      }
    }, firstPoint: {
      get: function () {
        return {x: this[$a][0], y: this[$a][1]}
      }
    }
  }), yU[Kp] = QU;
  var tW = 0;
  eY[Jp] = Ux;
  var iW = function (t) {
    this[Ao] = new uY, this._f6 = t || []
  };
  iW[yh] = {
    toJSON: function () {
      var t = [];
      return this._f6.forEach(function (i) {
        t.push(i.toJSON())
      }), t
    }, parseJSON: function (t) {
      var i = this._f6;
      t[Dc](function (t) {
        i[Io](new QU(t.type, t[$a]))
      })
    }, clear: function () {
      this._f6[rh] = 0, this[Ao][Qa](), this._iy = 0, this._63 = !0
    }, _d6: !0, _65: function (t, i) {
      this._d6 && 0 === this._f6[rh] && t != qU && this._f6.push(new QU(qU, [0, 0])), this._f6[Io](new QU(t, i)), this._63 = !0
    }, add: function (t, i) {
      g(this._f6, t, i), this._63 = !0
    }, removePathSegment: function (t) {
      return t >= this._f6[rh] ? !1 : (this._f6[oh](t, 1), void (this._63 = !0))
    }, moveTo: function (t, i) {
      this._65(qU, [t, i])
    }, lineTo: function (t, i) {
      this._65(XU, [t, i])
    }, quadTo: function (t, i, n, e) {
      this._65(VU, [t, i, n, e])
    }, curveTo: function (t, i, n, e, s, r) {
      this._65(ZU, [t, i, n, e, s, r])
    }, arcTo: function (t, i, n, e, s) {
      this._65(KU, [t, i, n, e, s])
    }, closePath: function () {
      this._65(JU)
    }, _7z: function (t, i, n, e, s) {
      if (e.selectionColor) {
        if (n == gU[qx]) {
          if (!e[Qp]) return;
          return t[ld] = e.selectionColor, t[bd] = e[Qp] * i, t.shadowOffsetX = (e.selectionShadowOffsetX || 0) * i, void (t.shadowOffsetY = (e[tE] || 0) * i)
        }
        if (n == gU[iE]) {
          if (!e[nE]) return;
          t[Ko] = e[eE];
          var r = s.lineWidth || 0;
          s[sE] && (r += 2 * s[sE]), t.lineWidth = e.selectionBorder + r, this._lk(t), t[Lo]()
        }
      }
    }, _63: !0, _f6: null, _iy: 0, lineCap: $x, lineJoin: Bf, draw: function (t, i, n, e, s) {
      t[_d] = n.lineCap || this[_d], t[dd] = n[dd] || this[dd], e && (s || (s = n), this._7z(t, i, s[rE], s, n));
      var r = e && s.selectionType == gU.SELECTION_TYPE_SHADOW;
      n[hE] && (this._lk(t), t.lineWidth = n[No] + 2 * (n.outline || 0), t.strokeStyle = n[hE], t[Lo](), r && (r = !1, t[ld] = aE)), t[No] = 0, this._lk(t), n[oE] && (t[tf] = n[cp] || n[oE], t[Do]()), n[fE] && (t[tf] = n[cE] || n[fE], t[Do]()), n.lineWidth && (t.lineWidth = n.lineWidth, n[Ec] && (n[uE] && (t[Ko] = n[uE], t[Lo](), r && (t.shadowColor = aE)), t[_d] = n[_E] || t.lineCap, t[dd] = n[dE] || t[dd], t[Ec] = n[Ec], t[Mc] = n[Mc]), t[Ko] = n[cp] || n[Ko], t[Lo](), t[Ec] = [])
    }, _lk: function (t) {
      t.beginPath();
      for (var i, n, e = 0, s = this._f6.length; s > e; e++) i = this._f6[e], kU._lk(t, i, n), n = i
    }, invalidate: function () {
      this._63 = !0
    }, validate: function () {
      if (this._63 = !1, this[Ao][Qa](), this._iy = 0, 0 != this._f6[rh]) for (var t, i, n = this._f6, e = 1, s = n[0], r = s, h = n.length; h > e; e++) t = n[e], t[Fo] == qU ? r = t : (kU._5b(this[Ao], t, s, r), i = kU._5d(t, s, r), t._iy = i, this._iy += i), s = t
    }, getBounds: function (t, i) {
      if (this._63 && this.validate(), i = i || new uY, t) {
        var n = t / 2;
        i.set(this[Ao].x - n, this[Ao].y - n, this.bounds[ha] + t, this[Ao].height + t)
      } else i.set(this.bounds.x, this[Ao].y, this.bounds.width, this[Ao].height);
      return i
    }, hitTest: function (t, i, n, e, s, r) {
      return _n.call(this, t, i, n, e, s, r)
    }, toSegments: function () {
      return [][fh](this._f6)
    }, generator: function (t, i, n, e, s) {
      return un.call(this, t, i, n, e, s)
    }, getLocation: function (t, i) {
      return dn[ah](this, t, i || 0)
    }
  }, K(iW[yh], {
    segments: {
      get: function () {
        return this._f6
      }, set: function (t) {
        this.clear(), this._f6 = t
      }
    }, length: {
      get: function () {
        return this._63 && this[So](), this._iy
      }
    }, _empty: {
      get: function () {
        return 0 == this._f6[rh]
      }
    }
  }), Ln[yh] = {
    _$x: function (t, i) {
      var n, e, s, r, h, a = t.length, o = 0, f = 0;
      for (h = 0; a > h; h += 4) if (t[h + 3] > 0) {
        n = (h + 4) / i / 4 | 0;
        break
      }
      for (h = a - 4; h >= 0; h -= 4) if (t[h + 3] > 0) {
        e = (h + 4) / i / 4 | 0;
        break
      }
      for (o = 0; i > o; o++) {
        for (f = n; e > f; f++) if (t[f * i * 4 + 4 * o + 3] > 0) {
          s = o;
          break
        }
        if (s >= 0) break
      }
      for (o = i - 1; o >= 0; o--) {
        for (f = n; e > f; f++) if (t[f * i * 4 + 4 * o + 3] > 0) {
          r = o;
          break
        }
        if (r >= 0) break
      }
      this._x = s, this._y = n, this._width = r - s + 1, this._height = e - n + 1, this._jk = new uY(s, n, this[wp], this[Tp]), this[lE] = this[wp] * this._height, this[vE] = i, this[bE] = t
    }, _e3: function (t, i) {
      return this[bE][4 * (t + this._x + (this._y + i) * this[vE]) + 3]
    }, _it: function (t, i, n) {
      (!n || 1 >= n) && (n = 1), n = 0 | n, t = Math[Bf](t - this._x) - n, i = Math[Bf](i - this._y) - n, n += n;
      for (var e = t, s = i; i + n > s;) {
        for (var e = t; t + n > e;) {
          if (this._e3(e, s)) return !0;
          ++e
        }
        ++s
      }
      return !1
    }
  }, gU[Lf] = yE, gU.BLEND_MODE_MULTIPLY = gE, gU.BLEND_MODE_COLOR_BURN = xE, gU[Rf] = pE, gU[EE] = mE, gU[Nf] = wE, gU[TE] = kE, eY.BLEND_MODE = gU[Rf];
  var nW = function (t, i, n) {
    this._jf = t, this[ef] = i || 1, this._hy = n
  };
  nW.prototype = {
    scale: 1, _jf: null, _ja: null, _hy: !0, _lk: function (t, i, n) {
      if (i && (i = Rn(i)), !i || this._hy === !1) return void t[xd](this._jf, 0, 0);
      if (this._jf instanceof Image) {
        var e = Ri();
        e.width = this._jf[ha], e[Ja] = this._jf[Ja], e.g.drawImage(this._jf, 0, 0, this._jf[ha], this._jf[Ja]), this._jf = e
      }
      this._ja || (this._ja = {});
      var s = i + n, e = this._ja[s];
      if (e || (e = Bn(this._jf, i, n), e || (this._hy = !1), this._ja[s] = e || this._jf), e) if (HH) try {
        t[xd](e, 0, 0)
      } catch (r) {
      } else t[xd](e, 0, 0)
    }
  };
  var eW = function (t, i, n, e, s, r, h, a, o) {
    this._lx = zn(t, i, n, e, s, r, h, a, o)
  }, sW = {
    server: {
      draw: function (t) {
        t[vo](), t[sf](0, 0), t[rf](), t[of](0, 0), t[ff](40, 0), t[ff](40, 40), t[ff](0, 40), t[zf](), t[ME](), t.translate(0, 0), t.translate(0, 0), t[ef](1, 1), t[sf](0, 0), t[Ko] = aE, t.lineCap = $x, t.lineJoin = Hx, t[OE] = 4, t[vo](), t[vo](), t[Eo](), t[vo](), t[Eo](), t[vo](), t[Eo](), t[vo](), t[Eo](), t[vo](), t[Eo](), t[vo](), t[Eo](), t.save(), t[Eo](), t[vo](), t[Eo](), t[vo](), t.restore(), t[vo](), t[Eo](), t.save(), t[Eo](), t[vo](), t[Eo](), t.save(), t.restore(), t[Eo](), t[vo]();
        var i = t[Rp](6.75, 3.9033, 30.5914, 27.7447);
        i[Bp](.0493, IE), i.addColorStop(.0689, SE), i[Bp](.0939, AE), i[Bp](.129, jE), i[Bp](.2266, PE), i.addColorStop(.2556, CE), i[Bp](.2869, LE), i.addColorStop(.3194, DE), i[Bp](.3525, RE), i[Bp](.3695, NE), i[Bp](.5025, BE), i[Bp](.9212, $E), i[Bp](1, FE), t[tf] = i, t.beginPath(), t.moveTo(25.677, 4.113), t[hp](25.361, 2.4410000000000007, 23.364, 2.7940000000000005, 22.14, 2.7990000000000004), t[hp](19.261, 2.813, 16.381, 2.8260000000000005, 13.502, 2.8400000000000003), t[hp](12.185, 2.846, 10.699000000000002, 2.652, 9.393, 2.8790000000000004), t[hp](9.19, 2.897, 8.977, 2.989, 8.805, 3.094), t[hp](8.084999999999999, 3.5109999999999997, 7.436999999999999, 4.1259999999999994, 6.776, 4.63), t.bezierCurveTo(5.718999999999999, 5.436, 4.641, 6.22, 3.6029999999999998, 7.05), t[hp](4.207, 6.5889999999999995, 21.601999999999997, 36.579, 21.028, 37.307), t[hp](22.019, 36.063, 23.009999999999998, 34.819, 24.000999999999998, 33.575), t[hp](24.587999999999997, 32.84, 25.589999999999996, 31.995000000000005, 25.593999999999998, 30.983000000000004), t[hp](25.595999999999997, 30.489000000000004, 25.598, 29.994000000000003, 25.601, 29.500000000000004), t[hp](25.612, 26.950000000000003, 25.622, 24.400000000000006, 25.633, 21.85), t[hp](25.657, 16.318, 25.680999999999997, 10.786000000000001, 25.704, 5.253), t.bezierCurveTo(25.706, 4.885, 25.749, 4.478, 25.677, 4.113), t.bezierCurveTo(25.67, 4.077, 25.697, 4.217, 25.677, 4.113), t[zf](), t[Do](), t[Lo](), t[Eo](), t[vo](), t[vo](), t[tf] = GE, t[rf](), t[of](19.763, 6.645), t[hp](20.002000000000002, 6.643999999999999, 20.23, 6.691999999999999, 20.437, 6.778), t[hp](20.644000000000002, 6.864999999999999, 20.830000000000002, 6.991, 20.985, 7.146999999999999), t.bezierCurveTo(21.14, 7.302999999999999, 21.266, 7.488999999999999, 21.352999999999998, 7.696999999999999), t[hp](21.438999999999997, 7.903999999999999, 21.487, 8.133, 21.487, 8.372), t.lineTo(21.398, 36.253), t[hp](21.397, 36.489, 21.349, 36.713, 21.262, 36.917), t.bezierCurveTo(21.174, 37.121, 21.048000000000002, 37.305, 20.893, 37.458), t.bezierCurveTo(20.738, 37.611, 20.553, 37.734, 20.348, 37.818999999999996), t[hp](20.141, 37.903999999999996, 19.916, 37.95099999999999, 19.679, 37.949), t.lineTo(4.675, 37.877), t[hp](4.4399999999999995, 37.876000000000005, 4.216, 37.827000000000005, 4.012, 37.741), t.bezierCurveTo(3.8089999999999997, 37.653999999999996, 3.6249999999999996, 37.528999999999996, 3.4719999999999995, 37.376), t.bezierCurveTo(3.3179999999999996, 37.221, 3.1939999999999995, 37.037, 3.1079999999999997, 36.833999999999996), t[hp](3.022, 36.629999999999995, 2.9739999999999998, 36.406, 2.9739999999999998, 36.172), t[ff](2.924, 8.431), t.bezierCurveTo(2.923, 8.192, 2.971, 7.964, 3.057, 7.758), t[hp](3.143, 7.552, 3.267, 7.365, 3.4219999999999997, 7.209), t.bezierCurveTo(3.5769999999999995, 7.052999999999999, 3.76, 6.925, 3.965, 6.837), t.bezierCurveTo(4.17, 6.749, 4.396, 6.701, 4.633, 6.7), t.lineTo(19.763, 6.645), t.closePath(), t[Do](), t.stroke(), t[Eo](), t[Eo](), t[vo](), t[tf] = zE, t[rf](), t.arc(12.208, 26.543, 2.208, 0, 6.283185307179586, !0), t.closePath(), t[Do](), t.stroke(), t.restore(), t[vo](), t[tf] = GE, t[rf](), t.arc(12.208, 26.543, 1.876, 0, 6.283185307179586, !0), t.closePath(), t[Do](), t.stroke(), t[Eo](), t.save(), t.fillStyle = zE, t[rf](), t[of](19.377, 17.247), t[hp](19.377, 17.724, 18.991999999999997, 18.108999999999998, 18.516, 18.108999999999998), t.lineTo(5.882, 18.108999999999998), t[hp](5.404999999999999, 18.108999999999998, 5.02, 17.723, 5.02, 17.247), t.lineTo(5.02, 11.144), t[hp](5.02, 10.666, 5.406, 10.281, 5.882, 10.281), t[ff](18.516, 10.281), t[hp](18.993, 10.281, 19.377, 10.666, 19.377, 11.144), t[ff](19.377, 17.247), t.closePath(), t[Do](), t[Lo](), t[Eo](), t[vo](), t[vo](), t[tf] = GE, t.beginPath(), t[of](18.536, 13.176),t.bezierCurveTo(18.536, 13.518, 18.261000000000003, 13.794, 17.919, 13.794),t.lineTo(6.479, 13.794),t[hp](6.1370000000000005, 13.794, 5.861, 13.518, 5.861, 13.176),t[ff](5.861, 11.84),t.bezierCurveTo(5.861, 11.498, 6.137, 11.221, 6.479, 11.221),t[ff](17.918, 11.221),t[hp](18.259999999999998, 11.221, 18.535, 11.497, 18.535, 11.84),t[ff](18.535, 13.176),t.closePath(),t.fill(),t.stroke(),t[Eo](),t[vo](),t[tf] = GE,t[rf](),t.moveTo(18.536, 16.551),t.bezierCurveTo(18.536, 16.892999999999997, 18.261000000000003, 17.168999999999997, 17.919, 17.168999999999997),t[ff](6.479, 17.168999999999997),t[hp](6.1370000000000005, 17.168999999999997, 5.861, 16.892999999999997, 5.861, 16.551),t[ff](5.861, 15.215999999999998),t[hp](5.861, 14.872999999999998, 6.137, 14.596999999999998, 6.479, 14.596999999999998),t[ff](17.918, 14.596999999999998),t[hp](18.259999999999998, 14.596999999999998, 18.535, 14.872999999999998, 18.535, 15.215999999999998),t[ff](18.535, 16.551),t.closePath(),t[Do](),t[Lo](),t[Eo](),t.restore(),t[Eo]()
      }
    }, exchanger2: {
      draw: function (t) {
        t[vo](), t[sf](0, 0), t[rf](), t.moveTo(0, 0), t[ff](40, 0), t[ff](40, 40), t[ff](0, 40), t[zf](), t.clip(), t.translate(0, 0), t.translate(0, 0), t[ef](1, 1), t[sf](0, 0), t[Ko] = aE, t[_d] = $x, t[dd] = Hx, t[OE] = 4, t[vo](), t[vo](), t.restore(), t[vo](), t[Eo](), t[vo](), t[Eo](), t[vo](), t[Eo](), t[vo](), t[Eo](), t[vo](), t[Eo](), t.save(), t[Eo](), t[vo](), t.restore(), t[vo](), t[Eo](), t[vo](), t[Eo](), t[Eo](), t.save();
        var i = t.createLinearGradient(.4102, 24.3613, 39.5898, 24.3613);
        i.addColorStop(0, IE), i[Bp](.0788, PE), i[Bp](.2046, HE), i[Bp](.3649, YE), i.addColorStop(.5432, UE), i[Bp](.6798, WE), i.addColorStop(.7462, qE), i[Bp](.8508, XE), i.addColorStop(.98, CE), i[Bp](1, VE), t.fillStyle = i, t[rf](), t.moveTo(.41, 16.649), t[hp](.633, 19.767, .871, 20.689, 1.094, 23.807000000000002), t.bezierCurveTo(1.29, 26.548000000000002, 3.324, 28.415000000000003, 5.807, 29.711000000000002), t[hp](10.582, 32.202000000000005, 16.477, 32.806000000000004, 21.875999999999998, 32.523), t[hp](26.929, 32.258, 32.806, 31.197000000000003, 36.709999999999994, 27.992000000000004), t[hp](38.30499999999999, 26.728000000000005, 38.83599999999999, 25.103000000000005, 38.998999999999995, 23.161000000000005), t[hp](39.589, 16.135000000000005, 39.589, 16.135000000000005, 39.589, 16.135000000000005), t.bezierCurveTo(39.589, 16.135000000000005, 3.26, 16.647, .41, 16.649), t[zf](), t[Do](), t[Lo](), t[Eo](), t[vo](), t[vo](), t[tf] = GE, t[rf](), t.moveTo(16.4, 25.185), t[hp](12.807999999999998, 24.924999999999997, 9.139, 24.238, 5.857999999999999, 22.705), t[hp](3.175999999999999, 21.450999999999997, -.32200000000000095, 18.971999999999998, .544999999999999, 15.533999999999999), t.bezierCurveTo(1.3499999999999992, 12.335999999999999, 4.987999999999999, 10.495999999999999, 7.807999999999999, 9.428999999999998), t.bezierCurveTo(11.230999999999998, 8.133999999999999, 14.911999999999999, 7.519999999999999, 18.558, 7.345999999999998), t.bezierCurveTo(22.233, 7.169999999999998, 25.966, 7.437999999999998, 29.548000000000002, 8.300999999999998), t[hp](32.673, 9.052999999999999, 36.192, 10.296, 38.343, 12.814999999999998), t.bezierCurveTo(40.86600000000001, 15.768999999999998, 39.208000000000006, 19.066999999999997, 36.406000000000006, 21.043999999999997), t[hp](33.566, 23.046999999999997, 30.055000000000007, 24.071999999999996, 26.670000000000005, 24.676999999999996), t.bezierCurveTo(23.289, 25.28, 19.824, 25.436, 16.4, 25.185), t[hp](13.529, 24.977, 19.286, 25.396, 16.4, 25.185), t[zf](), t[Do](), t[Lo](), t[Eo](), t[Eo](), t[vo](), t[vo](), t[vo](), t.save(), t[vo](), t.fillStyle = ZE, t.beginPath(), t[of](5.21, 21.754), t.lineTo(8.188, 17.922), t[ff](9.53, 18.75), t[ff](15.956, 16.004), t.lineTo(18.547, 17.523), t.lineTo(12.074, 20.334), t.lineTo(13.464, 21.204), t[ff](5.21, 21.754), t[zf](), t[Do](), t[Lo](), t[Eo](), t.restore(), t.restore(), t.save(), t.save(), t[vo](), t[tf] = ZE, t[rf](), t.moveTo(17.88, 14.61), t[ff](9.85, 13.522), t[ff](11.703, 12.757), t.lineTo(7.436, 10.285), t[ff](10.783, 8.942), t.lineTo(15.091, 11.357), t.lineTo(16.88, 10.614), t[ff](17.88, 14.61), t[zf](), t[Do](), t.stroke(), t[Eo](), t[Eo](), t[vo](), t[vo](), t[tf] = ZE, t[rf](), t[of](17.88, 14.61), t[ff](9.85, 13.522), t.lineTo(11.703, 12.757), t[ff](7.436, 10.285), t[ff](10.783, 8.942), t[ff](15.091, 11.357), t[ff](16.88, 10.614), t.lineTo(17.88, 14.61), t.closePath(), t[Do](), t[Lo](), t[Eo](), t[Eo](), t[Eo](),t[vo](),t[vo](),t[vo](),t[tf] = ZE,t.beginPath(),t[of](23.556, 15.339),t[ff](20.93, 13.879),t[ff](26.953, 11.304),t[ff](25.559, 10.567),t[ff](33.251, 9.909),t[ff](31.087, 13.467),t[ff](29.619, 12.703),t[ff](23.556, 15.339),t.closePath(),t.fill(),t.stroke(),t.restore(),t[Eo](),t[Eo](),t[vo](),t.save(),t.save(),t[tf] = ZE,t[rf](),t[of](30.028, 23.383),t.lineTo(24.821, 20.366),t[ff](22.915, 21.227),t[ff](21.669, 16.762),t[ff](30.189, 17.942),t[ff](28.33, 18.782),t[ff](33.579, 21.725),t[ff](30.028, 23.383),t[zf](),t[Do](),t.stroke(),t[Eo](),t[Eo](),t[vo](),t[vo](),t[tf] = ZE,t[rf](),t[of](30.028, 23.383),t.lineTo(24.821, 20.366),t[ff](22.915, 21.227),t[ff](21.669, 16.762),t[ff](30.189, 17.942),t.lineTo(28.33, 18.782),t[ff](33.579, 21.725),t.lineTo(30.028, 23.383),t.closePath(),t[Do](),t[Lo](),t[Eo](),t.restore(),t[Eo](),t[Eo](),t[Eo](),t.restore()
      }
    }, exchanger: {
      draw: function (t) {
        t.save(), t.translate(0, 0), t[rf](), t[of](0, 0), t.lineTo(40, 0), t[ff](40, 40), t[ff](0, 40), t[zf](), t[ME](), t.translate(0, 0), t.translate(0, 0), t.scale(1, 1), t[sf](0, 0), t[Ko] = aE, t[_d] = $x, t.lineJoin = Hx, t[OE] = 4, t[vo](), t.save(), t[Eo](), t[vo](), t[Eo](), t[vo](), t[Eo](), t[vo](), t[Eo](), t[vo](), t[Eo](), t[vo](), t.restore(), t[vo](), t[Eo](), t.restore(), t.save();
        var i = t.createLinearGradient(.2095, 20.7588, 39.4941, 20.7588);
        i[Bp](0, KE), i[Bp](.0788, JE), i.addColorStop(.352, QE), i.addColorStop(.6967, tm), i.addColorStop(.8916, im), i.addColorStop(.9557, nm), i.addColorStop(1, em), t.fillStyle = i, t[rf](), t.moveTo(39.449, 12.417), t.lineTo(39.384, 9.424), t.bezierCurveTo(39.384, 9.424, .7980000000000018, 22.264, .3710000000000022, 23.024), t[hp](-.026999999999997804, 23.733, .4240000000000022, 24.903000000000002, .5190000000000022, 25.647000000000002), t[hp](.7240000000000022, 27.244000000000003, .9240000000000023, 28.841, 1.1350000000000022, 30.437), t[hp](1.3220000000000023, 31.843, 2.7530000000000023, 32.094, 3.9620000000000024, 32.094), t.bezierCurveTo(8.799000000000003, 32.092, 13.636000000000003, 32.091, 18.473000000000003, 32.089), t[hp](23.515, 32.086999999999996, 28.556000000000004, 32.086, 33.598, 32.083999999999996), t.bezierCurveTo(34.859, 32.083999999999996, 36.286, 31.979999999999997, 37.266, 31.081999999999997), t[hp](37.537, 30.820999999999998, 37.655, 30.535999999999998, 37.699999999999996, 30.229999999999997), t[ff](37.711, 30.316999999999997), t.lineTo(39.281, 16.498999999999995), t[hp](39.281, 16.498999999999995, 39.467999999999996, 15.126999999999995, 39.489, 14.666999999999994), t.bezierCurveTo(39.515, 14.105, 39.449, 12.417, 39.449, 12.417), t.closePath(), t[Do](), t.stroke(), t[Eo](), t[vo](), t.save(), t[vo](), t.save(), t[Eo](), t.save(), t[Eo](), t[vo](), t[Eo](), t[vo](), t.restore(), t[vo](), t[Eo](), t[vo](), t[Eo](), t.save(), t[Eo](), t[vo](), t[Eo](), t.save(), t[Eo](), t[Eo](), t[vo]();
        var i = t[Rp](19.8052, 7.7949, 19.8052, 24.7632);
        i[Bp](0, sm), i[Bp](.1455, rm), i[Bp](.2975, hm), i[Bp](.4527, am), i[Bp](.6099, om), i[Bp](.7687, fm), i[Bp](.9268, cm), i[Bp](.9754, um), i.addColorStop(1, _m), t.fillStyle = i, t.beginPath(), t[of](33.591, 24.763), t.bezierCurveTo(23.868000000000002, 24.754, 14.145, 24.746000000000002, 4.423000000000002, 24.738000000000003), t[hp](3.140000000000002, 24.737000000000002, -.48799999999999777, 24.838000000000005, .3520000000000021, 22.837000000000003), t.bezierCurveTo(1.292000000000002, 20.594000000000005, 2.2330000000000023, 18.351000000000003, 3.1730000000000023, 16.108000000000004), t[hp](4.113000000000002, 13.865000000000006, 5.054000000000002, 11.623000000000005, 5.994000000000002, 9.380000000000004), t[hp](6.728000000000002, 7.629000000000005, 9.521000000000003, 7.885000000000004, 11.156000000000002, 7.880000000000004), t[hp](16.974000000000004, 7.861000000000004, 22.793000000000003, 7.843000000000004, 28.612000000000002, 7.825000000000005), t[hp](30.976000000000003, 7.818000000000005, 33.341, 7.810000000000005, 35.707, 7.803000000000004), t[hp](36.157000000000004, 7.802000000000004, 36.609, 7.787000000000004, 37.06, 7.804000000000005), t[hp](37.793, 7.833000000000005, 39.389, 7.875000000000004, 39.385000000000005, 9.424000000000005), t[hp](39.38400000000001, 9.647000000000006, 39.31, 10.138000000000005, 39.27700000000001, 10.359000000000005), t.bezierCurveTo(38.81900000000001, 13.361000000000004, 38.452000000000005, 15.764000000000006, 37.99400000000001, 18.766000000000005), t[hp](37.806000000000004, 19.998000000000005, 37.61800000000001, 21.230000000000004, 37.43000000000001, 22.462000000000007), t.bezierCurveTo(37.151, 24.271, 35.264, 24.77, 33.591, 24.763), t[zf](), t[Do](), t[Lo](), t.restore(), t[Eo](), t[Eo](), t[vo](), t[vo](), t[vo](), t[tf] = ZE, t[rf](), t[of](10.427, 19.292), t[ff](5.735, 16.452), t[ff](12.58, 13.8), t[ff](12.045, 15.07), t[ff](20.482, 15.072), t[ff](19.667, 17.887), t[ff](11.029, 17.851), t[ff](10.427, 19.292), t.closePath(), t[Do](), t.stroke(), t[Eo](), t[Eo](), t[vo](), t[vo](), t[tf] = ZE, t[rf](), t[of](13.041, 13.042), t[ff](8.641, 10.73), t[ff](14.82, 8.474), t[ff](14.373, 9.537), t[ff](22.102, 9.479), t[ff](21.425, 11.816), t[ff](13.54, 11.85), t.lineTo(13.041, 13.042), t[zf](), t[Do](), t.stroke(), t[Eo](), t[Eo](), t[vo](), t[vo](), t[tf] = ZE, t.beginPath(), t[of](29.787, 16.049), t[ff](29.979, 14.704), t[ff](21.51, 14.706), t[ff](22.214, 12.147), t[ff](30.486, 12.116), t.lineTo(30.653, 10.926), t.lineTo(36.141, 13.4), t[ff](29.787, 16.049), t[zf](), t[Do](), t[Lo](), t[Eo](), t.restore(), t[vo](), t[vo](), t[tf] = ZE, t[rf](), t.moveTo(28.775, 23.14), t[ff](29.011, 21.49), t.lineTo(19.668, 21.405), t.lineTo(20.523, 18.295), t[ff](29.613, 18.338), t[ff](29.815, 16.898), t[ff](35.832, 19.964), t[ff](28.775, 23.14), t.closePath(), t[Do](), t[Lo](), t.restore(), t[Eo](), t[Eo](),t.restore()
      }
    }, cloud: {
      draw: function (t) {
        t[vo](), t[rf](), t[of](0, 0), t[ff](90.75, 0), t[ff](90.75, 62.125), t.lineTo(0, 62.125), t[zf](), t[ME](), t.strokeStyle = aE, t[_d] = $x, t[dd] = Hx, t[OE] = 4, t[vo]();
        var i = t.createLinearGradient(44.0054, 6.4116, 44.0054, 51.3674);
        i[Bp](0, "rgba(159, 160, 160, 0.7)"), i[Bp](.9726, dm), t.fillStyle = i, t[rf](), t[of](57.07, 20.354), t[hp](57.037, 20.354, 57.006, 20.358, 56.974000000000004, 20.358), t.bezierCurveTo(54.461000000000006, 14.308, 48.499, 10.049000000000001, 41.538000000000004, 10.049000000000001), t.bezierCurveTo(33.801, 10.049000000000001, 27.309000000000005, 15.316000000000003, 25.408000000000005, 22.456000000000003), t.bezierCurveTo(18.988000000000007, 23.289, 14.025000000000006, 28.765000000000004, 14.025000000000006, 35.413000000000004), t[hp](14.025000000000006, 42.635000000000005, 19.880000000000006, 48.49, 27.102000000000004, 48.49), t[hp](29.321000000000005, 48.49, 31.407000000000004, 47.933, 33.237, 46.961), t.bezierCurveTo(34.980000000000004, 49.327, 37.78, 50.867999999999995, 40.945, 50.867999999999995), t[hp](43.197, 50.867999999999995, 45.261, 50.086, 46.896, 48.785999999999994), t[hp](49.729, 50.78699999999999, 53.244, 51.98799999999999, 57.07, 51.98799999999999), t[hp](66.412, 51.98799999999999, 73.986, 44.90699999999999, 73.986, 36.17099999999999), t[hp](73.986, 27.436, 66.413, 20.354, 57.07, 20.354), t[zf](), t.fill(), t.stroke(), t[Eo](), t[Eo]()
      }
    }, node: {
      width: 60, height: 100, draw: function (t) {
        t.save(), t[sf](0, 0), t.beginPath(), t[of](0, 0), t.lineTo(40, 0), t.lineTo(40, 40), t.lineTo(0, 40), t[zf](), t[ME](), t[sf](0, 0), t[sf](0, 0), t.scale(1, 1), t[sf](0, 0), t[Ko] = aE, t[_d] = $x, t[dd] = Hx, t[OE] = 4, t[vo](), t.fillStyle = lm, t.beginPath(), t.moveTo(13.948, 31.075), t.lineTo(25.914, 31.075), t[rp](25.914, 31.075, 25.914, 31.075), t[ff](25.914, 34.862), t[rp](25.914, 34.862, 25.914, 34.862), t.lineTo(13.948, 34.862), t[rp](13.948, 34.862, 13.948, 34.862), t.lineTo(13.948, 31.075), t[rp](13.948, 31.075, 13.948, 31.075), t.closePath(), t.fill(), t[Lo](), t.restore(), t[vo](), t[tf] = vm, t.beginPath(), t[of](29.679, 35.972), t[hp](29.679, 36.675000000000004, 29.110999999999997, 37.244, 28.407999999999998, 37.244), t.lineTo(11.456, 37.244), t[hp](10.751999999999999, 37.244, 10.183, 36.675, 10.183, 35.972), t[ff](10.183, 36.136), t[hp](10.183, 35.431000000000004, 10.751999999999999, 34.863, 11.456, 34.863), t.lineTo(28.407, 34.863), t[hp](29.11, 34.863, 29.678, 35.431, 29.678, 36.136), t[ff](29.678, 35.972), t.closePath(), t[Do](), t[Lo](), t[Eo](), t[vo](), t[tf] = vm, t[rf](), t[of](.196, 29.346), t[hp](.196, 30.301, .9690000000000001, 31.075, 1.925, 31.075), t[ff](37.936, 31.075), t.bezierCurveTo(38.891, 31.075, 39.665, 30.301, 39.665, 29.346), t.lineTo(39.665, 27.174), t[ff](.196, 27.174), t[ff](.196, 29.346), t.closePath(), t[Do](), t.stroke(), t.restore(), t[vo](), t[tf] = bm, t[rf](), t[of](37.937, 3.884), t[ff](1.926, 3.884), t[hp](.97, 3.884, .19699999999999984, 4.657, .19699999999999984, 5.614), t[ff](.19699999999999984, 27.12), t[ff](39.666000000000004, 27.12), t.lineTo(39.666000000000004, 5.615), t[hp](39.665, 4.657, 38.892, 3.884, 37.937, 3.884), t[zf](), t.fill(), t[Lo](), t[Eo](), t.save(), t.save(), t.restore(), t.save(), t[Eo](), t[Eo](), t.save();
        var i = t[Rp](6.9609, 2.9341, 32.9008, 28.874);
        i[Bp](0, ym), i[Bp](1, gm), t[tf] = i, t[rf](), t[of](35.788, 6.39), t[ff](4.074, 6.39), t[hp](3.315, 6.39, 2.702, 7.003, 2.702, 7.763), t[ff](2.702, 24.616), t.lineTo(37.159, 24.616), t[ff](37.159, 7.763), t[hp](37.159, 7.003, 36.546, 6.39, 35.788, 6.39), t[zf](), t[Do](), t[Lo](), t[Eo](), t[Eo]()
      }
    }, group: {
      draw: function (t) {
        t[vo](), t.translate(0, 0), t.beginPath(), t[of](0, 0), t[ff](47.75, 0), t[ff](47.75, 40), t[ff](0, 40), t[zf](), t[ME](), t.translate(0, 0), t[sf](0, 0), t[ef](1, 1), t[sf](0, 0), t.strokeStyle = aE, t.lineCap = $x, t[dd] = Hx, t[OE] = 4, t[vo](), t.save(), t[tf] = lm, t[rf](), t[of](10.447, 26.005), t.lineTo(18.847, 26.005), t.quadraticCurveTo(18.847, 26.005, 18.847, 26.005), t[ff](18.847, 28.663), t[rp](18.847, 28.663, 18.847, 28.663), t[ff](10.447, 28.663), t[rp](10.447, 28.663, 10.447, 28.663), t.lineTo(10.447, 26.005), t[rp](10.447, 26.005, 10.447, 26.005), t[zf](), t[Do](), t[Lo](), t[Eo](), t.save(), t[tf] = vm, t.beginPath(), t.moveTo(21.491, 29.443), t.bezierCurveTo(21.491, 29.935000000000002, 21.094, 30.338, 20.597, 30.338), t.lineTo(8.698, 30.338), t.bezierCurveTo(8.201, 30.338, 7.8020000000000005, 29.936, 7.8020000000000005, 29.443), t[ff](7.8020000000000005, 29.557000000000002), t.bezierCurveTo(7.8020000000000005, 29.063000000000002, 8.201, 28.662000000000003, 8.698, 28.662000000000003), t[ff](20.597, 28.662000000000003), t[hp](21.093, 28.662000000000003, 21.491, 29.062, 21.491, 29.557000000000002), t[ff](21.491, 29.443), t.closePath(), t[Do](), t[Lo](), t[Eo](), t[vo](), t[tf] = vm, t[rf](), t.moveTo(.789, 24.79), t.bezierCurveTo(.789, 25.461, 1.334, 26.005, 2.0060000000000002, 26.005), t[ff](27.289, 26.005), t[hp](27.961000000000002, 26.005, 28.504, 25.461, 28.504, 24.79), t.lineTo(28.504, 23.267), t.lineTo(.789, 23.267), t[ff](.789, 24.79), t.closePath(), t.fill(), t.stroke(), t.restore(), t[vo](), t.fillStyle = bm, t[rf](), t.moveTo(27.289, 6.912), t[ff](2.006, 6.912), t[hp](1.3339999999999996, 6.912, .7889999999999997, 7.455, .7889999999999997, 8.126), t[ff](.7889999999999997, 23.227), t[ff](28.503999999999998, 23.227), t.lineTo(28.503999999999998, 8.126), t.bezierCurveTo(28.504, 7.455, 27.961, 6.912, 27.289, 6.912), t[zf](), t[Do](), t[Lo](), t.restore(), t[vo](), t[vo](), t[Eo](), t[vo](), t[Eo](), t.restore(), t[vo]();
        var i = t[Rp](5.54, 6.2451, 23.7529, 24.458);
        i.addColorStop(0, ym), i[Bp](1, gm), t[tf] = i, t[rf](), t[of](25.78, 8.671), t[ff](3.514, 8.671), t[hp](2.9819999999999998, 8.671, 2.549, 9.101999999999999, 2.549, 9.635), t[ff](2.549, 21.466), t[ff](26.743, 21.466), t[ff](26.743, 9.636), t[hp](26.743, 9.102, 26.312, 8.671, 25.78, 8.671), t[zf](), t.fill(), t.stroke(), t[Eo](), t[Eo](), t[vo](), t[vo](), t[tf] = lm, t[rf](), t[of](27.053, 33.602), t[ff](36.22, 33.602), t[rp](36.22, 33.602, 36.22, 33.602), t.lineTo(36.22, 36.501), t[rp](36.22, 36.501, 36.22, 36.501), t.lineTo(27.053, 36.501), t.quadraticCurveTo(27.053, 36.501, 27.053, 36.501), t[ff](27.053, 33.602), t.quadraticCurveTo(27.053, 33.602, 27.053, 33.602), t[zf](), t[Do](), t[Lo](), t[Eo](), t[vo](), t.fillStyle = vm, t[rf](), t[of](39.104, 37.352), t[hp](39.104, 37.891, 38.67, 38.327, 38.13, 38.327), t[ff](25.143, 38.327), t[hp](24.602, 38.327, 24.166, 37.891, 24.166, 37.352), t[ff](24.166, 37.477999999999994), t[hp](24.166, 36.937, 24.602, 36.501, 25.143, 36.501), t[ff](38.131, 36.501), t[hp](38.671, 36.501, 39.105, 36.937, 39.105, 37.477999999999994), t.lineTo(39.105, 37.352), t[zf](), t[Do](), t.stroke(), t[Eo](), t[vo](), t.fillStyle = vm, t[rf](), t[of](16.514, 32.275), t[hp](16.514, 33.004999999999995, 17.107, 33.601, 17.839, 33.601), t[ff](45.433, 33.601), t[hp](46.166, 33.601, 46.758, 33.005, 46.758, 32.275), t[ff](46.758, 30.607999999999997), t[ff](16.514, 30.607999999999997), t.lineTo(16.514, 32.275), t[zf](), t.fill(), t[Lo](), t[Eo](), t[vo](), t.fillStyle = bm, t[rf](), t.moveTo(45.433, 12.763), t.lineTo(17.839, 12.763), t[hp](17.107, 12.763, 16.514, 13.356, 16.514, 14.089), t[ff](16.514, 30.57), t[ff](46.757999999999996, 30.57), t[ff](46.757999999999996, 14.088), t[hp](46.758, 13.356, 46.166, 12.763, 45.433, 12.763), t[zf](), t[Do](), t.stroke(), t[Eo](), t[vo](), t.save(), t.restore(), t.save(), t[Eo](), t[Eo](), t[vo](), i = t.createLinearGradient(21.6973, 12.0352, 41.5743, 31.9122), i[Bp](0, ym), i[Bp](1, gm), t[tf] = i, t[rf](), t[of](43.785, 14.683), t[ff](19.486, 14.683), t.bezierCurveTo(18.903000000000002, 14.683, 18.433, 15.153, 18.433, 15.735), t[ff](18.433, 28.649), t.lineTo(44.837, 28.649), t[ff](44.837, 15.734), t[hp](44.838, 15.153, 44.367, 14.683, 43.785, 14.683), t.closePath(), t.fill(), t[Lo](), t[Eo](), t[Eo](),t.save(),t[nf] = .5,t[rf](),t.moveTo(23.709, 36.33),t.lineTo(4.232, 36.33),t.lineTo(4.232, 27.199),t[ff](5.304, 27.199),t[ff](5.304, 35.259),t[ff](23.709, 35.259),t[ff](23.709, 36.33),t.closePath(),t.fill(),t.stroke(),t[Eo](),t[Eo]()
      }
    }, subnetwork: {
      draw: function (t) {
        t[vo](), t.translate(0, 0), t[rf](), t.moveTo(0, 0), t.lineTo(60.75, 0), t[ff](60.75, 42.125), t[ff](0, 42.125), t.closePath(), t[ME](), t[sf](0, .26859504132231393), t[ef](.6694214876033058, .6694214876033058), t[sf](0, 0), t.strokeStyle = aE, t[_d] = $x, t[dd] = Hx, t.miterLimit = 4, t[vo](), t.save(), t[Eo](), t.save(), t[Eo](), t[Eo](), t.save();
        var i = t[Rp](43.6724, -2.7627, 43.6724, 59.3806);
        i[Bp](0, "rgba(159, 160, 160, 0.7)"), i[Bp](.9726, dm), t[tf] = i, t.beginPath(), t[of](61.732, 16.509), t.bezierCurveTo(61.686, 16.509, 61.644, 16.515, 61.599, 16.515), t[hp](58.126, 8.152000000000001, 49.884, 2.2650000000000006, 40.262, 2.2650000000000006), t[hp](29.567, 2.2650000000000006, 20.594, 9.545000000000002, 17.966, 19.415), t.bezierCurveTo(9.09, 20.566, 2.229, 28.136, 2.229, 37.326), t.bezierCurveTo(2.229, 47.309, 10.322, 55.403000000000006, 20.306, 55.403000000000006), t[hp](23.374000000000002, 55.403000000000006, 26.257, 54.633, 28.787, 53.28900000000001), t[hp](31.197, 56.56000000000001, 35.067, 58.69000000000001, 39.442, 58.69000000000001), t.bezierCurveTo(42.555, 58.69000000000001, 45.408, 57.60900000000001, 47.669, 55.81200000000001), t.bezierCurveTo(51.586, 58.57800000000001, 56.443999999999996, 60.238000000000014, 61.732, 60.238000000000014), t[hp](74.64699999999999, 60.238000000000014, 85.116, 50.45000000000002, 85.116, 38.37400000000001), t[hp](85.116, 26.298, 74.646, 16.509, 61.732, 16.509), t[zf](), t[Do](), t[Lo](), t[Eo](), t[vo](), t[vo](), t.fillStyle = lm, t[rf](), t[of](34.966, 44.287), t[ff](45.112, 44.287), t[rp](45.112, 44.287, 45.112, 44.287), t.lineTo(45.112, 47.497), t.quadraticCurveTo(45.112, 47.497, 45.112, 47.497), t.lineTo(34.966, 47.497), t[rp](34.966, 47.497, 34.966, 47.497), t.lineTo(34.966, 44.287), t.quadraticCurveTo(34.966, 44.287, 34.966, 44.287), t.closePath(), t[Do](), t[Lo](), t[Eo](), t[vo](), t[tf] = xm, t.beginPath(), t[of](48.306, 48.439), t[hp](48.306, 49.034, 47.824999999999996, 49.52, 47.226, 49.52), t[ff](32.854, 49.52), t[hp](32.253, 49.52, 31.771, 49.034000000000006, 31.771, 48.439), t[ff](31.771, 48.578), t.bezierCurveTo(31.771, 47.981, 32.253, 47.497, 32.854, 47.497), t[ff](47.226, 47.497), t.bezierCurveTo(47.824999999999996, 47.497, 48.306, 47.98, 48.306, 48.578), t[ff](48.306, 48.439), t[zf](), t.fill(), t[Lo](), t[Eo](), t.save(), t[tf] = pm, t[rf](), t[of](23.302, 42.82), t.bezierCurveTo(23.302, 43.63, 23.96, 44.287, 24.772, 44.287), t.lineTo(55.308, 44.287), t[hp](56.12, 44.287, 56.775, 43.629999999999995, 56.775, 42.82), t[ff](56.775, 40.98), t[ff](23.302, 40.98), t[ff](23.302, 42.82), t.closePath(), t[Do](), t[Lo](), t[Eo](), t[vo](), t[tf] = bm, t.beginPath(), t[of](55.307, 21.229), t[ff](24.771, 21.229), t[hp](23.959, 21.229, 23.301000000000002, 21.884, 23.301000000000002, 22.695), t[ff](23.301000000000002, 40.933), t[ff](56.774, 40.933), t[ff](56.774, 22.695), t.bezierCurveTo(56.774, 21.884, 56.119, 21.229, 55.307, 21.229), t.closePath(), t[Do](), t.stroke(), t[Eo](), t[vo](), t[vo](), t[Eo](), t[vo](), t.restore(), t.restore(), t.save(), i = t.createLinearGradient(29.04, 20.4219, 51.0363, 42.4181), i[Bp](0, ym), i[Bp](1, gm), t[tf] = i, t[rf](), t[of](53.485, 23.353), t[ff](26.592, 23.353), t.bezierCurveTo(25.948999999999998, 23.353, 25.427, 23.873, 25.427, 24.517000000000003), t.lineTo(25.427, 38.807), t[ff](54.647, 38.807), t[ff](54.647, 24.517000000000003), t.bezierCurveTo(54.648, 23.873, 54.127, 23.353, 53.485, 23.353), t[zf](),t[Do](),t[Lo](),t[Eo](),t[Eo](),t[Eo]()
      }
    }
  };
  for (var rW in sW) kn(Em + rW, sW[rW]);
  var hW = function () {
    this[pc] = !1;
    var t = this._fk;
    t[Qa]();
    var i = this[ac] || 0, n = this._85.x + i / 2, e = this._85.y + i / 2, s = this._85[ha] - i, r = this._85[Ja] - i,
      h = Zn[ah](this, {x: n, y: e});
    te(t, h.x, h.y, !0), h = Zn[ah](this, {x: n + s, y: e}), te(t, h.x, h.y), h = Zn[ah](this, {
      x: n + s,
      y: e + r
    }), te(t, h.x, h.y), h = Zn[ah](this, {
      x: n,
      y: e + r
    }), te(t, h.x, h.y), this[hc] && (h = Zn[ah](this, {
      x: this._pointerX,
      y: this[_c]
    }), te(t, h.x, h.y)), i && t[tc](i / 2)
  }, aW = 20, oW = {
    _hf: !1, _iw: null, _n9z: 0, _kk: -1, _kl: null, _ez: function (t) {
      this._iw || (this._iw = [], this._j5 = DU), this._iw[Io](t), this._do(), this._kn()
    }, _kn: function () {
      if (!this._kl) {
        var t = this;
        this._kl = setTimeout(function i() {
          return t._do() !== !1 ? void (t._kl = setTimeout(i, t._hd())) : void delete t._kl
        }, this._hd())
      }
    }, _hd: function () {
      return Math.max(aW, this._iw[this._kk].delay)
    }, _do: function () {
      return this._hc(this._kk + 1)
    }, _hc: function (t) {
      if (this._hf) t %= this[mm]; else if (t >= this._iw[rh]) return !1;
      if (this._kk == t) return !1;
      this._kk = t;
      var i = this._iw[this._kk], n = i[wm];
      return n || (i._n9ache = n = Ri(this.width, this[Ja]), n.g[Rc](i[Ro], 0, 0), n[pp] = i[pp]), this._kj = n, this[Zf] = !0, this._dp()
    }, _n9y: function () {
      return this._iw ? (this._hf = !0, this[mm] = this._iw[rh], 1 == this._n9z ? this._dp() : void this._kn()) : void this._hu()
    }, _lm: function () {
      this._kl && (clearTimeout(this._kl), delete this._kl)
    }, _dp: function () {
      var t = this[dp][Rv];
      if (!t || !t.length) return !1;
      for (var i = new mY(this, Ap, jp, this._kj), n = 0, e = t[rh]; e > n; n++) {
        var s = t[n];
        s.scope._jy && s.scope._jy[Tm] ? (t.splice(n, 1), n--, e--) : s[Lv][ah](s[Nv], i)
      }
      return t.length > 0
    }, _nca: function (t, i) {
      this[dp].addListener(t, i), this._hf && !this._kl && this._kn()
    }, _6l: function (t, i) {
      this[dp][Bv](t, i), this[dp][km]() || this._lm()
    }, _ie: function () {
      this._lm(), this[dp][Qa]()
    }, _fs: function () {
      var t = this._kj[Mm];
      return t || (this._kj[Mm] = t = new nW(this._kj, 1)), t
    }
  }, fW = function (t) {
    return t[Om](function (t, i) {
      return 2 * t + i
    }, 0)
  }, cW = function (t) {
    for (var i = [], n = 7; n >= 0; n--) i[Io](!!(t & 1 << n));
    return i
  }, uW = function (t) {
    this[Ro] = t, this.len = this.data[rh], this.pos = 0, this[Im] = function () {
      if (this.pos >= this.data[rh]) throw new Error("Attempted to read past end of stream.");
      return 255 & t[Yc](this.pos++)
    }, this.readBytes = function (t) {
      for (var i = [], n = 0; t > n; n++) i[Io](this[Im]());
      return i
    }, this.read = function (t) {
      for (var i = "", n = 0; t > n; n++) i += String[Wc](this[Im]());
      return i
    }, this.readUnsigned = function () {
      var t = this[Sm](2);
      return (t[1] << 8) + t[0]
    }
  }, _W = function (t, i, n) {
    for (var e, s, r = 0, h = function (t) {
      for (var n = 0, e = 0; t > e; e++) i[Yc](r >> 3) & 1 << (7 & r) && (n |= 1 << e), r++;
      return n
    }, a = [], o = 1 << t, f = o + 1, c = t + 1, u = [], _ = function () {
      u = [], c = t + 1;
      for (var i = 0; o > i; i++) u[i] = [i];
      u[o] = [], u[f] = null
    }, d = 0; s = e, e = h(c), !(d++ > n);) if (e !== o) {
      if (e === f) break;
      if (e < u[rh]) s !== o && u[Io](u[s][fh](u[e][0])); else {
        if (e !== u[rh]) throw new Error(Am);
        u.push(u[s].concat(u[s][0]))
      }
      a[Io].apply(a, u[e]), u[rh] === 1 << c && 12 > c && c++
    } else _();
    return a
  }, dW = function (t, i) {
    i || (i = {});
    var n = function (i) {
      for (var n = [], e = 0; i > e; e++) n[Io](t[Sm](3));
      return n
    }, e = function () {
      var i, n;
      n = "";
      do i = t.readByte(), n += t[jm](i); while (0 !== i);
      return n
    }, s = function () {
      var e = {};
      if (e.sig = t[jm](3), e.ver = t[jm](3), Pm !== e.sig) throw new Error(Cm);
      e[ha] = t.readUnsigned(), e.height = t[Lm]();
      var s = cW(t[Im]());
      e[Dm] = s[Rm](), e[Nm] = fW(s[oh](0, 3)), e.sorted = s[Rm](), e[Bm] = fW(s[oh](0, 3)), e[$m] = t[Im](), e[Fm] = t[Im](), e[Dm] && (e.gct = n(1 << e[Bm] + 1)), i.hdr && i.hdr(e)
    }, r = function (n) {
      var s = function (n) {
        var e = (t[Im](), cW(t.readByte()));
        n[Gm] = e[oh](0, 3), n.disposalMethod = fW(e[oh](0, 3)), n[zm] = e.shift(), n[Sc] = e[Rm](), n[jc] = t[Lm](), n.transparencyIndex = t.readByte(), n.terminator = t.readByte(), i.gce && i.gce(n)
      }, r = function (t) {
        t[Hm] = e(), i.com && i.com(t)
      }, h = function (n) {
        t[Im](), n[Ym] = t[Sm](12), n[Um] = e(), i.pte && i.pte(n)
      }, a = function (n) {
        var s = function (n) {
          t[Im](), n[Wm] = t[Im](), n.iterations = t[Lm](), n[qm] = t[Im](), i.app && i.app[Xm] && i.app.NETSCAPE(n)
        }, r = function (t) {
          t.appData = e(), i.app && i.app[t[Vm]] && i.app[t[Vm]](t)
        };
        switch (t[Im](), n.identifier = t.read(8), n[Zm] = t[jm](3), n[Vm]) {
          case"NETSCAPE":
            s(n);
            break;
          default:
            r(n)
        }
      }, o = function (t) {
        t[Ro] = e(), i[Wm] && i.unknown(t)
      };
      switch (n[Km] = t[Im](), n.label) {
        case 249:
          n[Jm] = Qm, s(n);
          break;
        case 254:
          n[Jm] = tw, r(n);
          break;
        case 1:
          n[Jm] = iw, h(n);
          break;
        case 255:
          n.extType = nw, a(n);
          break;
        default:
          n[Jm] = Wm, o(n)
      }
    }, h = function (s) {
      var r = function (t, i) {
        for (var n = new Array(t.length), e = t.length / i, s = function (e, s) {
          var r = t[ch](s * i, (s + 1) * i);
          n.splice[bh](n, [e * i, i].concat(r))
        }, r = [0, 4, 2, 1], h = [8, 8, 4, 2], a = 0, o = 0; 4 > o; o++) for (var f = r[o]; e > f; f += h[o]) s(f, a), a++;
        return n
      };
      s[ew] = t[Lm](), s[Nc] = t[Lm](), s[ha] = t[Lm](), s[Ja] = t[Lm]();
      var h = s.width * s[Ja], a = cW(t.readByte());
      s.lctFlag = a.shift(), s.interlaced = a[Rm](), s[sw] = a[Rm](), s.reserved = a.splice(0, 2), s[rw] = fW(a[oh](0, 3)), s[Cc] && (s.lct = n(1 << s[rw] + 1)), s[hw] = t[Im]();
      var o = e();
      s[Lc] = _W(s.lzwMinCodeSize, o, h), s[aw] && (s.pixels = r(s.pixels, s.width)), i.img && i.img(s)
    }, a = function () {
      var n = {};
      switch (n.sentinel = t[Im](), String[Wc](n.sentinel)) {
        case"!":
          n[Fo] = ow, r(n);
          break;
        case",":
          n.type = X_, h(n);
          break;
        case";":
          n[Fo] = fw, i.eof && i.eof(n);
          break;
        default:
          throw new Error(cw + n[uw].toString(16))
      }
      fw !== n.type && setTimeout(a, 0)
    }, o = function () {
      s(), setTimeout(a, 0)
    };
    o()
  }, lW = "";
  i[wb] && i[wb](_w, function (t) {
    if (t[Ba] && t[dw] && t[lw] && 73 == t[vw]) {
      var i = yU.name + bw + yU[yw] + gw + yU[xw] + xo + yU[pw] + xo + yU.copyright + lW;
      yU[Ew](i)
    }
  }, !1);
  var vW = mw;
  lW = ww + decodeURIComponent("temp%20license%20-%202019.9.1");
  var bW, yW, gW, xW = t, pW = Tw, EW = kw, mW = Mw, wW = Ow, TW = Iw, kW = Sw, MW = Aw, OW = jw, IW = Pw, SW = Cw,
    AW = Lw, jW = Dw, PW = Rw, CW = Nw, LW = Bw, DW = $w, RW = Fw, NW = Gw, BW = zw, $W = Hw, FW = Yw, GW = xW[wW + Uw];
  GW && (yW = xW[CW + Ww][TW + qw], GW.call(xW, ae, DW), GW[ah](xW, oe, BW), GW[ah](xW, function () {
    HW && HW == vW && (JW = !1)
  }, RW));
  var zW, HW, YW, UW = 111, WW = function (t, i) {
    i || (i = Xw + EW + Vw);
    try {
      gW[ah](t, i, 6 * UW, 1 * UW)
    } catch (n) {
    }
  }, qW = !0, XW = !0, VW = !0, ZW = !0, KW = !0, JW = !0, QW = 2048, tq = function (t, i) {
    return he ? he(t, i) || "" : void 0
  };
  if (i.createElement) {
    var iq = i[oo](Zw);
    iq[va][Kw] = ed, iq[$c] = function (t) {
      var i = t[Zu][Jw], n = yW;
      if ("" === n || Qw == n || tT == n) return void this[lp][lp][xp](this.parentNode);
      var e = i[iT][Wc];
      i[wW + Uw](function () {
        re(e) != zW && (Iq.prototype._jm = null)
      }, BW), this[lp][lp][xp](this[lp])
    };
    var nq = i[oo](W_);
    nq[va][ha] = cd, nq[va][Ja] = cd, nq[va][nT] = nd, nq.appendChild(iq), i[Qd][nu](nq)
  }
  if (i[LW + eT]) {
    var eq = i[LW + eT](IW + sT);
    eq[va][Kw] = ed, eq[$c] = function (t) {
      var i = rT, n = t[Zu][i + hT];
      bW = n.Date.now();
      var e = n[SW + AW + aT + jW + oT][PW + Fo];
      gW = e[pW + fT], VH && (n = xW);
      var s = n[wW + Uw];
      s[ah](xW, _e, BW), s.call(xW, de, $W), s[ah](xW, ve, RW), s[ah](xW, fe, NW), s.call(xW, ue, FW), s[ah](xW, le, BW), s[ah](xW, ce, BW), this[lp][lp][xp](this[lp])
    };
    var nq = i[oo](W_);
    nq.style[ha] = cd, nq.style[Ja] = cd, nq[va][nT] = nd, nq[nu](eq), i.documentElement[nu](nq)
  }
  var sq = {position: cT, userSelect: ed, outline: ed, transformOrigin: uT, "-webkit-tap-highlight-color": aE}, rq = _T;
  xi(Kh + rq, sq);
  var hq = {
    width: hd,
    height: hd,
    position: sd,
    overflow: nd,
    textAlign: Ca,
    outline: ed,
    tapHighlightColor: aE,
    userSelect: ed
  }, aq = dT;
  xi(Kh + aq, hq);
  var oq = lT, fq = {overflow: nd, "text-align": Ca, "-webkit-tap-highlight-color": aE, outline: ed};
  xi(Kh + oq, fq);
  var cq = I(function (t) {
    this[vT] = new _q, this._mr = new sY, this._7q = [], this[bT] = [], this[yT] = [], this._7p = {}, this[Ao] = new uY, this._jj = new gq, this[gT] = new xq, this._jj.listener = function (t) {
      this._6q(t)
    }[el](this), this[xT](), this[pT](t)
  }, {
    _nci: null, _jf: null, _mr: null, _n9l: null, _jj: null, _naq: function (t) {
      return t ? (this[ET] || (this._63s = {}), this[ET][t] ? !1 : (this[ET][t] = !0, void this[mT]())) : this[mT]()
    }, _9t: function (t) {
      return this[ET] && this[ET][t]
    }, isInvalidate: function () {
      return this._63
    }, clear: function () {
      this._mr[Qa](), this[bT][rh] = 0, this._7p = {}, this._ncc = !1, this.invalidate()
    }, _67: function () {
      this[wT](TT), this._2b()
    }, _2b: function () {
      this[wT](kT)
    }, invalidate: function (t) {
      (t || !this._63) && (this._63 = !0, this._lm || (this._jmingID = requestAnimationFrame(this._f4[el](this))))
    }, _6e: function (t) {
      return this._lm = t, t ? void (this[MT] && (cancelAnimationFrame(this._jmingID), this[MT] = null)) : void (this._63 && this.invalidate(!0))
    }, _f4: function () {
      this[MT] = null, this._63 = !1;
      var t = this._ncc;
      this[Db] || (this[OT](), this._ncc = !0), this[IT](!t), this._32(), this._jm(), this._20()
    }, _nao: function (t) {
      this._ncs = t || this[ST], (t || this[ET][TT]) && this._8r(), (t || this._63s.matrix) && this._78(), this._ncn(t), this._41(t), this._63s = {}
    }, _32: function () {
      this[bT].length = 0;
      var t = this[gT];
      if (this._mr[Dc](function (i) {
        if (i[AT] !== !1) {
          var n = this._ncl(i);
          t.intersects(n.x, n.y, n[ha], n.height) && this[bT].push(i)
        }
      }, this), this[bT] = this._id(this._n9l), !this[jT]) {
        var i = this[vT];
        this[yT].length = 0, i[PT](this[gT]), i._i4() || this._n9l[Dc](function (t) {
          var n = this._ncl(t);
          i._e4(n.x, n.y, n[ha], n.height) && this[yT][Io](t)
        }, this)
      }
    }, _id: function (t) {
      return VH ? t = d(t, this._9c) : t[CT](this._9c), t
    }, _9c: function (t, i) {
      return t = t.zIndex || 0, i = i[LT] || 0, t - i
    }, _ncl: function (t) {
      return t.boundingBox
    }, _jm: function () {
      if (this[jT]) return this._dc(), this._71(!0), void this[DT](this[RT], this._n9l);
      this._71(this[NT]);
      var t = this[vT], i = this._n9tx;
      i[vo](), this[NT] && (ge(i), i[xd](this[NT].canvas, this._nauffer.x, this[NT].y)), t._jn(i, this._dj.bind(this)), this._dc(), this[DT](i, this[yT]), i[Eo]()
    }, _71: function (t) {
      this._naqCanvasSizeFlag ? (this[BT] = !1, this._jf[uo](this[wp], this._height)) : t && ye(this._jf)
    }, _8r: function () {
      var t = this[ha], i = this[Ja];
      return this._width == t && this._height == i ? !1 : (this[wp] = t, this._height = i, void (this[BT] = !0))
    }, _41: function (t) {
      if (!t && !this[ET][kT]) return !1;
      var i = this._jj.reverseTransform([0, 0]), n = this[ef], e = this[wp] / n, s = this[Tp] / n, r = this[Go],
        h = this[gT];
      if (h.x == i[0] && h.y == i[1] && h.width == e && h[Ja] == s && h[Go] == r) return !1;
      var a = h[$T]();
      return this._viewport.set(i[0], i[1], e, s, r, n), this._3a(this[gT], a, t), !0
    }, _3a: function (t, i, n) {
      this[jT] || n || (this._nauffer = this._g7(i, t))
    }, _6q: function () {
      if (this._ncc) {
        if (this._lm) {
          var t;
          this._n9urrentMatrix ? this._n9urrentMatrix[FT] = t = vq.mul([], this._jj.m, vq[GT]([], this._n9urrentMatrix.m)) : t = this._jj.m, this._4t(t)
        }
        this._naq(zT), this._2b()
      }
    }, _4t: function (t) {
      this[HT] = t, pq[e_](this._jf, K_, t ? YT + t[Hc](Yh) + ")" : "")
    }, _78: function () {
      var t = this[UT];
      if (this._n9urrentMatrix = {
        tx: this._jj.m[4],
        ty: this._jj.m[5],
        m: this._jj.m[ch](),
        scale: this._jj._g3(),
        rotate: this._jj._eq()
      }, this[HT] && this._4t(null), !this._ncs) {
        if (this._2d(this._n9urrentMatrix, t), !t || t.scale != this[UT].scale) return this._7c(this[UT][ef], t ? t.scale : null), void (this[jT] = !0);
        if (!t || t[Go] != this[UT][Go]) return this._4v(this[UT][Go], t ? t[Go] : null), void (this[jT] = !0);
        var i = t.m[4] - this[UT].m[4], n = t.m[5] - this._n9urrentMatrix.m[5], e = this[io];
        i *= e, n *= e;
        var s = 1e-4;
        (Math.abs(i - Math.round(i)) > s || Math.abs(n - Math[Bf](n)) > s) && (this[jT] = !0)
      }
    }, _7a: function () {
      var t = this[Ao], i = t[dh]();
      t[Qa](), this._mr.forEach(function (i) {
        i[AT] !== !1 && t.add(this._ncl(i))
      }, this), t[WT](i) || this._3b(t, i)
    }, _3b: function () {
    }, _ncc: !1, _n9d: function () {
    }, _9i: function (t) {
      var i = t.ratio;
      t[ef](i, i), t[K_][bh](t, this._jj.m)
    }, render: function (t, i) {
      i && i[rh] && (t[vo](), this._9i(t), i[Dc](function (i) {
        if (t[vo](), i[qT] !== !1) try {
          i[DT](t)
        } catch (n) {
          console[wf](n)
        }
        t[Eo]()
      }, this), t[Eo]())
    }, setParent: function (t) {
      N(t) && (t = i[XT](t)), this._mz != t && (this._mz && this._nci && (D(this._mz, oq), this._mz.removeChild(this._nci)), this._mz = t, this._mz && (L(this._mz, oq), this._mz[nu](this._6d()), this._67()))
    }, _6d: function () {
      return this[VT] || this[xT](), this[VT]
    }, _nan: function () {
      var t = Ri(!0);
      ee(t.g), t[eu] = rq;
      var n = i[oo](W_);
      return n.addEventListener(uy, function (t) {
        return i[ZT] == this ? (t[Nh] && t[Nh](), !1) : void 0
      }[el](n), !1), n[eu] = aq, n.appendChild(t), n[KT] = 0, this._jf = t, this._nci = n, this._n9tx = this._jf.getContext(co), t
    }, toLogical: function (t, i) {
      return t instanceof Object && (Q(t) && (t = this._7n(t)), Array[Vu](t) ? (i = t[1] || 0, t = t[0] || 0) : (i = t.y || 0, t = t.x || 0)), this._jj.reverseTransform([t, i])
    }, toCanvas: function (t, i) {
      return this._jj[K_]([t, i])
    }, _7n: function (t) {
      return Ei(t, this[VT])
    }, _ea: function (t, i, n) {
      if (t[$_] instanceof Function) return t[$_](i, n);
      var e = this[JT](t);
      return e ? n ? uY[p_](e.x, e.y, e.width, e[Ja], i[0] - n, i[1] - n, n + n, n + n) : uY[p_](e.x, e.y, e.width, e[Ja], i[0], i[1]) : t
    }, hitTest: function (t, i) {
      return this._88(t, i)
    }, _88: function (t, i) {
      i = this._9g(i), t = this[QT](t);
      for (var n, e = this[bT][rh]; --e >= 0;) if (n = this[bT][e], this._ea(n, t, i)) return n
    }, _9g: function (t) {
      return (t === n || null === t) && (t = eY[Pd]), t ? t / this.scale : 0
    }, getUIByMouseEvent: function (t, i) {
      if (t[tk]) return this._mr[Rl](t[tk]);
      var n = this._88(t, i);
      return t[tk] = n ? n.id : -1, n
    }, _7p: null, invalidateUI: function (t) {
      this._7p[t.id] = t, this[mT]()
    }, _96: function (t) {
      t.validate instanceof Function && t[So](this)
    }, _n98: function (t, i) {
      t[ik] && this._g5(t[ik]);
      var n = t.__hg;
      if (t[AT] = this._e7(t, i), !t[AT]) return n;
      var e = t[ik];
      this._96(t);
      var s = this[JT](t);
      t[ik] = {x: s.x, y: s.y, width: s[ha], height: s.height};
      var r = t[AT] !== n || !uY[WT](e, s);
      return r && t[ik] && this._g5(t[ik]), r
    }, _e7: function (t) {
      return t[qT] !== !1
    }, _$q: function (t) {
      this._mr.forEach(function (i) {
        this._n98(i, t)
      }, this), this._7p = {}, this._7a()
    }, _ncn: function (t) {
      var i = this[ef];
      if (t) return this._$q(i);
      var n = this[nk];
      this[nk] = !1;
      for (var e in this._7p) {
        var s = this._7p[e];
        n ? this._n98(s, i) : n = this[ek](s, i)
      }
      this._7p = {}, n && this._7a()
    }, _7q: null, _20: function () {
      if (!this._7q.length) return !1;
      var t = this._7q;
      this._7q = [], t.forEach(function (t) {
        try {
          var i = t.call, n = t[Nv], e = t[sk];
          n instanceof Object ? i = i.bind(n) : n && !e && (e = parseInt(n)), e ? setTimeout(i, e) : i()
        } catch (s) {
        }
      }, this), this._63 && this._f4()
    }, _dr: function (t, i, n) {
      this._7q[Io]({call: t, scope: i, delay: n}), this._63 || this._20()
    }, _3v: function (t, i) {
      for (var n = this[bT], e = 0, s = n[rh]; s > e; e++) if (t.call(i, n[e]) === !1) return !1
    }, _ej: function (t, i) {
      this._mr.forEach(t, i)
    }, _$t: function (t, i) {
      for (var n = this._n9l, e = n.length - 1; e >= 0; e--) if (t[ah](i, n[e]) === !1) return !1
    }, _3w: function (t, i) {
      this._mr[rk](t, i)
    }, _3x: function () {
      return this[Ao]
    }, _f2: function (t, i, n) {
      t /= this.scale || 1, this._j1(t, i, n)
    }, _j1: function (t, i, e) {
      if (this[Db] && (i === n || null === i)) {
        var s = this[QT](this.width / 2, this.height / 2);
        i = s[0] || 0, e = s[1] || 0
      }
      return this._jj[ef](t, [i || 0, e || 0])
    }, _ed: function (t, i) {
      this._jj[sf]([t, i], !0)
    }, _ncr: function (t, i, n, e) {
      if (n == this[ef] && e !== !1) {
        var s = this.ratio;
        s != (0 | s) && (t = Math[Bf](t * s) / s, i = Math[Bf](i * s) / s)
      }
      this._jj.translateTo([t, i], n)
    }, _j7: function (t, i) {
      return this._j1(this._eh, t, i)
    }, _ib: function (t, i) {
      return this._j1(1 / this._eh, t, i)
    }, _1g: function () {
      var t = this._3x();
      if (!t[gc]()) {
        var i = this.width / t[ha], n = this[Ja] / t[Ja], e = Math.min(i, n);
        return e = Math.max(this._g9, Math.min(this._gb, e)), {scale: e, cx: t.cx, cy: t.cy}
      }
    }, _eh: 1.3, _gb: 10, _g9: .1, _ncs: !1, _7c: function () {
    }, _4v: function () {
    }, _2d: function () {
    }, _dc: function () {
      this[NT] = null, this[vT]._l6()
    }, _dj: function (t) {
      var i = this._jj, n = this._jf[io], e = this.scale, s = i._eq();
      if (!s) {
        var r = i[K_]([t[0], t[1]]);
        return r[0] *= n, r[1] *= n, n *= e, r[2] = t[2] * n, r[3] = t[3] * n, r
      }
      var h = new uY, a = i[K_]([t[0], t[1]]);
      return h.add(a[0], a[1]), a = i[K_]([t[0] + t[2], t[1]]), h.add(a[0], a[1]), a = i.transform([t[0], t[1] + t[3]]), h.add(a[0], a[1]), a = i.transform([t[0] + t[2], t[1] + t[3]]), h.add(a[0], a[1]), [h.x * n, h.y * n, h[ha] * n, h.height * n]
    }, _g7: function (t, n) {
      var e = this._jf;
      if (e[ha] && e[Ja]) {
        var s = n._35(t.x, t.y, t.width, t[Ja]);
        if (s && s[ha] && s[ha]) {
          var r = this.scale * this[io], h = this[vT], a = {}, o = 1e-6;
          s.x > o && (a[Ca] = n._45(0, 0, s.x, n[Ja], r)), n.width - s.right > o && (a[na] = n._45(s[na], 0, n.width - s[na], n[Ja], r)), s.y > o && (a.top = n._45(s.x, 0, s.width, s.y, r)), n[Ja] - s.bottom > o && (a[ea] = n._45(s.x, s.bottom, s[ha], n[Ja] - s[ea], r)), U(a) || h._4n(a);
          var f = n._ho(t.x, t.y), c = (f[0] - s.x) * r, u = (f[1] - s.y) * r, _ = s.width * r, d = s[Ja] * r;
          c = Math[Bf](c), u = Math.round(u), _ = Math[Bf](_), d = Math[Bf](d);
          var l = this._naackCanvas;
          return l || (l = this[hk] = i[oo](fo), l.g = l[bf](co)), l.width = _, l.height = d, ge(l.g), l.g[xd](e, c, u), c = f[0] * r - c, u = f[1] * r - u, {
            x: c,
            y: u,
            canvas: l
          }
        }
      }
    }, _lv: function (t, i, n, e) {
      this._na5._n1(t, i, n, e)
    }, _g5: function (t) {
      this[vT]._hz(t)
    }
  });
  Object.defineProperties(cq[yh], {
    width: {
      get: function () {
        return this._nci.clientWidth
      }
    }, height: {
      get: function () {
        return this[VT][Ed]
      }
    }, rotate: {
      get: function () {
        return this._jj._eq()
      }
    }, tx: {
      get: function () {
        return this._jj._7u()[0]
      }
    }, ty: {
      get: function () {
        return this._jj._7u()[1]
      }
    }, ratio: {
      get: function () {
        return this._jf ? this._jf[io] : void 0
      }
    }, scale: {
      get: function () {
        return this._jj._g3()
      }, set: function (t) {
        this._f2(t)
      }
    }, renderScale: {
      get: function () {
        return this[ef] * this[io]
      }
    }, uis: {
      get: function () {
        return this._mr
      }
    }, length: {
      get: function () {
        return this._mr[rh]
      }
    }, viewportBounds: {
      get: function () {
        return this[gT][ak]()
      }
    }
  });
  var uq, _q = I({
    constructor: function () {
      this._gz = [], this._jk = new uY, this._h0 = HH ? 20 : 50
    }, _h0: 20, _gz: null, _m2: !1, _jk: null, _l6: function () {
      this._m2 = !1, this._gz[rh] = 0, this[ok] = null, this._jk.clear()
    }, _i4: function () {
      return 0 == this._gz[rh] && !this[ok]
    }, _n1: function (t, i, n, e) {
      0 >= n || 0 >= e || this._gz[Io]([t, i, n, e])
    }, _hz: function (t) {
      this._n1(t.x, t.y, t[ha], t.height)
    }, _4n: function (t) {
      var i = this._jk;
      for (var n in t) {
        var e = t[n], s = e[ak]();
        i.add(s)
      }
      this[ok] = t
    }, _ncz: function (t, i) {
      for (var n = [], e = this._gz, s = 0, r = e.length; r > s; s++) {
        var h = e[s];
        t[p_](h[0], h[1], h[2], h[3]) && (n[Io](h), this._jk.addRect(h[0], h[1], h[2], h[3]))
      }
      this._gz = n, this._m2 = i || n[rh] >= this._h0
    }, _e4: function (t, i, n, e) {
      if (!this._jk[Xl](t, i, n, e)) return !1;
      if (this._m2) return !0;
      if (this[ok]) {
        var s = this[ok];
        for (var r in s) if (s[r][p_](t, i, n, e)) return !0
      }
      for (var h, a = 0, o = this._gz[rh]; o > a; a++) if (h = this._gz[a], uY[p_](t, i, n, e, h[0], h[1], h[2], h[3])) return !0;
      return !1
    }, _jn: function (t, i) {
      if (this._i4()) return !1;
      if (t[rf](), this._m2) {
        var n = i([this._jk.x, this._jk.y, this._jk[ha], this._jk[Ja]]);
        return xe(t, n[0], n[1], n[2], n[3]), void t[ME]()
      }
      if (this[ok]) for (var e in this._viewportClips) {
        var n = this[ok][e][fk];
        xe(t, n[0], n[1], n[2], n[3])
      }
      for (var s = this._gz, r = 0, h = s[rh]; h > r; r++) {
        var n = i(s[r]);
        xe(t, n[0], n[1], n[2], n[3])
      }
      t[ME]()
    }
  });
  _q[ck] = function (t, i, n, e) {
    return t instanceof Object && (i = t.y, n = t.width, e = t.height, t = t.x), n = X(t + n) - (t = q(t)), e = X(i + e) - (i = q(i)), [t, i, n, e]
  }, _q[uk] = q, _q._fq = X, gU[_k] = dk, gU[lk] = vk, eY[bk] = gU[lk];
  var dq = I({
    _jm: function () {
      k(this, dq, yk, arguments), this[gk]._jm()
    },
    _9c: function (t, i) {
      return t = t[yc].zIndex || 0, i = i[yc][LT] || 0, t - i
    },
    "super": cq,
    constructor: function (t, n) {
      this._kp = t, N(n) && (n = i[XT](n)), n && n[$l] || (n = i.createElement(W_)), M(this, dq, [n]), this[gk] = new pr(this, this[VT]), this._gr = [], this._kp._4[ol](this._1a, this), this._kp._1d.addListener(this._90, this), this._kp._$z[ol](this._7f, this), this._kp._$f[ol](this._2s, this), this._kp._$k[ol](this._3p, this), this[xk] = {}, this._3j(eY[bk], !0)
    },
    _4t: function (t) {
      k(this, dq, pk, arguments), this[gk]._4t(t)
    },
    _gw: function (t) {
      return t.id || (t = this._mr[Rl](t)), t ? (this._mr[_h](t), t.destroy(), t.__jk && this._g5(t[ik]), void (this[nk] = !0)) : !1
    },
    _gu: function () {
      this._mr[Dc](function (t) {
        t[eg]()
      }), this._mr[Qa]()
    },
    _e7: function (t, i) {
      var n = t[Ro] || t;
      return n._$o && (n._$o = !1, n._hg = this._4x(n, i)), n._hg !== !1
    },
    _4x: function (t, i) {
      return this._3g(t, i) ? !this._kp._hgFilter || this._kp[Ek](t, i) !== !1 : !1
    },
    _9a: function (t) {
      return this._kp._3m == dr(t)
    },
    _3g: function (t, i) {
      if (t[qT] === !1) return !1;
      if (!(t instanceof wq)) return this._kp._3m != dr(t) ? !1 : !t._dh;
      var n = t[ou], e = t[ru];
      if (!n || !e) return !1;
      if (n == e && !t[_u]()) return !1;
      if (t[mk]()) {
        var s = t[hu](!0);
        if (s && !s._e7(t, i)) return !1
      }
      return this._e7(n, i) && this._e7(e, i) ? !0 : !1
    },
    _ncl: function (t) {
      return t[Db] ? {x: t.$x + t.uiBounds.x, y: t.$y + t[jd].y, width: t[jd][ha], height: t[jd][Ja]} : void 0
    },
    _2w: function (t) {
      var i = this._le(t);
      if (i) {
        var n = this[JT](i);
        if (n) return new uY(n)
      }
    },
    _ea: function (t, i, n) {
      return t[$_](i[0], i[1], n)
    },
    hitTest: function (t, i) {
      var n = k(this, dq, $_, arguments);
      if (n) {
        t = this.toLogical(t), i = this._9g(i);
        var e = n[$_](t[0], t[1], i, !0);
        if (e instanceof Iq) return e
      }
      return n
    },
    _3q: function (t) {
      return this.getUIByMouseEvent(t)
    },
    _71: function () {
      k(this, dq, wk, arguments), this[gk]._hw(this[ha], this.height)
    },
    _la: 1,
    _n9l: null,
    _84: null,
    _82: null,
    _mr: null,
    _mz: null,
    _jf: null,
    _na5: null,
    _63: !1,
    _ncc: !1,
    _jj: null,
    _3v: function (t, i) {
      for (var n = this._n9l, e = 0, s = n.length; s > e; e++) if (t[ah](i, n[e]) === !1) return !1
    },
    _ej: function (t, i) {
      this._mr[Dc](t, i)
    },
    _$t: function (t, i) {
      for (var n = this[bT], e = n[rh] - 1; e >= 0; e--) if (t[ah](i, n[e]) === !1) return !1
    },
    _3w: function (t, i) {
      this._mr.forEachReverse(t, i)
    },
    _3a: function (t) {
      k(this, dq, Tk, arguments), this[kk] = {value: t}
    },
    _n9d: function () {
      this._41(!0), this._originAdjusted || (this[Mk] = !0, this._kp && this._kp[Ok] && this._jj[Ik]([this[ha] / 2, this[Ja] / 2]))
    },
    _f4: function () {
      if (!this[Tm] && this._63) {
        if (this[MT] = null, this._63 = !1, this._ncc && this._kp && this._kp._$o && (this._kp._$o = !1, this._kp.forEach(function (t) {
          t[Sk](!0)
        })), k(this, dq, Ak, arguments), this._82Changed) {
          this._6w && this._6w._k4();
          var t = this._82Changed[Xu], i = this[jk].old;
          this[jk] = null, this._kp._4e(new wY(this._kp, Pk, t, i))
        }
        this[kk] && (this[kk] = !1, this._6w && this._6w._3a && this._6w._3a(this._viewport[ha] * this[gT][ef], this[gT][Ja] * this._viewport.scale), this._kp._4e(new wY(this._kp, kT, this._viewport)))
      }
    },
    _gr: null,
    _n98: function (t) {
      var i = t.$data;
      if (!t._18 && !i._63 && !i._$o) return !1;
      var n = t[Zf];
      return n = k(this, dq, ek, arguments) || n
    },
    _96: function (t) {
      var i = t[yc];
      i[Ck] && (i.__4i = !1, t._4i()), i[_b] && i._hi() && (t._4y(), i.__63 = !1), (t._18 || i._63) && (i._63 = !1, t[So]())
    },
    _gq: function (t, i) {
      var n = t.ratio;
      t.scale(n, n), t[K_][bh](t, this._jj.m);
      for (var e = this[Lk], s = [], r = 0, h = i[rh]; h > r; r++) {
        var a = i[r];
        a._jm(t, e), a._ka && a._ka[rh] && s[Io](a)
      }
      if (s[rh]) for (r = 0, h = s[rh]; h > r; r++) s[r]._8y(t, e)
    },
    render: function (t, i) {
      if (i.length) {
        if (t[vo](), HH) try {
          this._gq(t, i)
        } catch (n) {
        } else this._gq(t, i);
        t[Eo]()
      }
    },
    _gp: function (t, i, n) {
      t.save(), t[sf](-n.x * i, -n.y * i), t[ef](i, i);
      var e, s, r = this._mr._je[ch]();
      r = this._id(r);
      for (var h = [], a = 0, o = r[rh]; o > a; a++) e = r[a], e.__hg && (s = this._ncl(e), n[Xl](s.x, s.y, s.width, s.height) && (e._jm(t, i), e._ka && e._ka[rh] && h.push(e)));
      if (h.length) for (a = 0, o = h.length; o > a; a++) h[a]._8y(t, i);
      t[Eo]()
    },
    _$v: function () {
    },
    _1e: function () {
      for (var t, i, n = this._mr._je, e = new uY, s = n.length - 1; s >= 0; s--) t = n[s], t._hg && (i = t.uiBounds, e.addRect(t.$x + i.x, t.$y + i.y, i[ha], i[Ja]));
      var r = this._82;
      this._82 = e, e[WT](r) || this._$v(r, e)
    },
    _5z: function () {
      this._n9l.length = 0, this._84 = {}
    },
    _lc: function () {
      this._l6()
    },
    _ie: function () {
      this._l6(), this._ieed = !0, this._63 = !1, this[gk][Qa](), this._7q[rh] = 0, this._6w && (this._6w._ie(), delete this._6w)
    },
    _le: function (t) {
      return this._mr.getById(t.id || t)
    },
    _$a: function (t) {
      return this._d7(t)
    },
    _gj: function (t, i) {
      var n = this[Dk](t, i);
      return {x: n[0], y: n[1]}
    },
    _d7: function (t, i) {
      var n = this.toLogical(t, i);
      return {x: n[0], y: n[1]}
    },
    _9: null,
    _3p: function (t) {
      var i = t.source, n = t[Ro];
      if (n) if (this._ncc) {
        var e, s;
        if ($(n)) for (var r = 0, h = n[rh]; h > r; r++) s = n[r].id, e = this._mr.getById(s), e && (e[Rk] = i[Ll](s), e[Nk]()); else {
          if (s = n.id, e = this._mr[Rl](s), !e) return;
          e[Rk] = i[Ll](s), e[Nk]()
        }
        this[wT]()
      } else {
        this._9 || (this._9 = {});
        var e, s;
        if ($(n)) for (var r = 0, h = n[rh]; h > r; r++) s = n[r].id, this._9[s] = !0; else s = n.id, this._9[s] = !0
      }
    },
    _kp: null,
    _n93: function (t) {
      var i = t[Bk];
      return i ? new i(t, this._kp) : void 0
    },
    _1a: function (t) {
      if (!this[Db]) return !1;
      var i = t.source, n = t[rl];
      $k == n && this._kp[Sk](), Bk == n ? (this._gw(i.id), this._lg(i)) : Fk == n && i._hi() && t.value && this._5g(i);
      var e = this._mr[Rl](i.id);
      e && e[Db] && e[Gk](t) && this[wT]()
    },
    _3o: function (t) {
      var i = this._le(t);
      i && (i[zk](), this[wT]())
    },
    _90: function (t) {
      if (!this._ncc) return !1;
      switch (t[rl]) {
        case PY[Hv]:
          this._lg(t.data);
          break;
        case PY[Uv]:
          this._gh(t[Ro]);
          break;
        case PY[tb]:
          this._ig(t[Ro])
      }
    },
    _l6: function () {
      this[xk] = {}, this._gu(), this.clear()
    },
    _nab: null,
    _lg: function (t) {
      var i = this[Hk](t);
      i && (this._mr.add(i), this[Db] && (this[xk][t.id] = t), this._naq())
    },
    _gh: function (t) {
      if (Array[Vu](t)) {
        for (var i, n = [], e = 0, s = t.length; s > e; e++) i = t[e].id, n.push(i), delete this[xk][i];
        t = n
      } else t = t.id, delete this[xk][t], t = [t];
      t[Dc](function (t) {
        this._gw(t)
      }, this), this[wT]()
    },
    _ig: function () {
      this._l6()
    },
    _7f: function (t) {
      return this._ncc ? void (t.source instanceof Tq && !this[xk][t[mf].id] && (t[wv] && (this._3o(t.oldValue), t[wv][_b] = !0), t.value && (this._3o(t[Xu]), t[Xu][_b] = !0), this._5g(t[mf]))) : !1
    },
    _2s: function (t) {
      return this[Db] ? void (t.source instanceof Tq && !this[xk][t.source.id] && this._5g(t[mf])) : !1
    },
    _2r: function (t) {
      if (t[Yk]) {
        var i = t.getEdgeBundle(!0);
        if (!i) return t[Yk] = !1, void t.validateEdgeBundle();
        i[So](this._kp), i[Uk](function (t) {
          t[Wk]()
        })
      }
    },
    _$q: function (t) {
      var i, n = (this._kp, this._kp.graphModel), e = this._mr, s = [], r = 1;
      if (n[qk](function (t) {
        return t instanceof wq ? (this._2r(t), void s.push(t)) : (i = this[Hk](t), void (i && (e.add(i), t[Xk] = r++)))
      }, this), e[rh]) for (var h = e._je, r = h[rh] - 1; r >= 0; r--) i = h[r], this._3h(i, i.$data, t);
      for (var a, r = 0, o = s[rh]; o > r; r++) if (a = s[r], i = this[Hk](a)) {
        this._3h(i, a, t), e.add(i);
        var f = a[ou], c = a[ru], u = f[Xk] || 0;
        f != c && (u = Math.max(u, c[Xk] || 0)), a[Xk] = u
      }
      if (s.length && e._je[CT](function (t, i) {
        return t.$data.__kk - i[yc][Xk]
      }), this._9) {
        var _ = n.selectionModel;
        for (var d in this._9) if (_[Ll](d)) {
          var i = e.getById(d);
          i && (i[Rk] = !0)
        }
        this._9 = null
      }
      this._7a()
    },
    _ncn: function (t, i) {
      if (t) return this._$q();
      var n = this._naqBoundsFlag;
      this[nk] = !1;
      for (var e in this[xk]) {
        var s = this[xk][e];
        s instanceof Tq ? this._5g(s) : this._5e(s)
      }
      this[xk] = {};
      for (var r, h, a = this._mr._je, o = [], f = a[rh] - 1; f >= 0; f--) r = a[f], h = r[yc], h instanceof wq ? (this._2r(h), o[Io](r)) : this._3h(r, h, i) && !n && (n = !0);
      if (o.length) for (var f = 0, c = o.length; c > f; f++) r = o[f], this._3h(r, r[yc], i) && !n && (n = !0);
      n && this._7a()
    },
    _3h: function (t, i, n) {
      if (i instanceof wq) return i[Ck] && (i.__4i = !1, t._4i()), this[ek](t, n);
      if (i.__63 && i._hi() && (t._4y(), i.__63 = !1), this[ek](t, n)) {
        var e = this._4p(i);
        return e && (e.__63 = !0), i.hasEdge() && i[gu](function (t) {
          t[Ck] = !0
        }, this), !0
      }
    },
    _2m: function (t, i) {
      var n = t.fromAgent, e = t[ru], s = i[Vk](n.id);
      if (n == e) return s;
      var r = i[Vk](e.id);
      return Math.max(s, r)
    },
    _2k: function (t, i) {
      var n = this[Zk]._ha(t);
      return n ? i[Vk](n.id) : 0
    },
    _5g: function (t) {
      var i = this._mr, n = i[Rl](t.id);
      if (!n) throw new Error(Kk + t[kh] + Jk);
      var s = this._2k(t, i), r = [n];
      t.hasChildren() && e(t, function (t) {
        t instanceof Tq && (n = i.getById(t.id), n && r.push(n))
      }, this), this._4o(i, s, r)
    },
    _5e: function (t) {
      var i = this._mr.getById(t.id);
      if (i) {
        var n = this._2m(t, this._mr);
        this._mr.setIndexBefore(i, n)
      }
    },
    _4o: function (t, i, n) {
      function e(t) {
        s.add(t)
      }

      var s = new sY;
      l(n, function (n) {
        i = t.setIndexAfter(n, i), n[yc][gu](e)
      }, this), 0 != s.length && s[Dc](this._5e, this)
    },
    _4p: function (t) {
      var i = Pe(t);
      return i && i.expanded ? i : null
    },
    _6m: null,
    _6w: null,
    _3j: function (t, i) {
      return i || t != this._6m ? (this._6m = t, this._6w && (this._6w._ie(), delete this._6w), t == gU[lk] ? void (this._6w = new gr(this, this[VT])) : t == gU[_k] ? void (this._6w = new yr(this, this[VT])) : void 0) : !1
    },
    _2d: function (t, i) {
      this._6w && this._6w._k4(), this._kp._4e(new wY(this._kp, K_, t, i))
    },
    _7c: function (t, i) {
      this._kp._4e(new wY(this._kp, ef, t, i))
    },
    _3b: function (t, i) {
      this._82Changed = {value: t, old: i}
    },
    _7d: function () {
      this._67()
    }
  });
  Object[Zh](dq[yh], {
    _viewportBounds: {
      get: function () {
        return this[Qk]
      }
    }, _scale: {
      get: function () {
        return this.scale
      }, set: function (t) {
        this._f2(t)
      }
    }, _tx: {
      get: function () {
        return this.tx
      }
    }, _ty: {
      get: function () {
        return this.ty
      }
    }, graphModel: {
      get: function () {
        return this._kp[tM]
      }
    }
  });
  var lq = cq, vq = {};
  vq.create = function () {
    return [1, 0, 0, 1, 0, 0]
  }, vq[GT] = function (t, i) {
    var n = i[0], e = i[1], s = i[2], r = i[3], h = i[4], a = i[5], o = n * r - e * s;
    return o ? (o = 1 / o, t[0] = r * o, t[1] = -e * o, t[2] = -s * o, t[3] = n * o, t[4] = (s * a - r * h) * o, t[5] = (e * h - n * a) * o, t) : null
  }, vq[gE] = function (t, i, n) {
    var e = i[0], s = i[1], r = i[2], h = i[3], a = i[4], o = i[5], f = n[0], c = n[1], u = n[2], _ = n[3], d = n[4],
      l = n[5];
    return t[0] = e * f + r * c, t[1] = s * f + h * c, t[2] = e * u + r * _, t[3] = s * u + h * _, t[4] = e * d + r * l + a, t[5] = s * d + h * l + o, t
  }, vq.mul = vq.multiply, vq.rotate = function (t, i, n) {
    var e = i[0], s = i[1], r = i[2], h = i[3], a = i[4], o = i[5], f = Math.sin(n), c = Math.cos(n);
    return t[0] = e * c + r * f, t[1] = s * c + h * f, t[2] = e * -f + r * c, t[3] = s * -f + h * c, t[4] = a, t[5] = o, t
  }, vq[ef] = function (t, i, n) {
    var e = i[0], s = i[1], r = i[2], h = i[3], a = i[4], o = i[5], f = n[0], c = n[1];
    return t[0] = e * f, t[1] = s * f, t[2] = r * c, t[3] = h * c, t[4] = a, t[5] = o, t
  }, vq[sf] = function (t, i, n) {
    var e = i[0], s = i[1], r = i[2], h = i[3], a = i[4], o = i[5], f = n[0], c = n[1];
    return t[0] = e, t[1] = s, t[2] = r, t[3] = h, t[4] = e * f + r * c + a, t[5] = s * f + h * c + o, t
  }, vq[K_] = function (t, i) {
    var n = i[0], e = i[1];
    return [n * t[0] + e * t[2] + t[4], n * t[1] + e * t[3] + t[5]]
  }, vq.reverseTransform = function (t, i) {
    return vq[K_](vq.invert([], t), i)
  };
  var bq = Math.PI + Math.PI, yq = R, gq = I({
    equals: function (t) {
      if (!t || !Array.isArray(t)) return !1;
      for (var i = this.m, n = 0; n < i[rh];) {
        if (i[n] != t[n]) return !1;
        ++n
      }
      return !0
    }, constructor: function (t) {
      this.m = t || vq[Bw](), this.im = []
    }, listener: null, _63: !0, invalidate: function () {
      return this._63 = !0, this._naackM && this[WT](this[iM]) ? !1 : (this.listener && this[Cv]({
        target: this,
        kind: mT
      }), this._naackM = this.m[ch](), this)
    }, validate: function () {
      return this._63 = !1, vq[GT](this.im, this.m), this
    }, translate: function (t, i) {
      return yq(t) && (t = [arguments[0], arguments[1]], i = arguments[2]), i !== !1 ? (this.m[4] += t[0], this.m[5] += t[1], this[mT]()) : (vq[sf](this.m, this.m, t), this[mT]())
    }, translateTo: function (t, i) {
      return yq(t) && (t = [arguments[0], arguments[1]], i = arguments[2]), i && (i /= this._g3(), vq.scale(this.m, this.m, [i, i])), this.m[4] = t[0], this.m[5] = t[1], this.invalidate()
    }, scale: function (t, i) {
      return Lh == typeof t && (t = [t, t]), i ? (vq[sf](this.m, this.m, i), vq.scale(this.m, this.m, t), vq.translate(this.m, this.m, pe(i))) : vq[ef](this.m, this.m, t), this[mT]()
    }, rotate: function (t, i) {
      return i ? (vq[sf](this.m, this.m, i), vq[Go](this.m, this.m, t), vq[sf](this.m, this.m, pe(i))) : vq.rotate(this.m, this.m, t), this.invalidate()
    }, transform: function (t) {
      return vq[K_](this.m, t)
    }, reverseTransform: function (t) {
      return vq.transform(this._43(), t)
    }, toString: function () {
      return YT + this.m.join(Yh) + Uh
    }, _43: function () {
      return this._63 && this.validate(), this.im
    }, _en: function () {
      var t = this.m[0], i = this.m[1], n = this.m[2], e = this.m[3];
      return [Math[To](t * t + n * n), Math[To](i * i + e * e)]
    }, _g3: function () {
      var t = this.m[0], i = this.m[2];
      return Math[To](t * t + i * i)
    }, _7u: function () {
      return [this.m[4], this.m[5]]
    }, _nar: function () {
      var t = this.m[0], i = this.m[1], n = this.m[2], e = this.m[3];
      return [Ee(Math.atan2(i, e)), Ee(Math[ta](-n, t))]
    }, _eq: function () {
      return Ee(Math.atan2(this.m[1], this.m[3]))
    }
  }), xq = I({
    constructor: function () {
    }, x: 0, y: 0, width: 0, height: 0, rotate: 0, set: function (t, i, n, e, s, r) {
      return this.x = t, this.y = i, this.width = n, this[Ja] = e, this.rotate = s, this[ia] = Math.cos(s), this[Ul] = Math.sin(s), this.scale = r, this[nM] = null, this
    }, _ho: function (t, i) {
      return t -= this.x, i -= this.y, this[Go] ? Te(t, i, this._sin, this[ia]) : [t, i]
    }, _7w: function (t) {
      var i = new uY;
      return i.add(this._ho(t.x, t.y)), i.add(this._ho(t.x + t[ha], t.y)), i.add(this._ho(t.x, t.y + t[Ja])), i.add(this._ho(t.x + t[ha], t.y + t[Ja])), i
    }, _g1: function (t, i) {
      if (this[Go]) {
        var n = Te(t, i, Math.sin(-this.rotate), Math.cos(-this[Go]));
        t = n[0], i = n[1]
      }
      return [this.x + t, this.y + i]
    }, _60: function (t, i) {
      var n = this._ho(t, i);
      return t = n[0], i = n[1], t >= 0 && i >= 0 && t <= this.width && i <= this.height
    }, intersects: function (t, i, n, e) {
      if (!this[Go]) return uY[p_](this.x, this.y, this[ha], this.height, t, i, n, e);
      if (!n || !e) return this._60(t, i);
      var s = this[ak]();
      if (!s[p_](t, i, n, e)) return !1;
      for (var r = s[$a], h = 0; h < r.length;) {
        var a = r[h];
        if (uY[jo](t, i, n, e, a[0], a[1])) return !0;
        h++
      }
      var o = [[t, i], [t + n, i], [t, i + e], [t + n, i + e]];
      for (h = 0; h < o.length;) {
        var a = o[h];
        if (this._60(a[0], a[1])) return !0;
        h++
      }
      return we(r, o)
    }, getGlobalBounds: function () {
      return this[nM] || (this._globalBounds = this._7h(0, 0, this[ha], this[Ja])), this[nM]
    }, _7h: function (t, i, n, e) {
      if (!this[Go]) return new uY(this.x + t, this.y + i, n, e);
      var s = [], r = new uY, h = this._g1(t, i);
      return s[Io](h), r.add(h[0], h[1]), h = this._g1(t + n, i), s[Io](h), r.add(h[0], h[1]), h = this._g1(t, i + e), s[Io](h), r.add(h[0], h[1]), h = this._g1(t + n, i + e), s.push(h), r.add(h[0], h[1]), r[$a] = s, r
    }, _45: function (t, i, n, e, s) {
      var r;
      if (this.rotate) {
        var h = this._g1(t, i);
        r = (new xq).set(h[0], h[1], n, e, this[Go], this.scale)
      } else r = (new xq).set(this.x + t, this.y + i, n, e, 0, this.scale);
      return r[fk] = [Math[Bf](s * t), Math[Bf](s * i), Math.round(s * n), Math[Bf](s * e)], r
    }, _35: function (t, i, n, e) {
      if (!this[Go]) {
        var s = uY[$f](this.x, this.y, this.width, this.height, t, i, n, e);
        return s && s[ep](-this.x, -this.y), s
      }
      var r = this._ho(t, i);
      return t = r[0], i = r[1], uY.intersection(0, 0, this[ha], this[Ja], r[0], r[1], n, e)
    }, equals: function (t) {
      return this.x == t.x && this.y == t.y && this[ha] == t.width && this[Ja] == t[Ja] && this[Go] == t[Go]
    }, toString: function () {
      return this.x + Yh + this.y + Yh + this[ha] + Yh + this.height + Yh + this[Go]
    }, toJSON: function () {
      return {x: this.x, y: this.y, width: this[ha], height: this[Ja], rotate: this[Go], scale: this[ef]}
    }
  }), pq = {setStyle: gi, setStyles: bi, addRule: xi, pre: $Y}, Eq = function (t, i, n, e) {
    this.source = t, this[rl] = i, this[wv] = e, this[Xu] = n, this.propertyType = gU[n_]
  };
  m(Eq, wY);
  var mq = function (t) {
    this.id = ++FH, this._naz = {}, this._jc = {}, t && (this[eM] = t)
  };
  mq[yh] = {
    _jc: null, getStyle: function (t) {
      return this._jc[t]
    }, setStyle: function (t, i) {
      var n = this._jc[t];
      return n === i || n && i && n[WT] && n[WT](i) ? !1 : this[Uo](t, i, new Eq(this, t, i, n), this._jc)
    }, putStyles: function (t, i) {
      for (var n in t) {
        var e = t[n];
        i ? this._jc[n] = e : this[e_](n, e)
      }
    }, _$o: !0, invalidateVisibility: function (t) {
      this._$o = !0, t || (this instanceof Tq && this[sM]() && this[gu](function (t) {
        t._$o = !0
      }), this._hi() && this[eh]() && this.forEachChild(function (t) {
        t[Sk]()
      }))
    }, onParentChanged: function () {
      this[Sk]()
    }, _hi: function () {
      return !this._4a && this instanceof Oq
    }, invalidate: function () {
      this.onEvent(new mY(this, rM, mT))
    }, _naw: null, addUI: function (t, i) {
      if (this[hM] || (this._naw = new sY), t.id || (t.id = ++FH), this._naw[Ll](t.id)) return !1;
      var n = {id: t.id, ui: t, bindingProperties: i};
      this._naw.add(n);
      var e = new mY(this, rM, Yv, n);
      return this.onEvent(e)
    }, removeUI: function (t) {
      if (!this[hM]) return !1;
      var i = this[hM][Rl](t.id || t);
      return i ? (this[hM][_h](i), void this.onEvent(new mY(this, rM, _h, i))) : !1
    }, clearUIs: function () {
      this[aM] && this[aM][Nl]()[Dc](function (t) {
        this[oM](t.ui)
      }[el](this))
    }, toString: function () {
      return this.$name || this.id
    }, type: fM, _4a: !1, _hg: !0, inGroup: function (t) {
      var i = Pe(this);
      return i == t || i && t instanceof Oq && i.isDescendantOf(t)
    }
  }, m(mq, CY), A(mq[yh], [Bk, kh, LT, cM]), K(mq[yh], {
    enableSubNetwork: {
      get: function () {
        return this._4a
      }, set: function (t) {
        if (this._4a != t) {
          var i = this._4a;
          this._4a = t, this instanceof Tq && this._$w(), this.onEvent(new wY(this, $k, t, i))
        }
      }
    }, bindingUIs: {
      get: function () {
        return this._naw
      }
    }, styles: {
      get: function () {
        return this._jc
      }, set: function (t) {
        if (this._jc != t) {
          for (var i in this._jc) i in t || (t[i] = n);
          this[uM](t), this._jc = t
        }
      }
    }
  }), yU.findGroup = Pe;
  var wq = function (t, i, n) {
    this.id = ++FH, this._naz = {}, this._jc = {}, n && (this.$name = n), this[xu] = t, this.$to = i, this[_M]()
  };
  wq.prototype = {
    $uiClass: Ts, _j3: null, _hl: null, _j6: null, _hj: null, _f1: !1, type: dM, otherNode: function (t) {
      return t == this[uu] ? this.to : t == this.to ? this[uu] : void 0
    }, connect: function () {
      if (this._f1) return !1;
      if (!this[xu] || !this.$to) return !1;
      if (this._f1 = !0, this.$from == this.$to) return void this[xu]._ht(this);
      ze(this.$to, this), Fe(this[xu], this), Me(this[xu], this, this.$to);
      var t = this[ou], i = this.toAgent;
      if (t != i) {
        var n;
        this[xu]._dh && (Ie(t, this, i), n = !0), this.$to._dh && (Ae(i, this, t), n = !0), n && Me(t, this, i)
      }
    }, disconnect: function () {
      if (!this._f1) return !1;
      if (this._f1 = !1, this[xu] == this.$to) return void this[xu][lM](this);
      Ge(this[xu], this), He(this.$to, this), Oe(this[xu], this, this.$to);
      var t = this[ou], i = this[ru];
      if (t != i) {
        var n;
        this[xu]._dh && (Se(t, this, i), n = !0), this.$to._dh && (je(i, this, t), n = !0), n && Oe(t, this, i)
      }
    }, isConnected: function () {
      return this._f1
    }, isInvalid: function () {
      return !this.$from || !this.$to
    }, isLooped: function () {
      return this.$from == this.$to
    }, getEdgeBundle: function (t) {
      return t ? this._2a() : this[_u]() ? this[xu]._3t : this.$from[hu](this.$to)
    }, hasEdgeBundle: function () {
      var t = this.getEdgeBundle(!0);
      return t && t[vM][rh] > 1
    }, _2a: function () {
      var t = this[ou], i = this[ru];
      return t == i ? this[xu]._dh || this.$to._dh ? null : this[xu]._3t : this.fromAgent[hu](this.toAgent)
    }, _8s: null, hasPathSegments: function () {
      return this._8s && !this._8s[gc]()
    }, isBundleEnabled: function () {
      return this[bM] && !this[C_]() && !this.edgeType
    }, firePathChange: function (t) {
      this[Lv](new wY(this, yM, t))
    }, addPathSegment: function (t, i, n) {
      var e = new QU(i || XU, t);
      this._8s || (this._8s = new sY), this._8s.add(e, n), this[gM](e)
    }, addPathSegement: function () {
      return yU.log('change "edge.addPathSegement(...)" to "edge.addPathSegment(...)"'), this[xM][bh](this, arguments)
    }, removePathSegmentByIndex: function (t) {
      if (!this._8s) return !1;
      var i = this._8s[Pl](t);
      i && (this._8s.remove(i), this[gM](i))
    }, removePathSegment: function (t) {
      return this._8s ? (this._8s.remove(t), void this.firePathChange(t)) : !1
    }, movePathSegment: function (t, i, n) {
      if (!this._8s) return !1;
      if (t = t || 0, i = i || 0, yU[Zp](n)) {
        var e = this._8s[Pl](n);
        return e ? (e[pg](t, i), void this[gM]()) : !1
      }
      l(function (n) {
        n[pg](t, i)
      }), this[gM]()
    }, move: function (t, i) {
      return this._8s ? (this._8s[Dc](function (n) {
        n[pg](t, i)
      }, this), void this.firePathChange()) : !1
    }, validateEdgeBundle: function () {
    }
  }, m(wq, mq), K(wq[yh], {
    pathSegments: {
      get: function () {
        return this._8s
      }, set: function (t) {
        yU.isArray(t) && (t = new sY(t)), this._8s = t, this[gM]()
      }
    }, from: {
      get: function () {
        return this[xu]
      }, set: function (t) {
        if (this.$from != t) {
          var i = new wY(this, uu, t, this.$from);
          this[Th](i) !== !1 && (this[pM](), this[xu] = t, this.connect(), this[Lv](i))
        }
      }
    }, to: {
      get: function () {
        return this.$to
      }, set: function (t) {
        if (this.$to != t) {
          var i = new wY(this, EM, t, this.$to);
          this[Th](i) !== !1 && (this[pM](), this.$to = t, this.connect(), this.onEvent(i))
        }
      }
    }, fromAgent: {
      get: function () {
        return this[xu] ? this[xu]._dh || this[xu] : null
      }
    }, toAgent: {
      get: function () {
        return this.$to ? this.$to._dh || this.$to : null
      }
    }
  }), A(wq[yh], [j_, {name: bM, value: !0}, D_]);
  var Tq = function (t, i, n) {
    2 == arguments[rh] && R(t) && (n = i, i = t, t = null), this.id = ++FH, this._naz = {}, this._jc = {}, t && (this[eM] = t), this[mM] = wM, this.$anchorPosition = dY[ov], this[TM] = {
      x: i || 0,
      y: n || 0
    }, this[au] = {}
  };
  Tq[yh] = {
    $uiClass: ks, _dh: null, forEachEdge: function (t, i, n) {
      return !n && this._kh && this._kh[Dc](t, i) === !1 ? !1 : Ue(this, t, i)
    }, forEachOutEdge: function (t, i) {
      return We(this, t, i)
    }, forEachInEdge: function (t, i) {
      return qe(this, t, i)
    }, getEdges: function () {
      var t = [];
      return this[gu](function (i) {
        t[Io](i)
      }), t
    }, _i8: null, _fb: null, _jh: null, _hr: null, _nc3: 0, _99: 0, hasInEdge: function () {
      return null != this._i8
    }, hasOutEdge: function () {
      return null != this._fb
    }, hasEdge: function () {
      return null != this._i8 || null != this._fb || this.hasLoops()
    }, linkedWith: function (t) {
      return t[uu] == this || t.to == this || t[ou] == this || t[ru] == this
    }, hasEdgeWith: function (t) {
      var i = this[hu](t);
      return i && i[vM][rh] > 0
    }, _kh: null, _3t: null, hasLoops: function () {
      return this._kh && this._kh[rh] > 0
    }, _ht: function (t) {
      return this._kh || (this._kh = new sY, this._3t = new HX(this, this, this._kh)), this._3t._il(t)
    }, _n9b: function (t) {
      return this._3t ? this._3t._n9w(t) : void 0
    }, getEdgeBundle: function (t) {
      return t == this ? this._3t : this[au][t.id] || t._linkedNodes[this.id]
    }, _6f: function () {
      return this._95 && this._95[rh]
    }, _50: function () {
      return this._7x && this._7x[rh]
    }, _97: function () {
      return this._6f() || this._50()
    }, _7x: null, _95: null, _n9f: function () {
      var t = this._dh, i = ke(this);
      if (t != i) {
        var n = Ye(this);
        this._92(i), n[Dc](function (t) {
          var i = t.fromAgent, n = t[ru], t = t[Uu], e = t[xu]._dh, s = t.$to._dh;
          i != n && (i && Se(i, t, n || t.$to), n && je(n, t, i || t.$from)), e != s && (e && Ie(e, t, s || t.$to), s && Ae(s, t, e || t[xu]), Me(e || t[xu], t, s || t.$to))
        }, this)
      }
    }, onParentChanged: function () {
      yU[kM](this, Tq, MM, arguments), this[OM]()
    }, _7y: null, _$w: function () {
      var t;
      if (this._4a ? t = null : (t = this._dh, t || this._h9 !== !1 || (t = this)), this._7y == t) return !1;
      if (this._7y = t, this._fi && this._fi._je.length) for (var i, n = this._fi._je, e = 0, s = n[rh]; s > e; e++) i = n[e], i instanceof Tq && i._92(t)
    }, setLocation: function (t, i) {
      if (this[TM] && this[TM].x == t && this[TM].y == i) return !1;
      var n;
      n = this[TM] ? {x: this.$location.x, y: this[TM].y} : this[TM];
      var e = new wY(this, IM, n, {x: t, y: i});
      return this.beforeEvent(e) === !1 ? !1 : (this.$location ? (this.$location.x = t, this[TM].y = i) : this[TM] = new aY(t, i), this[Lv](e), !0)
    }, _du: null, addFollower: function (t) {
      return null == t ? !1 : t[SM] = this
    }, removeFollower: function (t) {
      return this._du && this._du[B_](t) ? t[SM] = null : !1
    }, hasFollowers: function () {
      return this._du && !this._du.isEmpty()
    }, toFollowers: function () {
      return this.hasFollowers() ? this._du.toDatas() : null
    }, clearFollowers: function () {
      this[AM]() && (this[jM](), l(this.toFollowers(), function (t) {
        t[SM] = null
      }))
    }, getFollowerIndex: function (t) {
      return this._du && this._du[B_](t) ? this._du.indexOf(t) : -1
    }, setFollowerIndex: function (t, i) {
      return this._du && this._du[B_](t) ? void this._du[yu](t, i) : -1
    }, getFollowerCount: function () {
      return this._du ? this._du[rh] : 0
    }, _94: function () {
      return this._du ? this._du : (this._du = new sY, this._du)
    }, isFollow: function (t) {
      if (!t || !this._host) return !1;
      for (var i = this[PM]; i;) {
        if (i == t) return !0;
        i = i[PM]
      }
      return !1
    }, _92: function (t) {
      return t == this._dh ? !1 : (this._dh = t, this[Sk](), void this._$w())
    }, type: CM
  }, m(Tq, mq), K(Tq[yh], {
    loops: {
      get: function () {
        return this._kh
      }
    }, edgeCount: {
      get: function () {
        return this[fu] + this._99
      }
    }, agentNode: {
      get: function () {
        return this._dh || this
      }
    }, host: {
      set: function (t) {
        if (this == t || t == this._host) return !1;
        var i = new wY(this, SM, this[PM], t);
        if (!1 === this[Th](i)) return !1;
        var n = null, e = null, s = this._host;
        if (null != t && (n = new wY(t, LM, null, this), !1 === t[Th](n))) return !1;
        if (null != s && (e = new wY(s, DM, null, this), !1 === s[Th](e))) return !1;
        if (this[PM] = t, null != t) {
          var r = t._94();
          r.add(this)
        }
        if (null != s) {
          var r = s._94();
          r[_h](this)
        }
        return this[Lv](i), null != t && t.onEvent(n), null != s && s.onEvent(e), !0
      }, get: function () {
        return this[PM]
      }
    }
  }), A(Tq[yh], [IM, TT, Ap, Go, RM]), K(Tq[yh], {
    x: {
      get: function () {
        return this[IM].x
      }, set: function (t) {
        t != this[IM].x && (this[IM] = new aY(t, this.location.y))
      }
    }, y: {
      get: function () {
        return this.location.y
      }, set: function (t) {
        t != this[IM].y && (this[IM] = new aY(this[IM].x, t))
      }
    }
  });
  var kq = function (t, i) {
    t instanceof iW && (i = t, t = n), w(this, kq, [t]), this[L_] = i || new iW, this[RM] = null, this.uiClass = xr, eY[NM] || (eY[NM] = {}, eY[NM][Aq.ARROW_TO] = !1), this[uM](eY[NM])
  };
  kq.prototype = {
    $uiClass: xr, type: BM, moveTo: function (t, i) {
      this[L_][of](t, i), this.firePathChange()
    }, lineTo: function (t, i) {
      this[L_][ff](t, i), this[gM]()
    }, quadTo: function (t, i, n, e) {
      this[L_].quadTo(t, i, n, e), this.firePathChange()
    }, curveTo: function (t, i, n, e, s, r) {
      this[L_][mu](t, i, n, e, s, r), this[gM]()
    }, arcTo: function (t, i, n, e, s) {
      this.path[sp](t, i, n, e, s), this[gM]()
    }, closePath: function () {
      this[L_].closePath(), this[gM]()
    }, clear: function () {
      this[L_][Qa](), this.firePathChange()
    }, removePathSegmentByIndex: function (t) {
      this[L_][$M](t) !== !1 && this.firePathChange()
    }, firePathChange: function () {
      this[L_]._63 = !0, this[Lv](new wY(this, yM))
    }, addPathSegment: function (t, i, n) {
      this[L_].add(new QU(i || XU, t), n), this.firePathChange()
    }
  }, m(kq, Tq), K(kq.prototype, {
    path: {
      get: function () {
        return this.image
      }, set: function (t) {
        this[Ap] = t
      }
    }, pathSegments: {
      get: function () {
        return this.path[FM]
      }, set: function (t) {
        this[L_][FM] = t || [], this[gM]()
      }
    }, length: {
      get: function () {
        return this[L_].length
      }
    }
  }), yU.ShapeNode = kq;
  var Mq = {
    _js: {}, register: function (t, i) {
      Mq._js[t] = i
    }, getShape: function (t, i, e, s, r, h) {
      s === n && (s = i, r = e, i = 0, e = 0), s || (s = 50), r || (r = 50);
      var a = Mq._js[t];
      return a ? a.generator instanceof Function ? a.generator(i, e, s, r, h) : a : void 0
    }, getRect: function (t, i, n, e, s, r, h) {
      return t instanceof Object && ha in t && (i = t.y, n = t.width, e = t[Ja], s = t.rx, r = t.ry, h = t[L_], t = t.x), Xe(h || new iW, t, i, n, e, s, r)
    }, getAllShapes: function (t, i, n, e, s) {
      var r = {};
      for (var h in Mq._js) {
        var a = Mq.getShape(h, t, i, n, e, s);
        a && (r[h] = a)
      }
      return r
    }, createRegularShape: function (t, i, n, e, s) {
      return is(t, i, n, e, s)
    }
  };
  ls(), vs[yh] = {type: GM}, m(vs, kq), yU.Bus = vs, bs[yh] = {
    _ha: function (t) {
      var i, n = t._jy;
      i = n ? n._fi : this[fb];
      var e = i[lh](t);
      if (0 > e) throw new Error(db + t + "' not exist in the box");
      for (; e >= 0;) {
        if (0 == e) return n instanceof Tq ? n : null;
        e -= 1;
        var r = i[Pl](e);
        if (r = s(r)) return r
      }
      return null
    }, forEachNode: function (t, i) {
      this[Dc](function (n) {
        return n instanceof Tq && t.call(i, n) === !1 ? !1 : void 0
      })
    }, _3m: null
  }, m(bs, DY), K(bs[yh], {
    propertyChangeDispatcher: {
      get: function () {
        return this._$n
      }
    }, currentSubNetwork: {
      get: function () {
        return this._3m
      }, set: function (t) {
        if (t && !t[$k] && (t = null), this._3m != t) {
          var i = this._3m;
          this._3m = t, this._$n[Lv](new wY(this, zM, t, i))
        }
      }
    }
  }), eY[HM] = gU[YM], eY[UM] = 5, eY.GROUP_EXPANDED = !0, eY[WM] = {width: 60, height: 60};
  var Oq = function (t, i, e) {
    w(this, Oq, arguments), (i === n || e === n) && (this[TM][qM] = !0), this.$groupType = eY.GROUP_TYPE, this[Qf] = eY[UM], this.$image = sW[XM], this.$minSize = eY[WM], this.expanded = eY[VM]
  };
  Oq[yh] = {
    type: ZM, $uiClass: lr, _9e: function () {
      return !this._h9 && !this._dh
    }, forEachOutEdge: function (t, i, n) {
      return We(this, t, i) === !1 ? !1 : !n && this._9e() && this._7x ? this._7x[Dc](t, i) : void 0
    }, forEachInEdge: function (t, i, n) {
      return qe(this, t, i) === !1 ? !1 : !n && this._9e() && this._95 ? this._95[Dc](t, i) : void 0
    }, forEachEdge: function (t, i, n) {
      return T(this, Oq, gu, arguments) === !1 ? !1 : n || n || !this._9e() ? void 0 : this._95 && this._95[Dc](t, i) === !1 ? !1 : this._7x ? this._7x.forEach(t, i) : void 0
    }, hasInEdge: function (t) {
      return t ? null != this._i8 : null != this._i8 || this._6f()
    }, hasOutEdge: function (t) {
      return t ? null != this._fb : null != this._fb || this._50()
    }, hasEdge: function (t) {
      return t ? null != this._i8 || null != this._fb : null != this._i8 || null != this._fb || this._97()
    }
  }, m(Oq, Tq), K(Oq[yh], {
    expanded: {
      get: function () {
        return this._h9
      }, set: function (t) {
        if (this._h9 != t) {
          var i = new wY(this, Fk, t, this._h9);
          this[Th](i) !== !1 && (this._h9 = t, this._$w(), this[Lv](i), this._dh || ys[ah](this))
        }
      }
    }
  }), A(Oq[yh], [KM, JM, xf, QM]), yU[tO] = Oq, gs.prototype[Fo] = iO, m(gs, Tq), yU[nO] = gs;
  var Iq = function (t) {
    this._jk = new uY, this._85 = new uY, this._fk = new uY, this.id = ++FH, t && (this[Ro] = t)
  };
  Iq[yh] = {
    invalidate: function () {
      this.invalidateData()
    },
    _18: !0,
    _jk: null,
    _85: null,
    _fk: null,
    _ncc: !1,
    _ju: 1,
    _jw: 1,
    _hg: !0,
    _86: 0,
    _64: 0,
    _jy: null,
    _nc8: null,
    borderColor: eO,
    borderLineDash: null,
    borderLineDashOffset: null,
    syncSelection: !0,
    syncSelectionStyles: !0,
    _15: function () {
      this[sO] = ui(this.anchorPosition, this._86, this._64)
    },
    setMeasuredBounds: function (t, i, n, e) {
      return t instanceof Object && (n = t.x, e = t.y, i = t[Ja], t = t.width), this._jk[ha] == t && this._jk[Ja] == i && this._jk.x == n && this._jk.y == e ? !1 : void this._jk.set(n || 0, e || 0, t || 0, i || 0)
    },
    initialize: function () {
    },
    measure: function () {
    },
    draw: function () {
    },
    _7z: function (t, i, n) {
      n[rE] == gU[qx] ? (t[ld] = n[eE], t[bd] = n.selectionShadowBlur * i, t[kp] = (n[rO] || 0) * i, t[Mp] = (n[tE] || 0) * i) : this._1o(t, i, n)
    },
    _1o: function (t, i, n) {
      var e = n[nE] || 0;
      n[hO] && (t[tf] = n.selectionBackgroundColor, t[aO](this._85.x - e / 2, this._85.y - e / 2, this._85[ha] + e, this._85[Ja] + e)), t[Ko] = n[eE], t[No] = e, t[Qo](this._85.x - e / 2, this._85.y - e / 2, this._85.width + e, this._85[Ja] + e)
    },
    _jm: function (t, i, n, e) {
      if (!this._hg) return !1;
      if (this[oO] || (n = this.selected), (n && !this[fO] || !e) && (e = this), t[vo](), 1 != this[cO] && (t[nf] = this.$alpha), t[sf](this.$x, this.$y), this.$rotatable && this[Xf] && t[Go](this[Xf]), (this[uO] || this.offsetY) && t[sf](this[uO], this[_O]), this.$rotate && t[Go](this[Wf]), this.$layoutByAnchorPoint && this[Uf] && t.translate(-this[Uf].x, -this[Uf].y), this[ld] && (t[ld] = this[ld], t[bd] = this[bd] * i, t.shadowOffsetX = this[kp] * i, t.shadowOffsetY = this[Mp] * i), n && e[rE] == gU.SELECTION_TYPE_BORDER_RECT && (this._1o(t, i, e), n = !1), this._$l() && this._lvShape && !this[rc]._empty) {
        this[rc].validate();
        var s = {
          lineWidth: this[ac],
          strokeStyle: this[dO],
          lineDash: this[lO],
          lineDashOffset: this[vO],
          fillColor: this[bO],
          fillGradient: this._naackgroundGradient,
          lineCap: $x,
          lineJoin: Bf
        };
        this[rc].draw(t, i, s, n, e), n = !1, t[ld] = aE
      }
      t[rf](), this[pf](t, i, n, e), t[Eo]()
    },
    invalidateData: function () {
      this.$invalidateData = !0, this[Zf] = !0, this._18 = !0
    },
    invalidateSize: function () {
      this[Zf] = !0, this._18 = !0
    },
    invalidateRender: function () {
      this._18 = !0
    },
    _4x: function () {
    },
    _$l: function () {
      return this[bO] || this[yO] || this.$border
    },
    _4g: function () {
      return this.$backgroundColor || this[yO]
    },
    doValidate: function () {
      return this[gO] && (this[gO] = !1, this[xO]() !== !1 && (this.$invalidateSize = !0)), this[Zf] && this[pO] && this[pO](), Jn.call(this) ? (this[pc] = !0, this.onBoundsChanged && this[EO](), !0) : this[mO] ? (this.$invalidateRotate = !0, this.$invalidateLocation = !1, !0) : void 0
    },
    validate: function () {
      var t = this._hg;
      return this.$invalidateVisibility && (this[wO] = !1, this._hg = this[TO], !this._hg || (this[yc] || this[kO]) && this._4x() !== !1 || (this._hg = !1)), this._hg ? (this._18 = !1, this[Db] || (this.initialize(), this[Db] = !0), this.doValidate()) : t != this._hg
    },
    _ho: function (t, i) {
      return t -= this.$x, i -= this.$y, Kn[ah](this, {x: t, y: i})
    },
    hitTest: function (t, i, n, e) {
      if (t -= this.$x, i -= this.$y, !this._fk[jo](t, i, n)) return !1;
      var s = Kn[ah](this, {x: t, y: i});
      return t = s.x, i = s.y, !e && this._$l() && this[rc] && this[rc][$_](t, i, n, !1, this.$border, this[bO] || this[yO]) ? !0 : this[MO](t, i, n)
    },
    doHitTest: function (t, i, n) {
      return this._jk.intersectsPoint(t, i, n)
    },
    hitTestByBounds: function (t, i, n, e) {
      var s = this._ho(t, i);
      return !e && this._$l() && this[rc] && this[rc][$_](t, i, n, !1, this[ac], this.$backgroundColor || this[yO]) ? !0 : this._jk.intersectsPoint(s.x, s.y, n)
    },
    onDataChanged: function () {
      this[gO] = !0, this._18 = !0, this[wO] = !0
    },
    getBounds: function () {
      var t = this._fk[dh]();
      return t[ep](this.x, this.y), this.parent && (this[su][Go] && Pi(t, this[su].rotate, t), t[ep](this.parent.x || 0, this[su].y || 0)), t
    },
    destroy: function () {
      this[Tm] = !0
    },
    _dn: !1
  }, K(Iq[yh], {
    originalBounds: {
      get: function () {
        return this._jk
      }
    }, data: {
      get: function () {
        return this[yc]
      }, set: function (t) {
        if (this.$data != t) {
          var i = this[yc];
          this.$data = t, this[OO](t, i)
        }
      }
    }, parent: {
      get: function () {
        return this._jy
      }
    }, showOnTop: {
      get: function () {
        return this._dn
      }, set: function (t) {
        t != this._dn && (this._dn = t, this._18 = !0, this._jy && this._jy._ncx && this._jy[IO](this))
      }
    }
  }), ps(Iq[yh], {
    visible: {value: !0, validateFlags: [SO, AO]},
    showEmpty: {validateFlags: [SO]},
    anchorPosition: {value: dY[ov], validateFlags: [jO, AO]},
    position: {value: dY[ov], validateFlags: [AO]},
    offsetX: {value: 0, validateFlags: [AO]},
    offsetY: {value: 0, validateFlags: [AO]},
    layoutByAnchorPoint: {value: !0, validateFlags: [PO, jO, AO]},
    padding: {value: 0, validateFlags: [PO]},
    border: {value: 0, validateFlags: [PO]},
    borderRadius: {value: eY[Jx]},
    showPointer: {value: !1, validateFlags: [PO]},
    pointerX: {value: 0, validateFlags: [PO]},
    pointerY: {value: 0, validateFlags: [PO]},
    pointerWidth: {value: eY.POINTER_WIDTH},
    backgroundColor: {validateFlags: [PO]},
    backgroundGradient: {validateFlags: [PO, CO]},
    selected: {value: !1, validateFlags: [PO]},
    selectionBorder: {value: eY.SELECTION_BORDER, validateFlags: [PO]},
    selectionShadowBlur: {value: eY.SELECTION_SHADOW_BLUR, validateFlags: [PO]},
    selectionColor: {value: eY[Zx], validateFlags: [PO]},
    selectionType: {value: eY[Kx], validateFlags: [PO]},
    selectionShadowOffsetX: {value: 0, validateFlags: [PO]},
    selectionShadowOffsetY: {value: 0, validateFlags: [PO]},
    shadowBlur: {value: 0, validateFlags: [PO]},
    shadowColor: {validateFlags: [PO]},
    shadowOffsetX: {value: 0, validateFlags: [PO]},
    shadowOffsetY: {value: 0, validateFlags: [PO]},
    renderColorBlendMode: {},
    renderColor: {},
    x: {value: 0, validateFlags: [AO]},
    y: {value: 0, validateFlags: [AO]},
    rotatable: {value: !0, validateFlags: [LO, PO]},
    rotate: {value: 0, validateFlags: [LO, PO]},
    _hostRotate: {validateFlags: [LO]},
    lineWidth: {value: 0, validateFlags: [$g]},
    alpha: {value: 1}
  });
  var Sq = [gU[t_], gU[n_], gU[i_]];
  ms.prototype = {
    removeBinding: function (t) {
      for (var i = Sq[rh]; --i >= 0;) {
        var n = Sq[i], e = this[n];
        for (var s in e) {
          var r = e[s];
          Array[Vu](r) ? (v(r, function (i) {
            return i.target == t
          }, this), r[rh] || delete e[s]) : r[Zu] == t && delete e[s]
        }
      }
    }, _1q: function (t, i, n) {
      if (!n && (n = this[i[xv] || gU[t_]], !n)) return !1;
      var e = n[t];
      e ? (Array[Vu](e) || (n[t] = e = [e]), e[Io](i)) : n[t] = i
    }, _28: function (t, i, n, e, s, r) {
      t = t || gU[t_];
      var h = this[t];
      if (!h) return !1;
      var a = {property: i, propertyType: t, bindingProperty: e, target: n, callback: s, invalidateSize: r};
      this._1q(i, a, h)
    }, onBindingPropertyChange: function (t, i, n, e) {
      var s = this[n || gU[t_]];
      if (!s) return !1;
      var r = s[i];
      return r ? (t._18 = !0, Es(t, r, n, e), !0) : !1
    }, initBindingProperties: function (t, i) {
      for (var e = Sq[rh]; --e >= 0;) {
        var s = Sq[e], r = this[s];
        for (var h in r) {
          var a = r[h];
          if (a.bindingProperty) {
            var o = a[Zu];
            if (o) {
              if (!(o instanceof Iq || (o = t[o]))) continue
            } else o = t;
            var f;
            f = i === !1 ? t[DO](a[Ku], s) : s == gU.PROPERTY_TYPE_STYLE ? t[S_][s_](t[yc], a[Ku]) : t[yc][a.property], f !== n && (o[a[Ju]] = f)
          }
        }
      }
    }
  };
  var Aq = {};
  Aq[Zx] = RO, Aq[Xx] = NO, Aq[Vx] = "selection.shadow.blur", Aq[BO] = "selection.shadow.offset.x", Aq.SELECTION_SHADOW_OFFSET_Y = "selection.shadow.offset.y", Aq[Kx] = $O, Aq[FO] = GO, Aq[zO] = "render.color.blend.mode", Aq[HO] = YO, Aq[UO] = WO, Aq[qO] = XO, Aq.SHADOW_OFFSET_X = VO, Aq.SHADOW_OFFSET_Y = ZO, Aq[KO] = JO, Aq[QO] = tI, Aq[iI] = "shape.stroke.fill.color", Aq.SHAPE_LINE_DASH = nI, Aq[eI] = "shape.line.dash.offset", Aq[sI] = rI, Aq.SHAPE_FILL_GRADIENT = hI, Aq[aI] = oI, Aq[fI] = cI, Aq[uI] = _I, Aq.BACKGROUND_COLOR = dI, Aq[lI] = vI, Aq[bI] = yI, Aq[gI] = xI, Aq[pI] = EI, Aq[mI] = "border.line.dash.offset", Aq.BORDER_RADIUS = wI, Aq[TI] = xf, Aq.LINE_CAP = kI, Aq[MI] = OI, Aq[II] = SI, Aq[AI] = jI, Aq[PI] = "image.background.color", Aq.IMAGE_BACKGROUND_GRADIENT = "image.background.gradient", Aq[CI] = LI, Aq.IMAGE_BORDER_STYLE = Aq[DI] = RI, Aq[NI] = "image.border.line.dash", Aq[BI] = "image.border.line.dash.offset", Aq[$I] = Aq[FI] = GI, Aq[zI] = HI, Aq[YI] = UI, Aq[WI] = qI, Aq[XI] = VI, Aq[ZI] = KI, Aq[JI] = QI, Aq.LABEL_VISIBLE = tS, Aq.LABEL_ANCHOR_POSITION = "label.anchor.position", Aq[iS] = nS, Aq.LABEL_FONT_SIZE = eS, Aq.LABEL_FONT_FAMILY = sS, Aq[rS] = hS, Aq[aS] = oS, Aq[fS] = cS, Aq.LABEL_POINTER = uS, Aq[_S] = dS, Aq.LABEL_OFFSET_X = lS, Aq[vS] = bS, Aq[yS] = gS, Aq[xS] = pS, Aq[ES] = mS, Aq[wS] = TS, Aq[kS] = "label.background.color", Aq[MS] = "label.background.gradient", Aq[OS] = IS, Aq.LABEL_SHADOW_BLUR = SS, Aq[AS] = jS, Aq[PS] = "label.shadow.offset.x", Aq.LABEL_SHADOW_OFFSET_Y = "label.shadow.offset.y", Aq.LABEL_Z_INDEX = CS, Aq[LS] = DS, Aq.GROUP_BACKGROUND_COLOR = "group.background.color", Aq.GROUP_BACKGROUND_GRADIENT = "group.background.gradient", Aq[RS] = NS, Aq[BS] = $S, Aq[FS] = "group.stroke.line.dash", Aq[GS] = "group.stroke.line.dash.offset", Aq[zS] = "edge.bundle.label.rotate", Aq.EDGE_BUNDLE_LABEL_POSITION = "edge.bundle.label.position", Aq[HS] = "edge.bundle.label.anchor.position", Aq.EDGE_BUNDLE_LABEL_COLOR = "edge.bundle.label.color", Aq[YS] = "edge.bundle.label.font.size", Aq[US] = "edge.bundle.label.font.family", Aq[WS] = "edge.bundle.label.font.style", Aq[qS] = "edge.bundle.label.padding", Aq.EDGE_BUNDLE_LABEL_POINTER_WIDTH = "edge.bundle.label.pointer.width", Aq.EDGE_BUNDLE_LABEL_POINTER = "edge.bundle.label.pointer", Aq[XS] = "edge.bundle.label.radius", Aq.EDGE_BUNDLE_LABEL_OFFSET_X = "edge.bundle.label.offset.x", Aq[VS] = "edge.bundle.label.offset.y", Aq[ZS] = "edge.bundle.label.border", Aq[KS] = "edge.bundle.label.border.color", Aq.EDGE_BUNDLE_LABEL_BACKGROUND_COLOR = "edge.bundle.label.background.color", Aq.EDGE_BUNDLE_LABEL_BACKGROUND_GRADIENT = "edge.bundle.label.background.gradient", Aq.EDGE_BUNDLE_LABEL_ROTATABLE = "edge.bundle.label.rotatable", Aq.EDGE_WIDTH = JS, Aq[QS] = tA, Aq.EDGE_OUTLINE = iA, Aq[nA] = eA,Aq.EDGE_LINE_DASH = sA,Aq.EDGE_LINE_DASH_OFFSET = "edge.line.dash.offset",Aq[M_] = rA,Aq.EDGE_TO_OFFSET = hA,Aq[aA] = oA,Aq.EDGE_BUNDLE_TYPE = fA,Aq.EDGE_BUNDLE_GAP = cA,Aq[H_] = uA,Aq[r_] = _A,Aq[g_] = dA,Aq[__] = "edge.split.by.percent",Aq[lA] = vA,Aq[bA] = yA,Aq[gA] = xA,Aq.EDGE_CORNER_RADIUS = pA,Aq[EA] = mA,Aq[wA] = TA,Aq[kA] = MA,Aq[O_] = OA,Aq[IA] = SA,Aq[AA] = jA,Aq.ARROW_FROM_OFFSET = PA,Aq.ARROW_FROM_STROKE = CA,Aq[LA] = "arrow.from.stroke.style",Aq[DA] = RA,Aq[NA] = "arrow.from.outline.style",Aq[BA] = $A,Aq[FA] = "arrow.from.line.dash.offset",Aq.ARROW_FROM_FILL_COLOR = "arrow.from.fill.color",Aq[GA] = "arrow.from.fill.gradient",Aq[zA] = HA,Aq[YA] = UA,Aq[WA] = qA,Aq[XA] = VA,Aq[ZA] = KA,Aq.ARROW_TO_STROKE = JA,Aq[QA] = "arrow.to.stroke.style",Aq[tj] = ij,Aq.ARROW_TO_OUTLINE_STYLE = "arrow.to.outline.style",Aq[nj] = ej,Aq[sj] = "arrow.to.line.dash.offset",Aq[rj] = hj,Aq.ARROW_TO_FILL_GRADIENT = "arrow.to.fill.gradient",Aq[aj] = oj,Aq[fj] = cj;
  var jq = new ms, Pq = gU.PROPERTY_TYPE_ACCESSOR, Cq = gU.PROPERTY_TYPE_STYLE, Lq = !1;
  jq._28(Cq, Aq[Kx], null, rE), jq._28(Cq, Aq[Xx], null, nE), jq._28(Cq, Aq.SELECTION_SHADOW_BLUR, null, Qp), jq._28(Cq, Aq[Zx], null, eE), jq._28(Cq, Aq[BO], null, "selectionShadowOffsetX"), jq._28(Cq, Aq.SELECTION_SHADOW_OFFSET_Y, null, "selectionShadowOffsetY"), jq._28(Pq, kh, Km, Ro), jq._28(Cq, Aq.LABEL_VISIBLE, Km, qT), jq._28(Cq, Aq[JI], Km, vc), jq._28(Cq, Aq[uj], Km, RM), jq._28(Cq, Aq[iS], Km, _j), jq._28(Cq, Aq.LABEL_FONT_SIZE, Km, $d), jq._28(Cq, Aq[ES], Km, Jg), jq._28(Cq, Aq.LABEL_BORDER_STYLE, Km, dO), jq._28(Cq, Aq[kS], Km, dj), jq._28(Cq, Aq.LABEL_ON_TOP, Km, lj), Lq || (jq._28(Cq, Aq[UO], null, bd), jq._28(Cq, Aq[qO], null, ld), jq._28(Cq, Aq[vj], null, kp), jq._28(Cq, Aq[bj], null, Mp), jq._28(Cq, Aq[yj], Km, Fd), jq._28(Cq, Aq[rS], Km, gj), jq._28(Cq, Aq[xS], Km, xj), jq._28(Cq, Aq[ZI], Km, Go), jq._28(Cq, Aq.LABEL_PADDING, Km, xf), jq._28(Cq, Aq.LABEL_POINTER_WIDTH, Km, pj), jq._28(Cq, Aq[Ej], Km, mj), jq._28(Cq, Aq[_S], Km, wj), jq._28(Cq, Aq[Tj], Km, uO), jq._28(Cq, Aq[vS], Km, _O), jq._28(Cq, Aq[OS], Km, kj), jq._28(Cq, Aq.LABEL_BACKGROUND_GRADIENT, Km, sc), jq._28(Cq, Aq[yS], Km, TT), jq._28(Cq, Aq[Mj], Km, bd), jq._28(Cq, Aq[AS], Km, ld), jq._28(Cq, Aq.LABEL_SHADOW_OFFSET_X, Km, kp), jq._28(Cq, Aq.LABEL_SHADOW_OFFSET_Y, Km, Mp), jq._28(Cq, Aq[Oj], Km, LT), jq._28(Cq, Aq[FO], null, cp), jq._28(Cq, Aq[zO], null, Op), jq._28(Cq, Aq[HO], null, YO));
  var Dq = new ms;
  Dq._28(Pq, IM), Dq._28(Pq, RM, null, Ij), Dq._28(Pq, Go, null, Go), Lq || (Dq._28(Cq, Aq.BACKGROUND_COLOR, null, dj), Dq._28(Cq, Aq.BACKGROUND_GRADIENT, null, sc), Dq._28(Cq, Aq[TI], null, xf), Dq._28(Cq, Aq[bI], null, Jg), Dq._28(Cq, Aq[Jx], null, wj), Dq._28(Cq, Aq[gI], null, dO), Dq._28(Cq, Aq.BORDER_LINE_DASH, null, lO), Dq._28(Cq, Aq.BORDER_LINE_DASH_OFFSET, null, vO)), Dq._28(Pq, Ap, Ap, Ro, Sj), Dq._28(Pq, TT, Ap, TT), Dq._28(Cq, Aq[KO], Ap, No), Dq._28(Cq, Aq.SHAPE_STROKE_STYLE, Ap, Ko), Dq._28(Cq, Aq[sI], Ap, oE), Dq._28(Cq, Aq[uI], Ap, bc), Lq || (Dq._28(Cq, Aq.IMAGE_ADJUST, Ap, Aj), Dq._28(Cq, Aq[aI], Ap, sE), Dq._28(Cq, Aq[fI], Ap, hE), Dq._28(Cq, Aq[jj], Ap, fE), Dq._28(Cq, Aq[Pj], Ap, Ec), Dq._28(Cq, Aq[eI], Ap, Mc), Dq._28(Cq, Aq[Cj], Ap, _d), Dq._28(Cq, Aq[MI], Ap, dd), Dq._28(Cq, Aq.IMAGE_BACKGROUND_COLOR, Ap, dj), Dq._28(Cq, Aq[Lj], Ap, sc), Dq._28(Cq, Aq[zI], Ap, xf), Dq._28(Cq, Aq[CI], Ap, Jg), Dq._28(Cq, Aq.IMAGE_BORDER_RADIUS, Ap, wj), Dq._28(Cq, Aq[DI], Ap, dO), Dq._28(Cq, Aq[NI], Ap, lO), Dq._28(Cq, Aq[BI], Ap, vO), Dq._28(Cq, Aq[YI], Ap, LT), Dq._28(Cq, Aq[XI], Ap, YO)), Dq._28(Pq, Fk, null, null, Dj), Dq._28(Pq, $k, null, null, Dj);
  var Rq = new ms;
  Rq._28(Pq, JM, null, null, Rj), Rq._28(Pq, QM, null, null, Rj), Rq._28(Pq, KM, null, null, Rj), Rq._28(Pq, xf, null, null, Rj), Rq._28(Cq, Aq[Nj], Bj, oE), Rq._28(Cq, Aq[$j], Bj, fE), Rq._28(Cq, Aq[RS], Bj, No), Rq._28(Cq, Aq[BS], Bj, Ko), Rq._28(Cq, Aq[FS], Bj, Ec), Rq._28(Cq, Aq[GS], Bj, Mc);
  var Nq = new ms;
  Nq._28(Pq, uu, Bj, null, Fj), Nq._28(Pq, EM, Bj, null, Fj), Nq._28(Pq, j_, Bj, null, Fj), Nq._28(Cq, Aq.EDGE_EXTEND, Bj, null, Fj), Nq._28(Cq, Aq[z_], Bj, null, Fj), Nq._28(Cq, Aq[Gj], Bj, No), Nq._28(Cq, Aq[QS], Bj, Ko), Nq._28(Cq, Aq[IA], Bj, zj), Nq._28(Cq, Aq[WA], Bj, Hj), Lq || (Nq._28(Cq, Aq[II], Bj, _E), Nq._28(Cq, Aq[AI], Bj, dE), Nq._28(Cq, Aq[aA], Bj, uE), Nq._28(Cq, Aq[EA], null, Yj, Fj), Nq._28(Cq, Aq[wA], null, R_, Fj), Nq._28(Cq, Aq[Uj], Bj, sE), Nq._28(Cq, Aq.EDGE_OUTLINE_STYLE, Bj, hE), Nq._28(Cq, Aq[Wj], Bj, Ec), Nq._28(Cq, Aq[qj], Bj, Mc), Nq._28(Cq, Aq[g_], Bj, null, Fj), Nq._28(Cq, Aq[M_], Bj, null, Fj), Nq._28(Cq, Aq[I_], Bj, null, Fj), Nq._28(Cq, Aq.EDGE_FROM_PORT, Bj, null, Fj), Nq._28(Cq, Aq[O_], Bj, null, Fj), Nq._28(Cq, Aq[Cj], Bj, _d), Nq._28(Cq, Aq.LINE_JOIN, Bj, dd), Nq._28(Pq, yM, null, null, Fj, !0), Nq._28(Pq, D_, null, null, Fj, !0), Nq._28(Cq, Aq[AA], Bj, Xj), Nq._28(Cq, Aq.ARROW_FROM_OFFSET, Bj, Vj), Nq._28(Cq, Aq[Zj], Bj, Kj), Nq._28(Cq, Aq[LA], Bj, Jj), Nq._28(Cq, Aq[DA], Bj, Qj), Nq._28(Cq, Aq[NA], Bj, "fromArrowOutlineStyle"), Nq._28(Cq, Aq[tP], Bj, iP), Nq._28(Cq, Aq[GA], Bj, "fromArrowFillGradient"), Nq._28(Cq, Aq.ARROW_FROM_LINE_DASH, Bj, nP), Nq._28(Cq, Aq[FA], Bj, "fromArrowLineDashOffset"), Nq._28(Cq, Aq[YA], Bj, eP), Nq._28(Cq, Aq[zA], Bj, sP), Nq._28(Cq, Aq.ARROW_TO_SIZE, Bj, rP), Nq._28(Cq, Aq[ZA], Bj, hP), Nq._28(Cq, Aq.ARROW_TO_STROKE, Bj, aP), Nq._28(Cq, Aq[QA], Bj, oP), Nq._28(Cq, Aq[tj], Bj, fP), Nq._28(Cq, Aq[cP], Bj, uP), Nq._28(Cq, Aq[rj], Bj, _P), Nq._28(Cq, Aq[dP], Bj, lP), Nq._28(Cq, Aq[nj], Bj, vP), Nq._28(Cq, Aq[sj], Bj, "toArrowLineDashOffset"), Nq._28(Cq, Aq[fj], Bj, bP), Nq._28(Cq, Aq[aj], Bj, yP));
  var Bq = new ms;
  Bq._28(Cq, Aq.EDGE_BUNDLE_LABEL_COLOR, gP, _j), Bq._28(Cq, Aq.EDGE_BUNDLE_LABEL_POSITION, gP, vc), Bq._28(Cq, Aq[HS], gP, RM), Bq._28(Cq, Aq[YS], gP, $d), Bq._28(Cq, Aq.EDGE_BUNDLE_LABEL_ROTATABLE, gP, kj), Lq || (Bq._28(Cq, Aq[zS], gP, Go), Bq._28(Cq, Aq.EDGE_BUNDLE_LABEL_FONT_FAMILY, gP, Fd), Bq._28(Cq, Aq[WS], gP, gj), Bq._28(Cq, Aq.EDGE_BUNDLE_LABEL_PADDING, gP, xf), Bq._28(Cq, Aq[xP], gP, pj), Bq._28(Cq, Aq[pP], gP, mj), Bq._28(Cq, Aq.EDGE_BUNDLE_LABEL_RADIUS, gP, wj), Bq._28(Cq, Aq.EDGE_BUNDLE_LABEL_OFFSET_X, gP, uO), Bq._28(Cq, Aq.EDGE_BUNDLE_LABEL_OFFSET_Y, gP, _O), Bq._28(Cq, Aq.EDGE_BUNDLE_LABEL_BORDER, gP, Jg), Bq._28(Cq, Aq[KS], gP, dO), Bq._28(Cq, Aq[EP], gP, dj), Bq._28(Cq, Aq.EDGE_BUNDLE_LABEL_BACKGROUND_GRADIENT, gP, sc));
  var $q = new ms;
  $q._28(Pq, IM), $q._28(Cq, Aq.BACKGROUND_COLOR, null, dj), $q._28(Cq, Aq.BACKGROUND_GRADIENT, null, sc), $q._28(Cq, Aq[TI], null, xf), $q._28(Cq, Aq[bI], null, Jg), $q._28(Cq, Aq[Jx], null, wj), $q._28(Cq, Aq[gI], null, dO), $q._28(Cq, Aq[pI], null, lO), $q._28(Cq, Aq.BORDER_LINE_DASH_OFFSET, null, vO), $q._28(Pq, Go, null, Go), $q._28(Pq, yM, null, null, mP), $q._28(Pq, L_, Ap, Ro), $q._28(Pq, TT, Ap, TT), $q._28(Cq, Aq[KO], Ap, No), $q._28(Cq, Aq[QO], Ap, Ko), $q._28(Cq, Aq[sI], Ap, oE), $q._28(Cq, Aq[jj], Ap, fE), Lq || ($q._28(Cq, Aq[II], Ap, _E), $q._28(Cq, Aq[AI], Ap, dE), $q._28(Cq, Aq[iI], Ap, uE), $q._28(Cq, Aq.SHAPE_OUTLINE, Ap, sE), $q._28(Cq, Aq[fI], Ap, hE), $q._28(Cq, Aq.SHAPE_LINE_DASH, Ap, Ec), $q._28(Cq, Aq.SHAPE_LINE_DASH_OFFSET, Ap, Mc), $q._28(Cq, Aq[Cj], Ap, _d), $q._28(Cq, Aq[MI], Ap, dd), $q._28(Cq, Aq[uI], Ap, bc), $q._28(Cq, Aq[PI], Ap, dj), $q._28(Cq, Aq[Lj], Ap, sc), $q._28(Cq, Aq[zI], Ap, xf), $q._28(Cq, Aq[CI], Ap, Jg), $q._28(Cq, Aq[FI], Ap, wj), $q._28(Cq, Aq[DI], Ap, dO), $q._28(Cq, Aq[NI], Ap, lO), $q._28(Cq, Aq[BI], Ap, vO), $q._28(Cq, Aq.ARROW_FROM, Ap, zj), $q._28(Cq, Aq[AA], Ap, Xj), $q._28(Cq, Aq.ARROW_FROM_OFFSET, Ap, Vj), $q._28(Cq, Aq[Zj], Ap, Kj), $q._28(Cq, Aq[LA], Ap, Jj), $q._28(Cq, Aq[tP], Ap, iP), $q._28(Cq, Aq[GA], Ap, "fromArrowFillGradient"), $q._28(Cq, Aq[BA], Ap, nP), $q._28(Cq, Aq[FA], Ap, "fromArrowLineDashOffset"), $q._28(Cq, Aq[YA], Ap, eP), $q._28(Cq, Aq[zA], Ap, sP), $q._28(Cq, Aq.ARROW_TO_SIZE, Ap, rP), $q._28(Cq, Aq.ARROW_TO_OFFSET, Ap, hP), $q._28(Cq, Aq[WA], Ap, Hj), $q._28(Cq, Aq[wP], Ap, aP), $q._28(Cq, Aq[QA], Ap, oP), $q._28(Cq, Aq[rj], Ap, _P), $q._28(Cq, Aq[dP], Ap, lP), $q._28(Cq, Aq.ARROW_TO_LINE_DASH, Ap, vP), $q._28(Cq, Aq.ARROW_TO_LINE_DASH_OFFSET, Ap, "toArrowLineDashOffset"), $q._28(Cq, Aq.ARROW_TO_LINE_JOIN, Ap, bP), $q._28(Cq, Aq[aj], Ap, yP));
  var Fq = function (t, i) {
    return t = t[LT], i = i[LT], t == i ? 0 : (t = t || 0, i = i || 0, t > i ? 1 : i > t ? -1 : void 0)
  }, Gq = function (t, i) {
    this.uiBounds = new uY, w(this, Gq, arguments), this.id = this.$data.id, this[S_] = i, this._fi = [], this[TP] = new ms
  };
  Gq.prototype = {
    syncSelection: !1, graph: null, layoutByAnchorPoint: !1, _nal: null, _fi: null, addChild: function (t, i) {
      t._jy = this, i !== n ? g(this._fi, t, i) : this._fi[Io](t), t._dn && this[IO](t), this.invalidateChildrenIndex(), this[kP](), this[MP] = !0
    }, removeChild: function (t) {
      this[TP][OP](t), t._jy = null, x(this._fi, t), this._ka && this._ka.remove(t), this[kP](), this.$invalidateChild = !0
    }, getProperty: function (t, i) {
      return i == gU[n_] ? this[S_].getStyle(this.$data, t) : i == gU.PROPERTY_TYPE_CLIENT ? this[yc].get(t) : this[yc][t]
    }, getStyle: function (t) {
      return this[S_][s_](this[yc], t)
    }, _$r: function (t, i, n) {
      var e = this._nal.onBindingPropertyChange(this, t, i, n);
      return jq[IP](this, t, i, n) || e
    }, onPropertyChange: function (t) {
      if (LT == t[rl]) return this.invalidateRender(), !0;
      if (rM == t.type) {
        if (mT == t[rl]) return this.invalidate(), !0;
        var i = t[Xu];
        return i && i.ui ? (Yv == t.kind ? this._9d(i) : _h == t.kind && this.removeChild(i.ui), !0) : !1
      }
      return this._$r(t[rl], t[xv] || Pq, t[Xu])
    }, label: null, initLabel: function () {
      var t = new Hq;
      t[kh] = Km, this.addChild(t), this.label = t
    }, initialize: function () {
      this[SP](), this[yc]._naw && this[yc][hM][Dc](this._9d, this), jq.initBindingProperties(this), this[TP][AP](this, !1)
    }, addBinding: function (t, i) {
      return i[Ku] ? (i.target = t, void this[TP]._1q(i[Ku], i)) : !1
    }, _gd: function (t, i) {
      var n = this.$data;
      if (!n._naw) return !1;
      var e = n[hM][Rl](t.id);
      if (!e || !e[jP]) return !1;
      var s = e.bindingProperties;
      if ($(s)) {
        var r = !1;
        return l(s, function (t) {
          return Ro == t.bindingProperty ? (r = ws(n, i, t[Ku], t.propertyType), !1) : void 0
        }, this), r
      }
      return Ro == s[Ju] ? ws(n, i, s[Ku], s[xv]) : !1
    }, _9d: function (t) {
      var i = t.ui;
      if (i) {
        var n = t[jP];
        n && (Array[Vu](n) ? n[Dc](function (t) {
          this.addBinding(i, t)
        }, this) : this[PP](i, n)), this[CP](i)
      }
    }, validate: function () {
      return this[Db] || (this.initialize(), this[Db] = !0), this[LP]()
    }, _8: !0, invalidateChildrenIndex: function () {
      this._8 = !0
    }, doValidate: function () {
      if (this._18 && (this._18 = !1, this[DP]() && (this.measure(), this.$invalidateSize = !0), this._8 && (this._8 = !1, VH ? this._fi = d(this._fi, Fq) : this._fi[CT](Fq))), Jn[ah](this) && (this[pc] = !0), this[pc]) {
        hW[ah](this), this[jd][Jf](this._fk);
        var t = this[RP] || 0, i = Math.max(this[RP] || 0, this.$shadowOffsetX || 0, this.$selectionShadowOffsetX || 0),
          n = Math.max(this[NP] || 0, this[BP] || 0), e = Math.max(2 * t, this.$shadowBlur, this[$P]);
        e += eY[FP] || 0;
        var s = e - i, r = e + i, h = e - n, a = e + n;
        return 0 > s && (s = 0), 0 > r && (r = 0), 0 > h && (h = 0), 0 > a && (a = 0), this[jd][tc](h, s, a, r), this[EO] && this[EO](), this[GP] = !0, !0
      }
    }, validateChildren: function () {
      var t = this[MP];
      this[MP] = !1;
      var i = this._naody, n = this[zP];
      i && (i.$renderColor = this[HP], i[YP] = this.$renderColorBlendMode, i.$shadowColor = this[UP], i[WP] = this[WP], i.$shadowOffsetX = this[qP], i[NP] = this[NP]), this[zP] = !1, i && i._18 && (n = i[So]() || n, i.$x = 0, i.$y = 0, i[pc] && hW.call(i), t = !0);
      for (var e = 0, s = this._fi[rh]; s > e; e++) {
        var r = this._fi[e];
        if (r != i) {
          var h = r._18 && r.validate();
          (h || n) && r._hg && ne(r, i, this), !t && h && (t = !0)
        }
      }
      return t
    }, measure: function () {
      this._jk[Qa]();
      for (var t, i, n = 0, e = this._fi[rh]; e > n; n++) t = this._fi[n], t._hg && (i = t._fk, i.width <= 0 || i.height <= 0 || this._jk[XP](t.$x + i.x, t.$y + i.y, i[ha], i[Ja]))
    }, _ka: null, _ncx: function (t) {
      if (!this._ka) {
        if (!t[lj]) return;
        return this._ka = new sY, this._ka.add(t)
      }
      return t[lj] ? this._ka.add(t) : this._ka[_h](t)
    }, draw: function (t, i, n) {
      for (var e, s = 0, r = this._fi[rh]; r > s; s++) e = this._fi[s], e._hg && !e[lj] && e._jm(t, i, n, this)
    }, _8y: function (t, i) {
      if (!this._hg || !this._ka || !this._ka.length) return !1;
      t[vo](), t.translate(this.$x, this.$y), this[qf] && this[Xf] && t[Go](this[Xf]), (this[uO] || this[_O]) && t[sf](this.offsetX, this.offsetY), this.$rotate && t.rotate(this[Wf]), this.$layoutByAnchorPoint && this[Uf] && t[sf](-this[Uf].x, -this[Uf].y), this[ld] && (t[ld] = this[ld], t[bd] = this[bd] * i, t[kp] = this[kp] * i, t[Mp] = this.shadowOffsetY * i), t[rf]();
      for (var n, e = 0, s = this._fi[rh]; s > e; e++) n = this._fi[e], n._hg && n[lj] && n._jm(t, i, this[Rk], this);
      t[Eo]()
    }, doHitTest: function (t, i, n) {
      if (n) {
        if (!this._jk[Xl](t - n, i - n, 2 * n, 2 * n)) return !1
      } else if (!this._jk[jo](t, i)) return !1;
      return this[VP](t, i, n)
    }, hitTestChildren: function (t, i, n) {
      for (var e, s = this._fi[rh] - 1; s >= 0; s--) if (e = this._fi[s], e._hg && e[$_](t, i, n)) return e;
      return !1
    }, destroy: function () {
      this[Tm] = !0;
      for (var t, i = this._fi[rh] - 1; i >= 0; i--) t = this._fi[i], t.destroy()
    }
  }, m(Gq, Iq), K(Gq[yh], {
    renderColorBlendMode: {
      get: function () {
        return this[YP]
      }, set: function (t) {
        this[YP] = t, this._18 = !0, this[gp] && (this[gp].renderColorBlendMode = this[YP])
      }
    }, renderColor: {
      get: function () {
        return this[HP]
      }, set: function (t) {
        this.$renderColor = t, this._18 = !0, this.body && (this[gp][cp] = this[HP])
      }
    }, bodyBounds: {
      get: function () {
        if (this[GP]) {
          this[GP] = !1;
          var t, i = this[gp];
          t = i && i._hg && !this._$l() ? i._fk.clone() : this._85.clone(), this[Go] && (t[ZP] = t[dh](), t.rotate = this[Go], t.tx = this.x, t.ty = this.y, Pi(t, this[Go], t)), t.x += this.$x, t.y += this.$y, this[KP] = t
        }
        return this[KP]
      }
    }, bounds: {
      get: function () {
        return new uY((this.$x || 0) + this.uiBounds.x, (this.$y || 0) + this.uiBounds.y, this.uiBounds[ha], this[jd][Ja])
      }
    }, body: {
      get: function () {
        return this[JP]
      }, set: function (t) {
        t && this._naody != t && (this[JP] = t, this.bodyChanged = !0, this[kP]())
      }
    }
  }), eY[FP] = 1;
  var zq = function () {
    w(this, zq, arguments)
  };
  zq[yh] = {
    strokeStyle: QP,
    lineWidth: 0,
    fillColor: null,
    fillGradient: null,
    _ju: 1,
    _jw: 1,
    outline: 0,
    onDataChanged: function (t) {
      T(this, zq, OO, arguments), this._kj && this._7s && this._kj._6l(this._7s, this), t && this._n97(t)
    },
    _n97: function (t) {
      this._kj = Mn(t), this._kj.validate(), (this._kj._lp == jU || this._kj._6a()) && (this._7s || (this._7s = function () {
        this[zk](), this._jy && this._jy[S_] && (this._jy[kP](), this._jy[S_][mT]())
      }), this._kj[Ef](this._7s, this))
    },
    _kj: null,
    initialize: function () {
      this._n97(this.$data)
    },
    _4x: function () {
      return this._kj && this._kj[pf]
    },
    _8r: function (t) {
      if (!t || t[ha] <= 0 || t[Ja] <= 0 || !this[tC] || !(this[TT] instanceof Object)) return this._ju = 1, void (this._jw = 1);
      var i = this.size[ha], e = this[TT][Ja];
      if ((i === n || null === i) && (i = -1), (e === n || null === e) && (e = -1), 0 > i && 0 > e) return this._ju = 1, void (this._jw = 1);
      var s, r, h = t.width, a = t.height;
      i >= 0 && (s = i / h), e >= 0 && (r = e / a), 0 > i ? s = r : 0 > e && (r = s), this._ju = s, this._jw = r
    },
    validateSize: function () {
      if (this[iC]) {
        this[iC] = !1;
        var t = this._originalBounds;
        this._ju, this._jw, this._8r(t), this[nC](t[ha] * this._ju, t[Ja] * this._jw, t.x * this._ju, t.y * this._jw)
      }
    },
    measure: function () {
      var t = this._kj[vf](this[No] + this.outline);
      return t ? (this.$invalidateScale = !0, void (this[eC] = t[dh]())) : void this._jk.set(0, 0, 0, 0)
    },
    onBoundsChanged: function () {
      this[sC] = !0
    },
    _1m: function () {
      this[sC] = !1, this._fillGradient = this.fillGradient ? BU.prototype.generatorGradient[ah](this[rC], this._85) : null
    },
    _kd: function (t) {
      var i, n;
      if (Vg == this.$adjustType) i = 1, n = -1; else {
        if (Zg != this.$adjustType) return;
        i = -1, n = 1
      }
      var e = this._jk.cx, s = this._jk.cy;
      t[sf](e, s), t[ef](i, n), t[sf](-e, -s)
    },
    draw: function (t, i, n, e) {
      if (this._ju && this._jw) {
        if (this[sC] && this._1m(), t[vo](), this.$adjustType && this._kd(t), this._kj._lp == CU) return t[ef](this._ju, this._jw), this._kj._lr[pf](t, i, this, n, e || this), void t[Eo]();
        n && this._7z(t, i, e), this._kj[pf](t, i, this, this._ju, this._jw), t[Eo]()
      }
    },
    doHitTest: function (t, i, n) {
      if (this._kj[$_]) {
        if (Vg == this[hC]) {
          var e = this._jk.cy;
          i = 2 * e - i
        } else if (Zg == this[hC]) {
          var s = this._jk.cx;
          t = 2 * s - t
        }
        t /= this._ju, i /= this._jw;
        var r = (this._ju + this._jw) / 2;
        return r > 1 && (n /= r, n = 0 | n), this._kj._lr instanceof iW ? this._kj._lr.hitTest(t, i, n, !0, this[aC], this[oC] || this.$fillGradient) : this._kj[$_](t, i, n)
      }
      return !0
    },
    $invalidateScale: !0,
    $invalidateFillGradient: !0
  }, m(zq, Iq), ps(zq.prototype, {
    adjustType: {},
    fillColor: {},
    size: {validateFlags: [PO, fC]},
    fillGradient: {validateFlags: [cC]}
  }), K(zq[yh], {
    originalBounds: {
      get: function () {
        return this[eC]
      }
    }
  }), eY.ALIGN_POSITION = dY.CENTER_MIDDLE;
  var Hq = function () {
    w(this, Hq, arguments), this[_j] = eY[iS]
  };
  Hq.prototype = {
    color: eY[iS],
    showPointer: !0,
    fontSize: null,
    fontFamily: null,
    fontStyle: null,
    _h4: null,
    alignPosition: null,
    measure: function () {
      this[Bd];
      var t = Fi(this[yc], this.$fontSize, this[uC], this[_C], eY.LINE_HEIGHT, this[dC]);
      if (this._h4 = t, this[tC]) {
        var i = this[tC][ha] || 0, n = this[tC][Ja] || 0;
        return this[nC](i > t[ha] ? i : t[ha], n > t[Ja] ? n : t[Ja])
      }
      return this[nC](t[ha], t[Ja])
    },
    doHitTest: function (t, i, n) {
      return this.$data ? $n(t, i, n, this) : !1
    },
    draw: function (t, i, n, e) {
      if (n && this._7z(t, i, e), this[lC] || eY[_o], this[qf] && this.$_hostRotate) {
        var s = vn(this[Xf]);
        s > rY && 3 * rY > s && (t.translate(this._jk[ha] / 2, this._jk[Ja] / 2), t[Go](Math.PI), t[sf](-this._jk.width / 2, -this._jk.height / 2))
      }
      var r = this[xj] || eY[vC], h = r.horizontalPosition, a = r[Kl], o = 0;
      h == vY ? (h = Vb, o += this._jk[ha] / 2) : h == bY ? (h = na, o += this._jk.width) : h = Ca;
      var f = 0;
      a == gY ? f = (this._jk[Ja] - this._h4[Ja]) / 2 : a == xY && (f = this._jk.height - this._h4.height), t[tf] = this.color, $i(t, this[yc], o, f, h, this[uC], this[lC], this[_C], eY[tp], this[dC])
    },
    _4x: function () {
      return null != this[yc] || this[tC]
    },
    $invalidateFont: !0
  }, m(Hq, Iq), ps(Hq.prototype, {
    size: {validateFlags: [$g]},
    fontStyle: {validateFlags: [$g, bC]},
    fontSize: {validateFlags: [$g, bC]},
    fontFamily: {validateFlags: [$g, bC]}
  }), K(Hq[yh], {
    font: {
      get: function () {
        return this[yC] && (this[yC] = !1, this[dC] = (this[_C] || eY.FONT_STYLE) + Ph + (this[lC] || eY[_o]) + lo + (this.$fontFamily || eY[jl])), this[dC]
      }
    }
  });
  var Yq = function (t) {
    t = t || new iW, this.pathBounds = new uY, w(this, Yq, [t])
  };
  Yq[yh] = {
    layoutByPath: !0,
    layoutByAnchorPoint: !1,
    measure: function () {
      this[gC] = !0, this.$invalidateToArrow = !0, this[yc][vf](this[aC] + this[xC], this[pC]), this[nC](this[pC])
    },
    validateSize: function () {
      if (this[gC] || this[EC]) {
        var t = this[pC][dh]();
        if (this[gC]) {
          this[gC] = !1;
          var i = this[mC]();
          i && t.add(i)
        }
        if (this.$invalidateToArrow) {
          this[EC] = !1;
          var i = this[wC]();
          i && t.add(i)
        }
        this[nC](t)
      }
    },
    validateFromArrow: function () {
      if (!this[yc]._iy || !this[TC]) return void (this.$fromArrowShape = null);
      var t = this[yc], i = 0, n = 0, e = this[kC];
      e && (isNaN(e) && (e.x || e.y) ? (i += e.x || 0, n += e.y || 0) : i += e || 0, i > 0 && 1 > i && (i *= t._iy)), this[MC] = t[kf](i, n), this[MC][Go] = Math.PI + this.fromArrowLocation[Go] || 0, this[OC] = Vs(this[TC], this[IC]);
      var s = this[OC].getBounds(this[SC][No] + this[SC][sE]);
      return this[AC] instanceof yU[jC] ? this[SC][cE] = BU[yh][PC][ah](this[AC], s) : this[SC] && (this[SC][cE] = null), s[ep](this.fromArrowLocation.x, this.fromArrowLocation.y), Ci(s, this[MC].rotate, s, this[MC].x, this[MC].y), s
    },
    validateToArrow: function () {
      if (!this[yc]._iy || !this.$toArrow) return void (this.$toArrowShape = null);
      var t = this.$data, i = 0, n = 0, e = this[CC];
      e && (isNaN(e) && (e.x || e.y) ? (i += e.x || 0, n += e.y || 0) : i += e || 0), 0 > i && i > -1 && (i *= t._iy), i += t._iy, this[LC] = t.getLocation(i, n), this[DC] = Vs(this.$toArrow, this[RC]);
      var s = this[DC][vf](this.toArrowStyles.lineWidth + this[NC][sE]);
      return this.toArrowFillGradient instanceof yU[jC] ? this[NC][cE] = BU[yh][PC].call(this[lP], s) : this.toArrowStyles && (this[NC][cE] = null), s.offset(this.toArrowLocation.x, this[LC].y), Ci(s, this[LC][Go], s, this[LC].x, this[LC].y), s
    },
    _21: function (t) {
      var i = t ? "from" : EM, e = this[i + BC];
      e === n && (e = this[aC]);
      var s = this[i + $C];
      s === n && (s = this.strokeStyle);
      var r = this[i + FC];
      r || (this[i + FC] = r = {}), r.lineWidth = e, r[Ko] = s, r[Ec] = this[i + GC], r[Mc] = this[i + zC], r[oE] = this[i + HC], r[fE] = this[i + YC], r.lineCap = this[i + UC], r[dd] = this[i + WC], r[sE] = this[i + qC] || 0, r[hE] = this[i + XC]
    },
    doValidate: function () {
      return this.$fromArrow && this._21(!0), this[VC] && this._21(!1), T(this, Yq, LP)
    },
    drawArrow: function (t, i, n, e) {
      if (this.$fromArrow && this.$fromArrowShape) {
        t[vo]();
        var s = this[MC], r = s.x, h = s.y, a = s[Go];
        t[sf](r, h), a && t.rotate(a), this[OC].draw(t, i, this[SC], n, e), t[Eo]()
      }
      if (this[VC] && this[DC]) {
        t[vo]();
        var s = this[LC], r = s.x, h = s.y, a = s[Go];
        t[sf](r, h), a && t[Go](a), this[DC][pf](t, i, this.toArrowStyles, n, e), t[Eo]()
      }
    },
    outlineStyle: null,
    outline: 0,
    onBoundsChanged: function () {
      this[sC] = !0
    },
    _1m: function () {
      this[sC] = !1, this[cE] = this[rC] ? BU.prototype.generatorGradient[ah](this.$fillGradient, this._85) : null
    },
    draw: function (t, i, n, e) {
      this[sC] && this._1m(), this[yc][pf](t, i, this, n, e), this[ZC](t, i, n, e)
    },
    doHitTest: function (t, i, n) {
      if (this[yc].hitTest(t, i, n, !0, this[aC] + this[xC], this[oC] || this[rC])) return !0;
      if (this[VC] && this[DC]) {
        var e = t - this.toArrowLocation.x, s = i - this.toArrowLocation.y;
        if (this[LC][Go]) {
          var r = Si(e, s, -this[LC].rotate);
          e = r.x, s = r.y
        }
        var h = this[NC].fillColor || this.toArrowStyles.fillGradient;
        if (this[DC][$_](e, s, n, !0, this[NC].lineWidth, h)) return !0
      }
      if (this[TC] && this.$fromArrowShape) {
        var e = t - this[MC].x, s = i - this.fromArrowLocation.y;
        if (this.fromArrowLocation[Go]) {
          var r = Si(e, s, -this[MC][Go]);
          e = r.x, s = r.y
        }
        var h = this.fromArrowStyles.fillColor || this[SC].fillGradient;
        if (this[OC][$_](e, s, n, !0, this.fromArrowStyles[No], h)) return !0
      }
      return !1
    },
    $fromArrowOutline: 0,
    $toArrowOutline: 0,
    $invalidateFillGradient: !0,
    $invalidateFromArrow: !0,
    $invalidateToArrow: !0
  }, m(Yq, Iq), ps(Yq[yh], {
    strokeStyle: {validateFlags: [KC, JC]},
    fillColor: {},
    fillGradient: {validateFlags: [cC]},
    fromArrowOffset: {validateFlags: [KC, PO]},
    fromArrowSize: {validateFlags: [KC, PO]},
    fromArrow: {validateFlags: [KC, PO]},
    fromArrowOutline: {validateFlags: [KC, PO]},
    fromArrowStroke: {validateFlags: [KC, PO]},
    fromArrowStrokeStyle: {validateFlags: [KC]},
    toArrowOffset: {validateFlags: [JC, PO]},
    toArrowSize: {validateFlags: [JC, PO]},
    toArrow: {validateFlags: [JC, PO]},
    toArrowOutline: {validateFlags: [JC, PO]},
    toArrowStroke: {validateFlags: [JC, PO]},
    toArrowStrokeStyle: {validateFlags: [JC]},
    outline: {value: 0, validateFlags: [$g]}
  }), K(Yq.prototype, {
    length: {
      get: function () {
        return this.data[rh]
      }
    }
  }), Ts.prototype = {
    shape: null, path: null, initialize: function () {
      T(this, Ts, QC), this[L_] = new iW, this[L_]._d6 = !1, this[Bj] = new Yq(this[L_]), this[CP](this[Bj], 0), this[JP] = this[Bj], Nq.initBindingProperties(this)
    }, _1j: !0, _57: null, _$l: function () {
      return !1
    }, _4g: function () {
      return !1
    }, validatePoints: function () {
      this[Bj][zk]();
      var t = this[yc], i = this[L_];
      i.clear();
      var n = t[ou], e = t[ru];
      n && e && nr(this, t, i, n, e)
    }, getEndPointBounds: function (t) {
      return t.getLinkableBounds()
    }, _3f: function (t, i, n, e, s, r, h, a, o) {
      return t.hasPathSegments() ? void (i[FM] = t.pathSegments[Nl]()) : n == e ? void this.drawLoopedEdge(i, n, s, r, a, o) : void this[tL](i, n, e, s, r, h, a, o)
    }, drawLoopedEdge: function (t, i, n, e) {
      hr(this, e, t)
    }, drawEdge: function (t, i, n, e, s, r, h, a) {
      if (e == gU[iL]) {
        var o = (h.x + a.x) / 2, f = (h.y + a.y) / 2, c = h.x - a.x, u = h.y - a.y, _ = Math[To](c * c + u * u),
          d = Math.atan2(u, c);
        d += Math.PI / 6, _ *= .04, _ > 30 && (_ = 30);
        var l = Math.cos(d) * _, v = Math.sin(d) * _;
        return t[ff](o - v, f + l), void t[ff](o + v, f - l)
      }
      var b = rr(this, this[Ro], s, r, i, n, h, a);
      b && (t._f6 = b)
    }, _1v: function () {
      if (!this[Ro].isBundleEnabled()) return null;
      var t = this[Ro].getEdgeBundle(!0);
      if (!t || !t.canBind(this[S_]) || !t._h9) return null;
      var i = t[nL](this);
      return t.isPositiveOrder(this[yc]) || (i = -i), i
    }, checkBundleLabel: function () {
      var t = this[eL]();
      return t ? (this[gP] || this[sL](), this[gP]._hg = !0, void (this.bundleLabel[Ro] = t)) : void (this.bundleLabel && (this[gP]._hg = !1, this.bundleLabel[Ro] = null))
    }, createBundleLabel: function () {
      var t = new Hq;
      t[rL] = !1, this.bundleLabel = t, this.addChild(this[gP]), Bq[AP](this)
    }, getBundleLabel: function () {
      return this[S_][eL](this.data)
    }, doValidate: function () {
      return this._1j && (this._1j = !1, this.validatePoints()), this[hL](), T(this, Ts, LP)
    }, _4i: function () {
      this._1j = !0, this[kP]()
    }, _$r: function (t, i, n) {
      var e = this._nal[IP](this, t, i, n);
      return e = jq[IP](this, t, i, n) || e, this[gP] && this.bundleLabel.$data && (e = Bq[IP](this, t, i, n) || e), Nq.onBindingPropertyChange(this, t, i, n) || e
    }
  }, m(Ts, Gq), Ts.drawReferenceLine = function (t, i, n, e) {
    if (t[of](i.x, i.y), !e || e == gU[aL]) return void t.lineTo(n.x, n.y);
    if (e == gU[u_]) t.lineTo(i.x, n.y); else if (e == gU[a_]) t.lineTo(n.x, i.y); else if (0 == e[lh](gU.EDGE_TYPE_ORTHOGONAL)) {
      var s;
      s = e == gU[h_] ? !0 : e == gU[c_] ? !1 : Math.abs(i.x - n.x) > Math.abs(i.y - n.y);
      var r = (i.x + n.x) / 2, h = (i.y + n.y) / 2;
      s ? (t.lineTo(r, i.y), t[ff](r, n.y)) : (t[ff](i.x, h), t[ff](n.x, h))
    } else if (0 == e[lh](gU[v_])) {
      var s, a = Uq[Aq[r_]] || 0;
      s = e == gU.EDGE_TYPE_ELBOW_HORIZONTAL ? !0 : e == gU[m_] ? !1 : Math.abs(i.x - n.x) > Math.abs(i.y - n.y), s ? (t.lineTo(i.x + a, i.y), t[ff](n.x - a, n.y)) : (t[ff](i.x, i.y + a), t[ff](n.x, n.y - a))
    } else if (0 == e[lh](oL)) {
      var a = Uq[Aq.EDGE_EXTEND] || 0;
      if (e == gU[d_]) {
        var o = Math.min(i.y, n.y) - a;
        t.lineTo(i.x, o), t.lineTo(n.x, o)
      } else if (e == gU[l_]) {
        var o = Math.max(i.y, n.y) + a;
        t.lineTo(i.x, o), t[ff](n.x, o)
      } else if (e == gU[o_]) {
        var f = Math.min(i.x, n.x) - a;
        t[ff](f, i.y), t.lineTo(f, n.y)
      } else if (e == gU[f_]) {
        var f = Math.max(i.x, n.x) + a;
        t[ff](f, i.y), t[ff](f, n.y)
      }
    } else if (e == gU[iL]) {
      var r = (i.x + n.x) / 2, h = (i.y + n.y) / 2, c = i.x - n.x, u = i.y - n.y, _ = Math.sqrt(c * c + u * u),
        d = Math[ta](u, c);
      d += Math.PI / 6, _ *= .04, _ > 30 && (_ = 30);
      var l = Math.cos(d) * _, v = Math.sin(d) * _;
      t[ff](r - v, h + l), t.lineTo(r + v, h - l)
    }
    t.lineTo(n.x, n.y)
  }, K(Ts.prototype, {
    length: {
      get: function () {
        return this[L_] ? this.path[rh] : 0
      }
    }
  }), Ts[yh][to] = function (t, i, n) {
    var e = Pn(this.path, t, i, this[Ro][fL], n);
    if (!e) return !1;
    var s = e.segments;
    if (s[rh] > 2) {
      var r = this[Ro], h = s[s.length - 1];
      h[Fo] == XU ? r.pathSegments = s.splice(1, s.length - 2) : (e[cL] && (h.invalidTerminal = !0), r[uL] = s[oh](1, s.length - 1))
    }
  }, ks.prototype = {
    _2h: null, image: null, initialize: function () {
      T(this, ks, QC), this._na8(), Dq[AP](this)
    }, _n97: function () {
      this[Ro][Ap] ? this[Ap] && (this[gp] = this[Ap]) : this[Km] && (this.body = this[Km])
    }, _na8: function () {
      this[Ap] = new zq, this[CP](this.image, 0), this[Sj]()
    }, doValidate: function () {
      this.body && (this instanceof lr && !this[yc].groupImage && this._4z() ? this[gp][Yf] = !1 : (this[gp].$layoutByAnchorPoint = null != this._2h, this[gp].anchorPosition = this._2h));
      var t = this[yc][TM], i = 0, n = 0;
      t && (i = t.x, n = t.y);
      var e = this.$x != i || this.$y != n;
      return e && (this[GP] = !0), this.$x = i, this.$y = n, Gq.prototype[LP][ah](this) || e
    }, _$r: function (t, i, n) {
      var e = this._nal[IP](this, t, i, n);
      return e = jq[IP](this, t, i, n) || e, Dq.onBindingPropertyChange(this, t, i, n) || e
    }, getLinkablePorts: function () {
      return this[Ro][_L]
    }, getLinkableBounds: function () {
      return this[dL]
    }, getDefaultPortPoint: function (t) {
      return ci(dY.CENTER_MIDDLE, t || this.getLinkableBounds())
    }, getPortPoint: function (t, i) {
      if (!t) return this.getDefaultPortPoint(i);
      i = i || this[lL]();
      var n;
      return i[ZP] ? (n = ci(t, i[ZP]), i[Go] && (n = Ai(n.x, n.y, i.rotate, i.rotateX || 0, i[vL] || 0)), n.x += i.tx || 0, n.y += i.ty || 0) : n = ci(t, i), n.port = t, n
    }, getPortPoints: function () {
      var t = this[bL]();
      if (t && Array.isArray(t)) {
        var i = [], n = this[lL]();
        return t.forEach(function (t) {
          i[Io](this[k_](t, n))
        }.bind(this)), i
      }
    }
  }, m(ks, Gq);
  var Uq = {};
  Uq[Aq.SELECTION_COLOR] = eY.SELECTION_COLOR, Uq[Aq.SELECTION_BORDER] = eY[Xx], Uq[Aq[Vx]] = eY[Vx], Uq[Aq[Kx]] = gU[qx], Uq[Aq[BO]] = 2, Uq[Aq[yL]] = 2, Uq[Aq.LABEL_COLOR] = eY.LABEL_COLOR, Uq[Aq[JI]] = dY[fv], Uq[Aq[uj]] = dY[av], Uq[Aq.LABEL_PADDING] = new _Y(0, 2), Uq[Aq[fS]] = 8, Uq[Aq[_S]] = 8, Uq[Aq.LABEL_POINTER] = !0, Uq[Aq[ES]] = 0, Uq[Aq[wS]] = QP, Uq[Aq[OS]] = !0, Uq[Aq[kS]] = null, Uq[Aq[MS]] = null, Uq[Aq.EDGE_COLOR] = gL, Uq[Aq[Gj]] = 1.5, Uq[Aq.EDGE_FROM_AT_EDGE] = !0, Uq[Aq[wA]] = !0, Uq[Aq[Nj]] = V(3438210798), Uq[Aq[RS]] = 1, Uq[Aq[BS]] = QP, Uq[Aq[WA]] = !0, Uq[Aq.ARROW_FROM_SIZE] = eY.ARROW_SIZE, Uq[Aq[XA]] = eY[w_], Uq[Aq[H_]] = 10, Uq[Aq[xL]] = 8, Uq[Aq.EDGE_CORNER] = gU.EDGE_CORNER_ROUND, Uq[Aq[__]] = !0, Uq[Aq[r_]] = 20, Uq[Aq[lA]] = .5, Uq[Aq[bA]] = 20, Uq[Aq.EDGE_BUNDLE_GAP] = 20, Uq[Aq[HS]] = dY.CENTER_BOTTOM, Uq[Aq[pL]] = dY.CENTER_TOP, Uq[Aq[EL]] = mL, Uq[Aq[KO]] = 1, Uq[Aq[QO]] = wL, Uq[Aq.RENDER_COLOR_BLEND_MODE] = eY.BLEND_MODE, Uq[Aq.ALPHA] = 1, eY.LOOKING_EDGE_ENDPOINT_TOLERANCE = 2;
  var Wq = function (i, n) {
    this._$n = new SY, this._$n.on(function (t) {
      zM == t[rl] && this.invalidateVisibility()
    }, this), this._1d = new SY, this._1d[ol](function (t) {
      !this[zM] || t.kind != PY[tb] && t[rl] != PY.KIND_REMOVE || this[Zk].contains(this[zM]) || (this.currentSubNetwork = null)
    }, this), this._4 = new SY, this._$z = new SY, this._$f = new SY, this._$k = new SY, this[Zk] = n || new bs, this._8f = new dq(this, i), this._2y = new Xr(this), this._13 = new SY, this[TL] = GY(t, kL, function () {
      this[ML]()
    }, !1, this), this._8f[VT].ondrop = function (t) {
      this[OL](t)
    }[el](this), this._8f[VT][IL] = function (t) {
      this[IL](t)
    }[el](this)
  };
  Wq[yh] = {
    originAtCenter: !0,
    editable: !1,
    ondragover: function (t) {
      yU[SL](t)
    },
    getDropInfo: function (t, i) {
      var n = null;
      if (i) try {
        n = JSON[Ha](i)
      } catch (e) {
      }
      return n
    },
    ondrop: function (t) {
      var i = t.dataTransfer;
      if (i) {
        var n = i[Uy](Ud), e = this[AL](t, n);
        e || (e = {}, e[Ap] = i.getData(Ap), e.type = i.getData(Fo), e[Km] = i.getData(Km), e[QM] = i[Uy](QM));
        var s = this.globalToLocal(t);
        if (s = this[QT](s.x, s.y), !(this[jL] instanceof Function && this.dropAction[ah](this, t, s, e) === !1) && (e.image || e[Km] || e[Fo])) {
          var r = e[Ap], h = e[Fo], a = e[Km], o = e[QM];
          yU.stopEvent(t);
          var f;
          if (h && PL != h ? nO == h ? f = this[CL](a, s.x, s.y) : LL == h ? f = this.createShapeNode(a, s.x, s.y) : tO == h ? (f = this[DL](a, s.x, s.y), o && (o = _r(o), o && (f[QM] = o))) : (h = J(h), h instanceof Function && h[yh] instanceof Tq && (f = new h, f[kh] = a, f[IM] = new aY(s.x, s.y), this[tM].add(f))) : f = this[RL](a, s.x, s.y), f) {
            if (r && (r = _r(r), r && (f.image = r)), t.shiftKey) {
              var c = this[qy](t);
              c && this[Xo](c) && (f.parent = c)
            }
            if (e.properties) for (var u in e[NL]) f[u] = e[NL][u];
            if (e.clientProperties) for (var u in e[BL]) f.set(u, e.clientProperties[u]);
            if (e.styles && f.putStyles(e[$L]), this[FL](f, t, e) === !1) return !1;
            var _ = new qr(this, qr.ELEMENT_CREATED, t, f);
            return this[GL](_), f
          }
        }
      }
    },
    _nc2: function (t) {
      return t.enableSubNetwork || t instanceof Oq || t[zL]
    },
    enableDoubleClickToOverview: !0,
    _8f: null,
    _$n: null,
    _1d: null,
    _4: null,
    _$k: null,
    _$z: null,
    _$f: null,
    _1i: function (t) {
      return this._$n[Th](t)
    },
    _4e: function (t) {
      this._$n.onEvent(t), kT == t[rl] && this[HL]()
    },
    isVisible: function (t) {
      return this._8f._e7(t)
    },
    isMovable: function (t) {
      return (t instanceof Tq || t instanceof wq && t.hasPathSegments()) && t[YL] !== !1
    },
    isSelectable: function (t) {
      return t.selectable !== !1
    },
    isEditable: function (t) {
      return t[rL] !== !1
    },
    isRotatable: function (t) {
      return t[kj] !== !1
    },
    isResizable: function (t) {
      return t[UL] !== !1
    },
    canLinkFrom: function (t) {
      return t.linkable !== !1 && t[WL] !== !1
    },
    canLinkTo: function (t, i) {
      return t.linkable === !1 || t[qL] === !1 ? !1 : i instanceof yU[tO] && t[XL](i) ? !1 : t instanceof yU[tO] && i[XL](t) ? !1 : !0
    },
    isEndPointEditable: function (t) {
      return t[VL] !== !1
    },
    createNode: function (t, i, n) {
      var e = new Tq(t, i, n);
      return this[tM].add(e), e
    },
    createText: function (t, i, n) {
      var e = new gs(t, i, n);
      return this._kpModel.add(e), e
    },
    createShapeNode: function (t, i, n, e) {
      R(i) && (e = n, n = i, i = null);
      var s = new kq(t, i);
      return s[TM] = new aY(n, e), this[tM].add(s), s
    },
    createGroup: function (t, i, n) {
      var e = new Oq(t, i, n);
      return this[tM].add(e), e
    },
    createEdge: function (t, i, n) {
      if (t instanceof Tq) {
        var e = n;
        n = i, i = t, t = e
      }
      var s = new wq(i, n);
      return t && (s[eM] = t), this._kpModel.add(s), s
    },
    addElement: function (t, i) {
      this[tM].add(t), i && t[eh]() && t.forEachChild(function (t) {
        this[ZL](t, i)
      }, this)
    },
    removeElement: function (t) {
      this._kpModel[_h](t)
    },
    clear: function () {
      this._kpModel[Qa]()
    },
    getStyle: function (t, i) {
      var e = t._jc[i];
      return e !== n ? e : this[KL](i)
    },
    getDefaultStyle: function (t) {
      if (this._jc) {
        var i = this._jc[t];
        if (i !== n) return i
      }
      return Uq[t]
    },
    _2u: function (t, i) {
      if (!this.limitedBounds || this[JL].contains(this[Qk])) return i && i(), !1;
      t = this._1t(), this[QL]();
      var n, e, s, r = this[Qk], h = this[JL], a = r.width / this.limitedBounds.width,
        o = r[Ja] / this.limitedBounds[Ja];
      if (1 >= a && 1 >= o) return n = h[Ca] > r[Ca] ? h.left : h[na] < r[na] ? r[Ca] - (r.right - h[na]) : r[Ca], e = h.top > r.top ? h.top : h[ea] < r[ea] ? r.top - (r.bottom - h[ea]) : r.top, void this[Ik](-n * this.scale, -e * this.scale, this.scale, !1, i);
      var f = a > o;
      s = Math.max(a, o), f ? (n = h.x, e = h.y + (r.top - h.top) * (1 - s) / s, e >= h.y ? e = h.y : e < h[ea] - r.height / s && (e = h[ea] - r[Ja] / s)) : (e = h.y, n = h.x + (r[Ca] - h.left) * (1 - s) / s, n >= h.x ? n = h.x : n < h[na] - r[ha] / s && (n = h.right - r[ha] / s)), s *= this[ef], n *= s, e *= s, this.translateTo(-n, -e, s, t, i)
    },
    checkLimitedBounds: function (t) {
      return this[tD] || !this[JL] || this[JL][B_](this.viewportBounds) ? !1 : (this[tD] = !0, void this.callLater(function () {
        this._2u(t, function () {
          this[tD] = !1
        }[el](this))
      }, this))
    },
    zoomByMouseEvent: function (t, i, n, e) {
      var s = this.globalToLocal(t);
      return Lh == typeof i ? this[iD](Math.pow(1.1, i), s.x, s.y, n, e) : i ? this[nD](s.x, s.y, n, e) : this[eD](s.x, s.y, n, e)
    },
    resetScale: 1,
    translate: function (t, i, n) {
      return this.translateTo(this.tx + t, this.ty + i, this[ef], n)
    },
    translateTo: function (t, i, n, e, s) {
      if (n && (n = Math.min(this[Sp], Math.max(this[sD], n))), e) {
        var r = this._61();
        return void r._l8(t, i, n, e, s)
      }
      var h = this._8f._ncr(t, i, n);
      return s && s(), h
    },
    centerTo: function (t, i, e, s, r) {
      return (!e || 0 >= e) && (e = this[ef]), s === n && (s = this._1t()), this[Ik](this.width / 2 - t * e, this.height / 2 - i * e, e, s, r)
    },
    moveToCenter: function (t, i) {
      if (arguments[2] === !1 || !this._8f[rD]()) {
        var n = this[Ao];
        return void this[hD](n.cx, n.cy, t, i)
      }
      return this._8f[Db] || (i = !1), this[aD](this.moveToCenter.bind(this, t, i, !1))
    },
    zoomToOverview: function (t, i) {
      if (arguments[2] === !1 || !this._8f[rD]()) {
        var n = this._8f._1g();
        return void (n && (i && (n[ef] = Math.min(n.scale, i)), this[hD](n.cx, n.cy, n[ef], t)))
      }
      return this._8f[Db] || (t = !1), this[aD](this[oD][el](this, t, i, !1))
    },
    _1t: function () {
      return this._8f._ncc ? this[fD] === n || null === this.zoomAnimation ? eY[cD] : this.zoomAnimation : !1
    },
    zoomAt: function (t, i, e, s, r) {
      s === n && (s = this._1t()), i === n && (i = this[ha] / 2), i = i || 0, e === n && (e = this.height / 2), e = e || 0;
      var h = this[ef];
      return t = Math.min(this[Sp], Math.max(this[sD], h * t)), i = t * (this.tx - i) / h + i, e = t * (this.ty - e) / h + e, this.translateTo(i, e, t, s, r)
    },
    zoomOut: function (t, i, n, e) {
      return this.zoomAt(1 / this.scaleStep, t, i, n, e)
    },
    zoomIn: function (t, i, n, e) {
      return this[iD](this[uD], t, i, n, e)
    },
    _61: function () {
      return this[_D] || (this[_D] = new Jq(this)), this[_D]
    },
    onAnimationStart: function () {
    },
    onAnimationEnd: function () {
    },
    isAnimating: function () {
      return this[_D] && this[_D]._em()
    },
    enableInertia: !0,
    _8v: function (t, i) {
      var n = this._61();
      return n._fg(t || 0, i || 0)
    },
    stopAnimation: function () {
      this[_D] && this[_D]._lm()
    },
    getUI: function (t) {
      return Q(t) ? this._8f._3q(t) : this._8f._le(t)
    },
    getUIByMouseEvent: function (t) {
      return this._8f._3q(t)
    },
    hitTest: function (t) {
      return this._8f[$_](t)
    },
    globalToLocal: function (t) {
      return this._8f._7n(t)
    },
    toCanvas: function (t, i) {
      return this._8f._gj(t, i)
    },
    toLogical: function (t, i) {
      return Q(t) ? this._8f._$a(t) : this._8f._d7(t, i)
    },
    getElementByMouseEvent: function (t) {
      var i = this._8f._3q(t);
      return i ? i[yc] : void 0
    },
    getElement: function (t) {
      return Q(t) ? this[qy](t) : this[tM][Rl](t)
    },
    invalidate: function () {
      this._8f._naq()
    },
    invalidateUI: function (t) {
      t[mT](), this.invalidate()
    },
    invalidateElement: function (t) {
      this._8f._3o(t)
    },
    getUIBounds: function (t) {
      return this._8f._2w(t)
    },
    forEachVisibleUI: function (t, i) {
      return this._8f._3v(t, i)
    },
    forEachReverseVisibleUI: function (t, i) {
      return this._8f._$t(t, i)
    },
    forEachUI: function (t, i) {
      return this._8f._ej(t, i)
    },
    forEachReverseUI: function (t, i) {
      return this._8f._3w(t, i)
    },
    forEach: function (t, i) {
      return this._kpModel[Dc](t, i)
    },
    getElementByName: function (t) {
      var i;
      return this.forEach(function (n) {
        return n[kh] == t ? (i = n, !1) : void 0
      }), i
    },
    focus: function (i) {
      if (i) {
        var n = t[Xd] || t[Sa], e = t.scrollY || t[ja];
        return this[Wy][dD](), void t[Zd](n, e)
      }
      this.canvasPanel[dD]()
    },
    callLater: function (t, i, n) {
      this._8f._dr(t, i, n)
    },
    exportImage: function (t, i, n) {
      return mr(this, t, i, n)
    },
    setSelection: function (t) {
      return this[tM][sb].set(t)
    },
    select: function (t) {
      return this[tM]._selectionModel[qd](t)
    },
    unselect: function (t) {
      return this[tM][sb][lD](t)
    },
    reverseSelect: function (t) {
      return this._kpModel[sb][vD](t)
    },
    selectAll: function () {
      Er(this)
    },
    unSelectAll: function () {
      this[wd].clear()
    },
    unselectAll: function () {
      this[bD]()
    },
    isSelected: function (t) {
      return this[tM]._selectionModel[B_](t)
    },
    sendToTop: function (t) {
      Re(this[tM], t)
    },
    sendToBottom: function (t) {
      Ne(this[tM], t)
    },
    moveElements: function (t, i, n) {
      var e = [], s = new sY;
      return l(t, function (t) {
        t instanceof Tq ? e[Io](t) : t instanceof wq && s.add(t)
      }), this._es(e, i, n, s)
    },
    _es: function (t, i, n, e) {
      if (0 == i && 0 == n || 0 == t[rh] && 0 == e.length) return !1;
      if (0 != t.length) {
        var s = this._3z(t);
        e = this._4r(s, e), l(s, function (t) {
          var e = t[TM];
          e ? t[yD](e.x + i, e.y + n) : t[yD](i, n)
        })
      }
      return e && e.length && this._e1(e, i, n), !0
    },
    _e1: function (t, i, n) {
      t.forEach(function (t) {
        t[pg](i, n)
      })
    },
    _4r: function (t, i) {
      return this[Zk].forEach(function (n) {
        n instanceof wq && this[gD](n) && t[B_](n.fromAgent) && t[B_](n[ru]) && i.add(n)
      }, this), i
    },
    _3z: function (t) {
      var i = new sY;
      return l(t, function (t) {
        !this.isMovable(t), i.add(t), Ce(t, i, this[xD])
      }, this), i
    },
    reverseExpanded: function (t) {
      if (!t[mk]()) return !1;
      var i = t[hu](!0);
      return i ? i[pD]() !== !1 ? (this[mT](), !0) : void 0 : !1
    },
    _2y: null,
    _13: null,
    beforeInteractionEvent: function (t) {
      return this._13[Th](t)
    },
    onInteractionEvent: function (t) {
      this._13[Lv](t)
    },
    addCustomInteraction: function (t) {
      this._2y[ED](t)
    },
    removeCustomInteraction: function (t) {
      this._2y.removeCustomInteraction(t)
    },
    enableWheelZoom: !0,
    enableTooltip: !0,
    getTooltip: function (t) {
      return t[cM] || t[kh]
    },
    updateViewport: function () {
      this._8f._7d()
    },
    destroy: function () {
      this._4e(new wY(this, eg, !0, this[Tm])), this._ieed = !0, zY(t, kL, this[TL]), this._2y[eg](), this[Zk] = new bs;
      var i = this.html;
      this._8f._ie(), i && (i[mD] = "")
    },
    onPropertyChange: function (t, i, n) {
      this._$n[ol](function (e) {
        e[rl] == t && i[ah](n, e)
      })
    },
    removeSelection: function () {
      var t = this[wd]._je;
      return t && 0 != t[rh] ? (t = t[ch](), this._kpModel[_h](t), t) : !1
    },
    removeSelectionByInteraction: function (t) {
      var i = this[wd][al];
      return i && 0 != i.length ? void yU[qg](wD + i.length, function () {
        var i = this[TD]();
        if (i) {
          var n = new qr(this, qr[kD], t, i);
          this.onInteractionEvent(n)
        }
      }, this) : !1
    },
    createShapeByInteraction: function (t, i, n, e) {
      var s = new iW(i);
      i[rh] > 2 && s[zf]();
      var r = this.createShapeNode(MD, s, n, e);
      this[FL](r, t);
      var h = new qr(this, qr[OD], t, r);
      return this[GL](h), r
    },
    createLineByInteraction: function (t, i, n, e) {
      var s = new iW(i), r = this.createShapeNode(ID, s, n, e);
      r[e_](yU[SD][sI], null), r[e_](yU.Styles[jj], null), r[e_](yU[SD][uI], !0), this.onElementCreated(r, t);
      var h = new qr(this, qr[OD], t, r);
      return this.onInteractionEvent(h), r
    },
    createEdgeByInteraction: function (t, i, n, e, s, r) {
      var h = this[AD](jD, t, i);
      if (s && h[e_](yU.Styles[kA], s), r && h[e_](yU.Styles[O_], r), e) h._8s = e; else {
        var a = this[PD], o = this.edgeType;
        this.interactionProperties && (a = this.interactionProperties[Bk] || a, o = this[CD][j_] || o), a && (h[Bk] = a), o && (h[j_] = o)
      }
      this.onElementCreated(h, n);
      var f = new qr(this, qr[OD], n, h);
      return this[GL](f), h
    },
    onElementCreated: function (t) {
      !t[su] && this.currentSubNetwork && (t[su] = this[zM])
    },
    allowEmptyLabel: !1,
    startLabelEdit: function (t, i, n, e) {
      var s = this;
      n[LD](e.x, e.y, i[Ro], this[s_](t, Aq.LABEL_FONT_SIZE), function (n) {
        return s.onLabelEdit(t, i, n, i.parent)
      })
    },
    onLabelEdit: function (t, i, n, e) {
      if (!n && !this[DD]) return yU[Ew](RD), !1;
      if (Km == i.name) {
        if (t[kh] == n) return !1;
        t[kh] = n
      } else e._gd(i, n) === !1 && (i[Ro] = n, this.invalidateElement(t))
    },
    setInteractionMode: function (t, i) {
      this.interactionMode = t, this.interactionProperties = i
    },
    upSubNetwork: function () {
      return this._3m ? this[zM] = dr(this._3m) : !1
    },
    _$o: !1,
    invalidateVisibility: function () {
      this._$o = !0, this[mT]()
    },
    getBundleLabel: function (t) {
      var i = t.getEdgeBundle(!0);
      return i && i[ND] == t ? BD + i[$D][rh] : null
    },
    zoomAnimation: null,
    pauseRendering: function (t, i) {
      (this.delayedRendering || i) && this._8f._6e(t)
    },
    _49: n,
    enableRectangleSelectionByRightButton: !0,
    getLinkablePoints: function (t) {
      return t[FD]
    }
  }, K(Wq[yh], {
    center: {
      get: function () {
        return this[QT](this[GD][wa] / 2, this.html.clientHeight / 2)
      }
    }, visibleFilter: {
      get: function () {
        return this[Ek]
      }, set: function (t) {
        this[Ek] = t, this[Sk]()
      }
    }, topCanvas: {
      get: function () {
        return this._8f[gk]
      }
    }, propertyChangeDispatcher: {
      get: function () {
        return this._$n
      }
    }, listChangeDispatcher: {
      get: function () {
        return this._1d
      }
    }, dataPropertyChangeDispatcher: {
      get: function () {
        return this._4
      }
    }, selectionChangeDispatcher: {
      get: function () {
        return this._$k
      }
    }, parentChangeDispatcher: {
      get: function () {
        return this._$z
      }
    }, childIndexChangeDispatcher: {
      get: function () {
        return this._$f
      }
    }, interactionDispatcher: {
      get: function () {
        return this._13
      }
    }, cursor: {
      set: function (t) {
        this[Wy][va][zD] = t || this._2y[Id]
      }, get: function () {
        return this[Wy][va][zD]
      }
    }, interactionMode: {
      get: function () {
        return this._2y._n9urrentMode
      }, set: function (t) {
        var i = this[HD];
        i != t && (this._2y[fl] = t, this._4e(new wY(this, HD, t, i)))
      }
    }, scaleStep: {
      get: function () {
        return this._8f._eh
      }, set: function (t) {
        this._8f._eh = t
      }
    }, maxScale: {
      get: function () {
        return this._8f._gb
      }, set: function (t) {
        this._8f._gb = t
      }
    }, minScale: {
      get: function () {
        return this._8f._g9
      }, set: function (t) {
        this._8f._g9 = t
      }
    }, scale: {
      get: function () {
        return this._8f[ty]
      }, set: function (t) {
        return this._8f._scale = t
      }
    }, tx: {
      get: function () {
        return this._8f._tx
      }
    }, ty: {
      get: function () {
        return this._8f._ty
      }
    }, styles: {
      get: function () {
        return this._jc
      }, set: function (t) {
        this._jc = t
      }
    }, selectionModel: {
      get: function () {
        return this[tM][sb]
      }
    }, graphModel: {
      get: function () {
        return this[tM]
      }, set: function (t) {
        if (this[tM] == t) return !1;
        var i = this._kpModel, n = new wY(this, Zk, i, t);
        return this._1i(n) === !1 ? !1 : (null != i && (i[YD][Bv](this._$n, this), i.listChangeDispatcher.removeListener(this._1d, this), i.dataChangeDispatcher[Bv](this._4, this), i.parentChangeDispatcher[Bv](this._$z, this), i[ob][Bv](this._$f, this), i[eb][Bv](this._$k, this)), this._kpModel = t, this._kpModel && (this._kpModel.propertyChangeDispatcher[ol](this._$n, this), this[tM][nb].addListener(this._1d, this), this[tM][rb].addListener(this._4, this), this[tM][ab][ol](this._$z, this), this[tM][ob][ol](this._$f, this), this._kpModel[eb][ol](this._$k, this)), this._8f && this._8f._lc(), void this._4e(n))
      }
    }, count: {
      get: function () {
        return this._kpModel[rh]
      }
    }, width: {
      get: function () {
        return this.html[wa]
      }
    }, height: {
      get: function () {
        return this[GD].clientHeight
      }
    }, viewportBounds: {
      get: function () {
        return this._8f._viewportBounds
      }
    }, bounds: {
      get: function () {
        return this._8f._3x()
      }
    }, canvasPanel: {
      get: function () {
        return this._8f._nci
      }
    }, html: {
      get: function () {
        return this._8f._nci[lp]
      }
    }, navigationType: {
      get: function () {
        return this._8f._6m
      }, set: function (t) {
        this._8f._3j(t)
      }
    }, _3m: {
      get: function () {
        return this[tM]._3m
      }
    }, currentSubNetwork: {
      get: function () {
        return this._kpModel[zM]
      }, set: function (t) {
        this[tM][zM] = t
      }
    }, limitedBounds: {
      get: function () {
        return this[UD]
      }, set: function (t) {
        return uY[WT](t, this[UD]) ? !1 : t ? void (this[UD] = new uY(t)) : void (this._limitedBounds = null)
      }
    }, ratio: {
      get: function () {
        return this._8f[io]
      }
    }, delayedRendering: {
      get: function () {
        return this._49 === n ? eY.DELAYED_RENDERING : this._49
      }, set: function (t) {
        t != this[WD] && (this._49 = t, this[qD](!1, !0))
      }
    }, fullRefresh: {
      get: function () {
        return this._8f[ST]
      }, set: function (t) {
        this._8f[ST] = t
      }
    }
  }), eY[XD] = !0, eY[VD] = 60, eY[ZD] = 60, lr.prototype = {
    _n97: function () {
      return this._4z() ? void 0 : T(this, lr, Sj, arguments)
    }, initialize: function () {
      T(this, lr, QC), this.checkBody()
    }, _na9: function () {
      this._lx = new iW, this[Bj] = new zq(this._lx), this[Bj].layoutByPath = !1, this.addChild(this[Bj], 0), this.body = this[Bj]
    }, checkBody: function () {
      return this._4z() ? (this._1r = !0, this[Bj] ? (this[Bj][qT] = !0, this.body = this[Bj]) : (this[KD](), Rq.initBindingProperties(this)), void (this[Ap] && (this[Ap].visible = !1))) : (this.image ? (this.image[qT] = !0, this[gp] = this[Ap]) : this[JD](), void (this[Bj] && (this[Bj].visible = !1)))
    }, _4z: function () {
      return this[yc]._hi() && this[yc][Fk]
    }, _lx: null, _1r: !0, _4y: function () {
      this._18 = !0, this._1r = !0
    }, doValidate: function () {
      if (this._1r && this._4z()) {
        if (this._1r = !1, this[Bj][zk](), this[yc].groupImage) {
          this[Bj][Ro] = this[yc].groupImage;
          var t = this._1u();
          return this[Bj][uO] = t.x + t[ha] / 2, this[Bj][_O] = t.y + t[Ja] / 2, this[Bj][TT] = {
            width: t.width,
            height: t[Ja]
          }, ks[yh][LP].call(this)
        }
        this[Bj][uO] = 0, this.shape[_O] = 0;
        var i = this._8d(this[yc][JM]);
        this._lx[Qa](), i instanceof uY ? Xe(this._lx, i.x, i.y, i[ha], i[Ja], i.rx, i.ry) : i instanceof rn ? Ve(this._lx, i) : i instanceof hn && Ze(this._lx, i), this._lx._63 = !0, this[Bj][zk]()
      }
      return ks.prototype.doValidate[ah](this)
    }, _6o: function (t, i, n, e, s) {
      switch (Lh != typeof e && (e = -i / 2), Lh != typeof s && (s = -n / 2), t) {
        case gU[QD]:
          var r = Math.max(i, n) / 2;
          return new rn(e + i / 2, s + n / 2, r);
        case gU[tR]:
          return new hn(e + i / 2, s + n / 2, i, n);
        default:
          return new uY(e, s, i, n)
      }
    }, _1u: function () {
      return this._8d(null)
    }, _8d: function (t) {
      var i, e, s = this[Ro], r = s[xf], h = s.minSize, a = eY.GROUP_MIN_WIDTH, o = eY[ZD];
      if (h && (Lh == typeof h[ha] && (a = h.width), Lh == typeof h[Ja] && (o = h[Ja]), i = h.x, e = h.y), !s[eh]()) return this._6o(t, a, o, i, e);
      var f, c = this[yc]._fi._je;
      (t == gU[QD] || t == gU[tR]) && (f = []);
      for (var u, _, d, l, v = new uY, b = 0, y = c[rh]; y > b; b++) {
        var g = c[b];
        if (this.graph.isVisible(g) && !(g instanceof wq)) {
          var x = this.graph[A_](g);
          x && (u = x.$x + x._fk.x, _ = x.$y + x._fk.y, d = x._fk[ha], l = x._fk[Ja], v[XP](u, _, d, l), f && (f[Io]({
            x: u,
            y: _
          }), f.push({x: u + d, y: _}), f[Io]({x: u + d, y: _ + l}), f.push({x: u, y: _ + l})))
        }
      }
      if (v[gc]()) return this._6o(t, a, o, i, e);
      var p = this.$data[TM];
      p ? p[qM] && (p.invalidateFlag = !1, i === n && (p.x = v.cx), e === n && (p.y = v.cy)) : p = this[yc][TM] = {
        x: v.cx,
        y: v.cy
      }, r && v.grow(r), Lh == typeof i && i + p.x < v.x && (v[ha] += v.x - (i + p.x), v.x = i + p.x, f && f.push({
        x: v.x,
        y: v.cy
      })), Lh == typeof e && e + p.y < v.y && (v.height += v.y - (v.y, e + p.y), v.y = e + p.y, f && f.push({
        x: v.cx,
        y: v.y
      }));
      var E, i = p.x, e = p.y;
      if (t == gU.GROUP_TYPE_CIRCLE) {
        E = an(f), E.cx -= i, E.cy -= e;
        var m = Math.max(a, o) / 2;
        return E.r < m && (E.cx += m - E.r, E.cy += m - E.r, E.r = m), E
      }
      return t == gU.GROUP_TYPE_ELLIPSE ? (E = on(f, v), E.cx -= i, E.cy -= e, E[ha] < a && (E.cx += (a - E[ha]) / 2, E.width = a), E.height < o && (E.cy += (o - E.height) / 2, E.height = o), E) : (E = v, v[ha] < a && (v[ha] = a), v.height < o && (v[Ja] = o), v[ep](-i, -e), E)
    }, _$r: function (t, i, n) {
      if (!this._4z()) return T(this, lr, iR, arguments);
      var e = this[TP][IP](this, t, i, n);
      return e = jq[IP](this, t, i, n) || e, e = Dq.onBindingPropertyChange(this, t, i, n) || e, Rq[IP](this, t, i, n) || e
    }
  }, m(lr, ks), yU[nR] = lr;
  var qq = {
    draw: function () {
    }
  };
  eY[yd] = null, eY[ad] = null;
  var Xq = {position: cT, "text-align": Vb}, Vq = {padding: eR, transition: sR}, Zq = {position: sd, display: rR};
  xi(hR, "opacity:0.7;vertical-align:middle;"), xi(".Q-Graph-Nav img:hover,img.hover", aR), iY || (xi(oR, fR + $Y(cR) + uR), xi(_R, dR + $Y(cR) + lR)), yr[yh] = {
    _nav: function (t, i) {
      return t._hg == i ? !1 : (t._hg = i, void (t[va][vR] = i ? "visible" : nd))
    }, _3a: function (t, i) {
      var n = i / 2 - this[bR]._img[Ed] / 2 + no;
      this._left[yR][va].top = n, this[fd][yR].style.top = n, this[Q_].style[ha] = t + no, this._navPane[va].height = i + no
    }, _9z: function (t, i, n, e) {
      this[gR](this[rd], t), this._nav(this[bR], i), this[gR](this._naottom, n), this._nav(this[fd], e)
    }, _ie: function () {
      var t = this[Q_].parentNode;
      t && t.removeChild(this._navPane)
    }, _k4: function () {
      var t = this[tu]._kp;
      if (t) {
        var i = t[Ao];
        if (i[gc]()) return void this._9z(!1, !1, !1, !1);
        var n = t[Qk], e = n.y > i.y + 1, s = n.x > i.x + 1, r = n[ea] < i[ea] - 1, h = n[na] < i[na] - 1;
        this._9z(e, s, r, h)
      }
    }
  };
  var Kq = 10;
  xi(xR, pR), xi(ER, "background-color: #7E7E7E;" + $Y(cR) + ": background-color 0.2s linear;"), xi(".Q-Graph-ScrollBar--V", "width: 8px;right: 0px;"), xi(".Q-Graph-ScrollBar--H", "height: 8px;bottom: 0px;"), xi(".Q-Graph-ScrollBar--V.Both", mR), xi(".Q-Graph-ScrollBar--H.Both", wR), iY || (xi(TR, fR + $Y(cR) + kR), xi(".Q-Graph:hover .Q-Graph-ScrollPane", dR + $Y(cR) + ":opacity 0.3s linear;")), gr[yh] = {
    _ie: function () {
      this._verticalDragSupport._ie(), this[MR]._ie(), delete this[OR], delete this[MR], this._lz[lp] && this._lz[lp][xp](this._lz)
    }, _lz: null, _naj: null, _8b: null, init: function (t) {
      var n = i[oo](W_);
      n[eu] = IR, bi(n, {width: hd, height: hd, position: sd});
      var e = i.createElement(W_);
      e.className = "Q-Graph-ScrollBar Q-Graph-ScrollBar--V";
      var s = i.createElement(W_);
      s[eu] = "Q-Graph-ScrollBar Q-Graph-ScrollBar--H", n.appendChild(e), n[nu](s), t[nu](n), this._lz = n, this._8b = s, this[SR] = e, s.isH = !0;
      var r = this, h = {
        onstart: function (t, i) {
          i[Ah].add(pd)
        }, onrelease: function (t, i) {
          i[Ah][_h](pd)
        }, ondrag: function (t, i) {
          var n = r[tu]._kp;
          if (n) {
            var e = i.isH, s = e ? t.dx : t.dy;
            if (s && i.scale) {
              var h = n.scale / i.scale;
              e ? n.translate(-h * s, 0) : n[sf](0, -h * s), yU[SL](t)
            }
          }
        }, enddrag: function (t, i) {
          var n = r[tu]._kp;
          if (n && n[AR]) {
            var e = i.isH, s = e ? t.vx : t.vy;
            if (Math.abs(s) > .1) {
              var h = n[ef] / i[ef];
              s *= h, e ? n._8v(-s, 0) : n._8v(0, -s)
            }
          }
        }
      };
      this[OR] = new wi(e, h), this[MR] = new wi(s, h)
    }, _3a: function () {
      var t = this._naaseCanvas._kp;
      t && t[aD](this._k4[el](this))
    }, _k4: function () {
      var t = this[tu]._kp;
      if (t) {
        var i = t[Ao];
        if (i[gc]()) return this._48(!1), void this._4c(!1);
        var n = t[Qk], e = t[ha], s = t.height, r = t.scale, h = 1 / r, a = n.x > i.x + h || n[na] < i[na] - h,
          o = n.y > i.y + h || n[ea] < i[ea] - h, f = a && o;
        f ? (L(this[SR], jR), L(this._8b, jR)) : (D(this._naj, jR), D(this._8b, jR)), this._48(a, n, i, f ? e - Kq : e), this._4c(o, n, i, f ? s - Kq : s)
      }
    }, _48: function (t, i, n, e) {
      if (!t) return this._8b.style[Kw] = ed, void (this._8b[ef] = 0);
      var s = Math.min(i.x, n.x), r = Math.max(i.right, n.right), h = r - s, a = e / h;
      this._8b[ef] = a, this._8b[va].left = parseInt((i.x - s) * a) + no, this._8b[va][na] = parseInt((r - i[na]) * a) + no, this._8b.style[Kw] = ""
    }, _4c: function (t, i, n, e) {
      if (!t) return this[SR][va].display = ed, void (this._naj[ef] = 0);
      var s = Math.min(i.y, n.y), r = Math.max(i.bottom, n[ea]), h = r - s, a = e / h;
      this[SR][ef] = a, this[SR][va].top = parseInt((i.y - s) * a) + no, this[SR][va].bottom = parseInt((r - i[ea]) * a) + no, this._naj[va][Kw] = ""
    }
  }, xr.prototype = {
    shape: null, initialize: function () {
      T(this, xr, QC), this[JD](), $q.initBindingProperties(this)
    }, _na8: function () {
      this[Ap] = new Yq(this[yc][L_]), this.addChild(this.image, 0), this.body = this.image
    }, invalidateShape: function () {
      this[Ap][zk](), this.invalidateRender()
    }, _$r: function (t, i, n) {
      var e = this._nal[IP](this, t, i, n);
      return e = jq[IP](this, t, i, n) || e, $q[IP](this, t, i, n) || e
    }, doValidate: function () {
      this[gp] && (this[Ap].data = this[Ro][L_], this[gp].$layoutByAnchorPoint = null != this._2h, this[gp][RM] = this._2h);
      var t = this[yc].$location, i = 0, n = 0;
      t && (i = t.x, n = t.y);
      var e = this.$x != i || this.$y != n;
      return e && (this[GP] = !0), this.$x = i, this.$y = n, Gq[yh][LP].call(this) || e
    }, getLinkablePorts: function () {
      return this.data[_L]
    }, getLinkableBounds: function () {
      return this[dL]
    }, getDefaultPortPoint: function (t) {
      return ci(dY[ov], t || this.getLinkableBounds())
    }, getPortPoint: function (t, i) {
      if (!t) return this.getDefaultPortPoint(i);
      i = i || this[lL]();
      var n;
      return i.original ? (n = ci(t, i[ZP]), i.rotate && (n = Ai(n.x, n.y, i[Go], i.rotateX || 0, i[vL] || 0)), n.x += i.tx || 0, n.y += i.ty || 0) : n = ci(t, i), n[PR] = t, n
    }, getPortPoints: function () {
      var t = this.getLinkablePorts();
      if (t && Array.isArray(t)) {
        var i = [], n = this[lL]();
        return t[Dc](function (t) {
          i.push(this[k_](t, n))
        }[el](this)), i
      }
    }
  }, m(xr, Gq), K(xr[yh], {
    path: {
      get: function () {
        return this[Ro].path
      }
    }, length: {
      get: function () {
        return this[Ro][rh]
      }
    }
  }), xr[yh].addPoint = function (t, i, n) {
    var e = this._ho(t, i), s = this.data, r = Pn(this[L_], e.x, e.y, this[Ro][fL], n);
    return r ? void (s[uL] = r.segments) : !1
  }, pr.prototype = {
    _m1: function () {
      this._jf[va].visibility = qT
    }, _k8: function () {
      this._jf[va].visibility = nd
    }, clear: function () {
      this._9b[Qa](), this._naq()
    }, contains: function (t) {
      return t instanceof Object && t.id && (t = t.id), this._9b[Ll](t)
    }, _4t: function (t) {
      pq.setStyle(this._jf, K_, t ? YT + t[Hc](Yh) + ")" : "")
    }, addDrawable: function (t, i) {
      if (i) {
        var n = {id: ++FH, drawable: t, scope: i};
        return this._9b.add(n), n
      }
      return t.id || (t.id = ++FH), this._9b.add(t), t
    }, removeDrawable: function (t) {
      return t.id ? void this._9b[_h](t) : this._9b[CR](t)
    }, _9b: null, invalidate: function () {
      this[wT]()
    }, _naq: function () {
      this[tu]._63 || this._jm()
    }, _hw: function (t, i) {
      this._jf[uo](t, i)
    }, _jm: function () {
      var t = this[tu]._scale, i = this.g;
      i._l6(), i[vo](), this[tu]._9i(i);
      for (var n = this._9b._je, e = 0, s = n[rh]; s > e; e++) i[vo](), i[rf](), this._g2(i, n[e], t), i.restore();
      i[Eo]()
    }, _g2: function (t, i, n) {
      return i instanceof Function ? void i(t, n) : void (i[LR] instanceof Function && i.scope && i[LR].call(i.scope, t, n))
    }
  }, eY.ZOOM_ANIMATE = !0;
  var Jq = function (t) {
    this._kp = t
  };
  eY[DR] = 600, eY[RR] = dU.easeOut, Jq.prototype = {
    _kp: null, _ew: null, _fg: function (t, i, n) {
      this._lm();
      var e = Math.abs(t / 2), s = Math.abs(i / 2), r = Math.min(eY[DR], .6 * Math.max(e, s) * 1e3);
      if (10 > r) return !1;
      var h = function (t) {
        return -(2 * Math.pow(t, 1.5) - 3 * t)
      }, a = t * r / 3, o = i * r / 3;
      this._l8(this._kp.tx + a, this._kp.ty + o, 0, {duration: r, animationType: h}, n)
    }, _6s: function (t, i, n, e, s) {
      this._ew && this._ew._lm(), s && (this[NR] = !0, this._kp[qD](!0)), this._4j(), this._ew = new vU(t, this, i, n), this._ew._6g = this._6g[el](this), this._ew._kn(e)
    }, _4j: function () {
      this._kp[BR]()
    }, _6g: function () {
      this.__delayRender && (this._kp.pauseRendering(!1), delete this.__delayRender), this._kp[$R]()
    }, _em: function () {
      return this._ew && this._ew._em()
    }, _lm: function () {
      this._ew && this._ew._lm()
    }, _i2: function (t) {
      var i = this._fromTX + (this[FR] - this[GR]) * t, n = this[zR] + (this[HR] - this[zR]) * t,
        e = this[YR] + (this[UR] - this[YR]) * t;
      this._kp[Ik](i, n, e, this[WR])
    }, _l8: function (t, i, n, e, s) {
      this._lm();
      var r = this._kp, h = r[ef];
      if (0 >= n && (n = h), t != r.tx || i != r.ty || n != h) {
        var a, o, f;
        e instanceof Object && (a = e.duration, o = e[qR], f = e[XR]);
        var c = r.tx, u = r.ty;
        if (!a) if (n != h) {
          var _ = n > h ? n / h : h / n;
          _ = Math.log(_) / Math.log(1.3), a = 60 * _
        } else {
          var d = oY(t, i, c, u);
          a = d / 2
        }
        o = o || eY[DR], f = f || eY.ANIMATION_TYPE, a = Math.min(o, a), this[GR] = c, this._fromTY = u, this[YR] = h, this[FR] = t, this[HR] = i, this[UR] = n, this._6s(this._i2, a, f, s, n != h)
      }
    }
  }, eY.INTERACTION_HANDLER_SIZE_TOUCH = 8, eY.INTERACTION_HANDLER_SIZE_DESKTOP = 4, eY.INTERACTION_ROTATE_HANDLER_SIZE_TOUCH = 30, eY[VR] = 20;
  var Qq = Math.PI / 4;
  wr[yh] = {
    element: null, _$j: !1, setCurrentElement: function (t, i) {
      this.element = t, i && this[ZR]()
    }, onDataPropertyChange: function () {
    }, addDataPropertyChangeListener: function () {
      this._$j || (this._$j = !0, this[S_][KR][ol](this._1a, this))
    }, removeDataPropertyChangeListener: function () {
      this._$j && (this._$j = !1, this[S_][KR][Bv](this._1a, this))
    }, _1a: function (t) {
      this[JR] && t[mf] == this.element && this.onDataPropertyChange(t)
    }, onElementRemoved: function (t, i) {
      this[JR] && (t == this[JR] || $(t) && p(t, this.element)) && this[eg](i)
    }, onClear: function (t) {
      this[JR] && this.destroy(t)
    }, destroy: function () {
      delete this[JR], this[QR](), this[tN](), this[iN] = null
    }, invalidate: function () {
      this[Md][mT]()
    }, removeDrawable: function () {
      this[nN] && (this[Md][tN](this[nN]), delete this[nN], this[mT]())
    }, drawableList: null, addDrawable: function () {
      this._lkableId || (this[nN] = this[Md].addDrawable(this[pf], this).id, this[mT]())
    }, isShowing: function () {
      return this[nN]
    }, draw: function (t, i) {
      this[iN] && this[iN][Dc](function (n) {
        n[pf](t, i)
      }), this.doDraw(t, i)
    }, doDraw: function () {
    }, escapable: !0, onkeydown: function (t, i) {
      this[eN] && 27 == t.keyCode && (z(t), this.destroy(i))
    }
  }, yU.DrawableInteraction = wr, Tr.prototype = {
    defaultCursor: Sd, getInteractionInstances: function (t) {
      if (!this[Od]) return null;
      for (var i = [], n = 0, e = this[Od][rh]; e > n; n++) {
        var s = this[Od][n];
        s instanceof Function && (s = new s(t)), s[sN] instanceof Function && s[sN](t), i[Io](s)
      }
      return i
    }
  }, kr[yh] = {
    _d9: null, _k2: null, destroy: function () {
      T(this, kr, eg, arguments), delete this._k2, delete this._98, delete this._d9
    }, doDraw: function (t) {
      var i = this[$a];
      i && (t[rf](), i[Dc](function (i) {
        this.drawPoint(t, i)
      }, this), this[rN] && t.closePath(), this[hN](t))
    }, styleDraw: function (t) {
      var i = Mr(this[S_].interactionProperties, this[aN](this.graph));
      i[No] && (t[No] = i[No], i.lineCap && (t.lineCap = i[_d]), i.lineJoin && (t[dd] = i[dd]), i[Ec] && (t.lineDash = i[Ec], t[Mc] = i.lineDashOffset || 0), t.strokeStyle = i.strokeStyle, t.stroke()), i.fillStyle && (t[tf] = i[tf], t.fill())
    }, drawPoint: function (t, i, n) {
      if (n) return void t.moveTo(i.x, i.y);
      if (yU[Vu](i)) {
        var e = i[0], s = i[1];
        t.quadraticCurveTo(e.x, e.y, s.x, s.y)
      } else t[ff](i.x, i.y)
    }, setCurrentPoint: function (t) {
      this[oN] = t
    }, addPoint: function (t) {
      this._k2 || (this._k2 = [], this[fN]()), this._k2.push(t), this[mT]()
    }
  }, m(kr, wr), K(kr[yh], {
    currentPoint: {
      get: function () {
        return this._98
      }, set: function (t) {
        this._98 = t, this[mT]()
      }
    }, prevPoint: {
      get: function () {
        return this._k2 && this._k2[rh] ? this._k2[this._k2[rh] - 1] : null
      }
    }, points: {
      get: function () {
        return this._98 && this._k2 && this._k2.length ? this._k2[fh](this._98) : void 0
      }
    }
  }), yU.DrawPathInteraction = kr, Or[yh] = {
    _hq: function (t, i) {
      return this[cN] && t != this[cN] ? this._ey(t, i) : this[uN](t, i)
    }, _nah: function (t, i) {
      return t instanceof Tq && i[WL](t)
    }, _ey: function (t, i) {
      return t instanceof Tq && i[qL](t, this[cN])
    }, _9p: function (t, i, n, e) {
      if (this[_N]) return this[_N][dN](n, e);
      var s = i[A_](t);
      return s && s.bodyBounds ? s[dL][Vb] : t[IM]
    }, _nax: function (t) {
      this._kl && (clearTimeout(this._kl), delete this._kl), this._kl = setTimeout(function (t) {
        if (delete this._kl, this[cN] && this.currentPoint) {
          var i = t.x - this.currentPoint.x, n = t.y - this[oN].y;
          Math[To](i * i + n * n) * this[S_][ef] <= 2 && this[to](this.currentPoint)
        }
      }[el](this, this.toLogicalPoint(t)), eY[lN])
    }, destroy: function () {
      T(this, Or, eg, arguments), this.start = null, this[vN] = null, this._kl && (clearTimeout(this._kl), delete this._kl), this[bN] = null, this[_N] = null
    }, ondblclick: function (t, i) {
      this[eg](i)
    }, finish: function (t, i, n, e) {
      var s;
      this._k2 && this._k2[rh] >= 2 && (this._k2[Rm](), s = new sY, l(this._k2, function (t) {
        if (yU[Vu](t)) {
          var i = t[0], n = t[1];
          s.add(new QU(gU[yN], [i.x, i.y, n.x, n.y]))
        } else s.add(new QU(gU[gN], [t.x, t.y]))
      }, this)), i[xN](this.start, n, t, s, this.startPort, e), this[eg](i)
    }, onstart: function (t, i) {
      if (this.start) {
        var n = t.getData(), e = i[QT](t);
        return this._ey(n, i) ? void this.finish(t, i, n, new Sr(i.getUI(n))[dN](e.x, e.y)[PR]) : void this[to](e)
      }
    }, startdrag: function (t, i) {
      if (!this[cN] && !t.responded) {
        var n = t.getData();
        if (n && this._nah(n, i)) {
          t[pN] = !0, this[cN] = n;
          var e = i[QT](t), s = this._9p(n, i, e.x, e.y);
          this[vN] = s[PR], this[to](s)
        }
      }
    }, ondrag: function (t, i) {
      this.onmousemove(t, i)
    }, enddrag: function (t, i) {
      if (this.start) {
        var n = t[Uy]();
        if (this._ey(n, i)) {
          var e = i[QT](t);
          this[EN](t, i, n, new Sr(i[A_](n))[dN](e.x, e.y).port)
        }
      }
    }, onrelease: function (t, i) {
      XY(t) && this.destroy(i)
    }, _naf: null, onmousemove: function (t, i) {
      var n = i[QT](t), e = Ir(i, n.x, n.y, function (t) {
        return this._hq(t, i)
      }.bind(this));
      e != this._naf && (this[bN] = e, this[_N] = e ? new Sr(i[A_](e)) : null, e ? (this[fN](), this[iN] = [this[_N]]) : this.drawableList = null, this.invalidate());
      var s;
      this[_N] && (s = this[_N][dN](n.x, n.y), this.invalidate()), this.start && (this.currentPoint = s || n, XY(t) && this._nax(t, i))
    }, toLogicalPoint: function (t) {
      return this[S_][QT](t)
    }, getDefaultDrawStyles: function () {
      return {
        lineWidth: this[S_].getDefaultStyle(Aq[Gj]),
        strokeStyle: this.graph[KL](Aq[QS]),
        lineDash: this.graph.getDefaultStyle(Aq[Wj]),
        lineDashOffset: this[S_][KL](Aq[qj]),
        lineCap: this[S_].getDefaultStyle(Aq.LINE_CAP),
        lineJoin: this[S_][KL](Aq.LINE_JOIN)
      }
    }
  }, m(Or, kr), yU.CreateEdgeInteraction = Or, Sr[yh][mN] = function (t, i, n) {
    var e = 0;
    t[rf](), t.rect(n.x - e, n.y - e, n[ha] + 2 * e, n[Ja] + 2 * e), t[No] = 2 / i, t[Ko] = wL, t[Lo]()
  }, Sr[yh][wN] = function (t, i, n) {
    var e = 4;
    t.beginPath();
    var s = e / i;
    t.moveTo(n.x + s, n.y), t.arc(n.x, n.y, s, 0, 2 * Math.PI, !1), t.lineWidth = 1 / i, n[TN] ? (t.strokeStyle = kN, t[tf] = MN) : (t.strokeStyle = wL, t[tf] = "rgba(255, 255, 255, 0.8)"), t[Do](), t.stroke()
  }, Sr[yh].draw = function (t, i) {
    var n = this.rect;
    this.drawRect(t, i, n), this[$a] && this[$a][Dc](function (n) {
      this[wN](t, i, n)
    }[el](this))
  }, Sr[yh][dN] = function (t, i, n) {
    if (this.points) {
      n = n || 20, n *= n;
      var e, s = n;
      return this[$a][Dc](function (r) {
        r.marked && (r[TN] = !1);
        var h = r.x - t, a = r.y - i, o = h * h + a * a;
        n > o && s > o && (s = o, e = r)
      }), e && (e[TN] = !0), e || this[Ld]
    }
    return this.defaultPoint
  }, Ar[yh] = {
    getDefaultDrawStyles: function () {
      return {
        lineWidth: this[S_][KL](Aq.SHAPE_STROKE),
        strokeStyle: this.graph[KL](Aq[QO]),
        fillStyle: this[S_][KL](Aq.SHAPE_FILL_COLOR)
      }
    }, finish: function (t, i) {
      if (this._k2 && this._k2[rh]) {
        var n = this._k2, e = 0, s = 0, r = 0;
        n[Dc](function (t) {
          return yU.isArray(t) ? void t.forEach(function () {
            e += t.x, s += t.y, r++
          }) : (e += t.x, s += t.y, void r++)
        }), e /= r, s /= r;
        var h = [];
        n[Dc](function (t, i) {
          if (0 == i) return void h[Io](new QU(gU[qp], [t.x - e, t.y - s]));
          if (yU[Vu](t)) {
            var n = t[0], r = t[1];
            h.push(new QU(gU.SEGMENT_QUAD_TO, [n.x - e, n.y - s, r.x - e, r.y - s]))
          } else h[Io](new QU(gU[gN], [t.x - e, t.y - s]))
        }), this[oo](t, h, e, s), this[eg](i)
      }
    }, startdrag: function (t) {
      t.responded = !0
    }, createElement: function (t, i, n, e) {
      return this.graph[ON](t, i, n, e)
    }, onstart: function (t, i) {
      var n = i[QT](t);
      this._d9 = n, this.addPoint(n)
    }, onmousemove: function (t, i) {
      this._d9 && (this[oN] = i[QT](t))
    }, ondblclick: function (t, i) {
      if (this._d9) {
        if (this._k2.length < 3) return void this.destroy(i);
        delete this._k2[this._k2.length - 1], this[EN](t, i)
      }
    }, isClosePath: !0
  }, m(Ar, kr), yU[IN] = Ar, jr.prototype = {
    isClosePath: !1, createElement: function (t, i, n, e) {
      return this.graph.createLineByInteraction(t, i, n, e)
    }, getDefaultDrawStyles: function () {
      return {
        lineWidth: Uq[Aq[KO]],
        strokeStyle: Uq[Aq[QO]],
        lineDash: this[S_].getDefaultStyle(Aq[Pj]),
        lineDashOffset: this.graph[KL](Aq.SHAPE_LINE_DASH_OFFSET),
        lineCap: this[S_].getDefaultStyle(Aq[Cj]),
        lineJoin: this[S_][KL](Aq.LINE_JOIN)
      }
    }
  }, m(jr, Ar), yU[SN] = jr, Pr.prototype = {
    destroy: function (t) {
      T(this, Pr, eg, arguments), t.cursor = "", this[cN] = null
    }, doDraw: function (t) {
      if (this.start && this[oN]) {
        var i, n;
        this.graph.interactionProperties && (i = this[S_][CD][Bk], n = this[S_][CD][j_]), i = i || this[S_][PD] || yU[AN], n = n || this[S_][j_];
        var e = i[jN] || yU.EdgeUI.drawReferenceLine, s = this[S_][A_](this[cN]);
        s && s[dL] && (s = s[dL][Vb], t[rf](), e(t, s, this[oN], n), this.styleDraw(t))
      }
    }, canLinkFrom: function (t, i) {
      return t instanceof Tq && i[WL](t)
    }, canLinkTo: function (t, i) {
      return t instanceof Tq && i[qL](t, this[cN])
    }, startdrag: function (t, i) {
      var n = t[Uy]();
      this[WL](n, i) && (t[pN] = !0, this[cN] = n, i[zD] = xg, this.addDrawable())
    }, ondrag: function (t, i) {
      this.start && (yU[SL](t), this[oN] = i[QT](t), this[mT]())
    }, enddrag: function (t, i) {
      if (this.start) {
        this.invalidate();
        var n = t.getData();
        this.canLinkTo(n, i) && i[xN](this.start, n, t), this.destroy(i)
      }
    }, getDefaultDrawStyles: function () {
      return {
        lineWidth: this[S_][KL](Aq[Gj]),
        strokeStyle: this[S_].getDefaultStyle(Aq[QS]),
        lineDash: this[S_][KL](Aq[Wj]),
        lineDashOffset: this.graph[KL](Aq[qj]),
        lineCap: this[S_][KL](Aq[Cj]),
        lineJoin: this[S_][KL](Aq.LINE_JOIN)
      }
    }
  }, m(Pr, kr), yU[PN] = Pr, eY[CN] = !1, Br[yh] = {
    html: null, createHTML: function () {
      var t = i[oo](LN);
      t[eu] = DN, t[va].position = cT, t[va][bo] = Vb, t[va][Jg] = RN, t.style.padding = NN, t[va].boxShadow = "0px 0px 10px rgba(40, 85, 184, 0.75)", t.style[Kw] = ed, t[va][nT] = nd;
      var n = this;
      return t[BN] = function (t) {
        n[$N](t)
      }, t[FN] = function (t) {
        return 27 == t[vw] ? void n[GN]() : void 0
      }, t.onkeypress = function (i) {
        if (13 == i.keyCode || 10 == i[vw]) {
          if (i[Nh](), i[lw] || i[Ba] || i[dw]) return Rr(t, xo), void n.onValueChange(i);
          n.stopEdit()
        }
      }, i[gp][nu](t), t
    }, setText: function (t, i) {
      this.html[Xu] = t || "", i && (this.html[va][$d] = i), Nr(this[GD]), this[zN](this[GD])
    }, onSizeChange: function (t) {
      var i = (t[Rd], t[Nd], Dr(t));
      return t.style[ha] = i[ha] + 30 + no, t.style[Ja] = i[Ja] + 10 + no, i
    }, onValueChange: function (t) {
      var i = t[Zu];
      this.onSizeChange(i), i.style[Ca] = i.x - i.offsetWidth / 2 + no
    }, onClickOnWindow: function (t) {
      t[Zu] != this[GD] && (eY[CN] ? this[HN]() : this[GN]())
    }, startEdit: function (i, n, e, s, r) {
      this.html || (this[GD] = this[YN]()), this[UN] || (this[UN] = function (t) {
        this[WN](t)
      }[el](this)), t[wb](uy, this[UN], !0), this.callback = r, this.html.x = i, this[GD].y = n, this.html[va][Kw] = rR, Lr(this[GD], i, n), this[qN](e, s || 10), Lr(this[GD], i, n)
    }, isEditing: function () {
      return ed != this.html[va][Kw]
    }, cancelEdit: function () {
      this[HN](!0)
    }, stopEdit: function (i) {
      if (this[XN]()) {
        t.removeEventListener(uy, this[UN]);
        var n = this.html[Xu];
        !i && this.callback && this[Qu](n), this[GD][va].display = ed, this[GD][Xu] = null, this[Qu] = null
      }
    }, destroy: function () {
      this.html && i.body.removeChild(this[GD])
    }
  }, yU[VN] = Br;
  var tX = function (t) {
    this[S_] = t
  };
  tX.prototype = {
    destroy: function (t) {
      t[ZN] && (t[ZN][eg](), delete t[ZN])
    }, ondblclick: function (t, i) {
      var n = t.getData();
      if (n) {
        if (n[KN] !== !1) {
          if (i[rL] && i[JN](n)) {
            var e = i[$_](t);
            if (e instanceof Hq && e[rL] !== !1) {
              var s = i[ZN];
              s || (i[ZN] = s = new Br);
              var r = e[vf]();
              return r = i[Dk](r.x + r[ha] / 2, r.y + r[Ja] / 2), r = Cr(r.x, r.y, i.html), void i.startLabelEdit(n, e, s, r)
            }
          }
          var h = n instanceof Oq, a = n instanceof wq && n[QN]();
          return n._4a && (Ti(t) || !h && !a) ? void (i[zM] = n) : h ? (n[Fk] = !n.expanded, void this[S_].onInteractionEvent(new qr(this[S_], qr[VM], t, n))) : void (a && this[S_][pD](n) && this[S_].onInteractionEvent(new qr(this[S_], qr[tB], t, n)))
        }
      } else {
        if (i[zM]) return void i[iB]();
        if (i[nB]) {
          var o = i[eB] || 1;
          Math.abs(i[ef] - o) < 1e-4 ? i[oD]() : i[sB](o)
        }
      }
    }
  };
  var iX = function (t) {
    this.graph = t
  };
  iX[yh] = {
    onkeydown: function (t, i) {
      if (i[rL]) {
        var n = t.keyCode;
        if (8 == n || 46 == n || 127 == n) return i[rB](t), void F(t);
        if (Ti(t)) {
          if (67 == n) ; else if (86 == n) ; else if (90 == n) ; else if (89 != n) return;
          F(t)
        }
      }
    }
  }, yU.EditInteraction = iX;
  var nX = function (t) {
    this[S_] = t
  };
  nX.prototype = {
    onkeydown: function (i, n) {
      if (i[hB] && 83 == i.keyCode) {
        var e = n[aB](n[ef], n.viewportBounds), s = t.open(), r = s.document;
        r.title = oB + e.width + fB + e[Ja];
        var h = r[oo](X_);
        h.src = e[Ro], r[gp][nu](h), F(i)
      }
    }
  };
  var eX = function (t) {
    this[S_] = t
  };
  eX.prototype = {
    destroy: function () {
      delete this[cB], delete this[uB]
    }, _23: function (t) {
      var i = new sY;
      return t[wd][Dc](function (n) {
        t.isMovable(n) && t[_B](n) && i.add(n)
      }, this), i
    }, onstart: function (t, i) {
      this.currentDraggingElement && this[eg](i)
    }, startdrag: function (t, i) {
      if (!(t[pN] || t.touches && 1 != t[Ma].length)) {
        var n = t[Uy]();
        if (!n || !i.isSelected(n) || !i[gD](n)) return void this[eg](i);
        t.responded = !0, this.currentDraggingElement = n, this[cB] = this._23(i);
        var e = new qr(i, qr[dB], t, this[uB], this[cB][al]);
        return i.beforeInteractionEvent(e) === !1 ? void this[eg](i) : void i.onInteractionEvent(e)
      }
    }, ondrag: function (t, i) {
      if (this[uB]) {
        if (t[Ma] && 1 != t[Ma].length) return void this[Hy](t, i);
        z(t);
        var n = t.dx, e = t.dy, s = i.scale;
        n /= s, e /= s;
        var r = new qr(i, qr.ELEMENT_MOVING, t, this.currentDraggingElement, this[cB].datas);
        i[lB](this[cB].datas, n, e), i[GL](r)
      }
    }, enddrag: function (t, i) {
      if (this[uB]) {
        if (this[cB] && this.draggingElements[rh]) {
          if (t[dw]) {
            var n, e = i[QT](t), s = e.x, r = e.y;
            i[Ad](function (t) {
              var i = t[Ro];
              if (!this[cB].contains(i) && t[jd].intersectsPoint(s - t.x, r - t.y) && t[$_](s, r, 1)) {
                if (i instanceof yU.Edge) {
                  if (!i[$k]) return;
                  for (var e = this.draggingElements[rh]; e-- > 0;) {
                    var h = this[cB].get(e);
                    if (h instanceof yU[PL] && h[vB](i)) return
                  }
                  return n = i, !1
                }
                return (i[$k] || i._hi() && i[Fk]) && (n = i), !1
              }
            }, this), n && this[cB][Dc](function (t) {
              for (var i = t[su]; i;) {
                if (this[cB][B_](i)) return;
                i = i.parent
              }
              t[su] = n
            }, this)
          }
          var h = new qr(i, qr[bB], t, this.currentDraggingElement, this.draggingElements[al]);
          i.onInteractionEvent(h)
        }
        this[eg](i)
      }
    }, onpinch: function (t, i) {
      this[uB] && this[Hy](t, i)
    }, step: 1, onkeydown: function (t, i) {
      if (Ti(t)) {
        var n, e;
        if (37 == t.keyCode ? n = -1 : 39 == t[vw] ? n = 1 : 38 == t[vw] ? e = -1 : 40 == t[vw] && (e = 1), n || e) {
          var s = this._23(i).datas;
          if (0 != s[rh]) {
            F(t), n = n || 0, e = e || 0;
            var r = this[yB] / i[ef], h = new qr(i, qr[bB], t, null, s);
            i.moveElements(s, n * r, e * r), i.onInteractionEvent(h)
          }
        }
      }
    }
  };
  var sX = function (t) {
    this[S_] = t
  };
  sX[yh] = {
    onkeydown: function (t, i) {
      Ti(t) || (37 == t.keyCode ? (this._54(i, 1, 0), F(t)) : 39 == t[vw] ? (this._54(i, -1, 0), F(t)) : 38 == t[vw] ? (this._54(i, 0, 1), F(t)) : 40 == t[vw] && (this._54(i, 0, -1), F(t)))
    }, _54: function (t, i, n) {
      t._8v(i, n)
    }, onstart: function (t, i) {
      this._kn && this.destroy(i)
    }, _kn: !1, startdrag: function (t, i) {
      if (!t[pN]) {
        t[pN] = !0, this._kn = !0, i[zD] = aU;
        var n = new qr(i, qr[gB], t);
        i[GL](n)
      }
    }, ondrag: function (t, i) {
      this._kn && i.translate(t.dx || 0, t.dy || 0)
    }, enddrag: function (t, i) {
      if (this._kn) {
        if (i[AR] !== !1) {
          var n = t.vx, e = t.vy;
          (Math.abs(n) > .1 || Math.abs(e) > .1) && i._8v(n, e)
        }
        this[eg](i);
        var s = new qr(i, qr[xB], t);
        i[GL](s)
      }
    }, startpinch: function (t, i) {
      i[qD](!0)
    }, onpinch: function (t, i) {
      this._kn = !0;
      var n = t.dScale;
      if (n) {
        var e = i[pB](t.center);
        i.zoomAt(n, e.x, e.y, !1)
      }
    }, endpinch: function (t, i) {
      i[qD](!1)
    }, destroy: function (t) {
      this._kn = !1, t.cursor = null
    }
  }, $r[yh] = {
    onDataPropertyChange: function () {
      this[S_][aD](function () {
        this._k4()
      }, this)
    }, destroy: function () {
      this.graph[zD] = null, this[JR] && delete this[JR][EB], this[mB] = !1, delete this._8s, delete this._98, delete this[wB], T(this, $r, eg, arguments)
    }, _8s: null, _5o: function (t) {
      this._8s = t, this[mT]()
    }, isEndPointEditable: function (t, i) {
      return this.graph[TB](t, i)
    }, drawPoint: function (t, i, n) {
      (!i[kB] || this[TB](this[JR], i.isFrom)) && (t[rf](), i[MB] ? t.rect(i.x - this[Kd] / n, i.y - this.handlerSize / n, this.handlerSize / n * 2, this[Kd] / n * 2) : t.arc(i.x, i.y, this.handlerSize / n, 0, 2 * Math.PI, !1), t.lineWidth = 1 / n, t.lineDash = [], t.strokeStyle = vd, t[tf] = "rgba(255, 255, 0, 0.8)", t[Lo](), t.fill())
    }, _g0: function (t, i, n, e) {
      e ? t.moveTo(i, n) : t.lineTo(i, n)
    }, doDraw: function (t, i) {
      this[OB](t, i)
    }, drawLine: function (t, i) {
      if (this._8s && this._8s[rh]) {
        var n = this._8s;
        t[vo]();
        var e = this[JR] instanceof kq;
        e && (t[sf](this[JR].x, this.element.y), this[JR].rotate && t[Go](this.element[Go]));
        var s, r = [];
        t.beginPath();
        var h = n[rh];
        n[Dc](function (i, n) {
          if (i[Fo] != gU[Vp]) for (var e = !n || n == h - 1, a = 0, o = i.points; a + 1 < o.length;) {
            var f = o[a], c = o[a + 1], u = {x: f, y: c, isControlPoint: this._6k(i, a)};
            e && (u[kB] = !0, u[IB] = 0 == n), r[Io](u), this._g0(t, u.x, u.y, null == s), s = u, a += 2
          }
        }, this), t[No] = 1 / i, t[Ec] = [2 / i, 3 / i], t[Ko] = SB, t[Lo](), r[Dc](function (n, e) {
          this[wN](t, n, i, e)
        }, this), t[Eo]()
      }
    }, invalidate: function () {
      this.topCanvas.invalidate()
    }, _31: function (t) {
      if (this.element != t && (this[JR] && this[eg](), t && this.isEditable(t))) {
        var i = this._5l(t, this.graph);
        i && (this[AB](t, !0), t[EB] = !0, this._n9anEdit = !0, this._5o(i), this[fN]())
      }
    }, _k4: function () {
      if (this[jB]() && this[JR]) {
        var t = this._5l(this[JR], this[S_]);
        return t ? void this._5o(t) : void this[eg](this.graph)
      }
    }, _5l: function (t, i) {
      if (i[JN](t)) {
        var n = t[uL] || [];
        n instanceof sY && (n = n[Nl]());
        var e = i.getUI(t);
        if (e instanceof yU.EdgeUI) {
          var s = e[P_](i[A_](t[ou])), r = e[P_](i[A_](t[ru])), h = s[Vb], a = r[Vb], o = e[s_](Aq[M_]),
            f = e[s_](Aq[I_]);
          o && (h.x += o.x || 0, h.y += o.y || 0), f && (a.x += f.x || 0, a.y += f.y || 0), n[oh](0, 0, new yU[Kp](gU[qp], [h.x, h.y])), n[Io](new yU[Kp](gU[qp], [a.x, a.y]))
        }
        return n
      }
    }, _ho: function (t, i) {
      t -= this[JR].x, i -= this[JR].y;
      var n = {x: t, y: i};
      return this.element[Go] && Zs(n, -this[JR].rotate), n
    }, isPointAddable: function () {
      return !0
    }, isPointRemovable: function () {
      return !0
    }, onclick: function (t, i) {
      if (i[rL] && t.altKey && this.element) {
        var n = this._fy(t, i);
        if (n && n[MB] && this[PB](this.element, n, i) !== !1) {
          if (n[Mf] >= 0) {
            this[JR][CB](n.index);
            var e = new qr(i, qr.POINT_REMOVE, t, this.element);
            e.point = n, i[GL](e)
          }
        } else if (this[JR] == t[Uy]() && this[LB](this.element, i) !== !1) {
          var s = i[QT](t), r = i[A_](this.element);
          if (r.addPoint(s.x, s.y, this.handlerSize || 2) !== !1) {
            var e = new qr(i, qr[DB], t, this[JR]);
            e[RB] = s, i.onInteractionEvent(e)
          }
        }
      }
    }, isEditable: function (t) {
      return this[S_].isEditable(t) && (t instanceof kq || t instanceof wq && (!t[_u]() || t.hasPathSegments()))
    }, ondblclick: function (t, i) {
      if (!i[rL]) return void (this[JR] && this[eg](i));
      var n = t.getData();
      return !n || n == this[JR] || n[EB] ? void this[eg](i) : void this._31(n)
    }, onstart: function (t, i) {
      if (this._mousePressed = !0, !i[rL]) return void (this.element && this[eg](i));
      if (!t[pN]) {
        if (this[JR] && this._fy(t, i)) return void (t[pN] = !0);
        var n = t.getData();
        return n && i.isResizable(n) && n instanceof kq ? void (this.element && n != this[JR] && this.destroy()) : void this._31(n)
      }
    }, onrelease: function () {
      this._mousePressed = !1, this[JR] && (this[wB] = !0)
    }, _98: null, _fy: function (t, i) {
      var n = i[QT](t);
      this[JR] instanceof kq && (n = this._ho(n.x, n.y));
      var e, s = i[ef], r = this[Kd] / s, h = this._8s, a = h.length, o = this.element instanceof yU[jD];
      return h.forEach(function (t, s) {
        for (var f = 0, c = t[$a]; f + 1 < c[rh];) {
          var u = c[f], _ = c[f + 1], d = oY(n.x, n.y, u, _);
          if (r > d) {
            if (e = {
              oldPoints: c[ch](0),
              segment: t,
              index: s,
              pointIndex: f
            }, o && (e[Mf] -= 1), o && !Fr(t) && (0 == s || s == h.length - 1)) {
              e[kB] = !0;
              var l = 0 == s;
              e[IB] = l;
              var v = l ? yU.Styles.EDGE_FROM_OFFSET : yU.Styles[I_], b = i[s_](this[JR], v) || {};
              e[NB] = [b.x || 0, b.y || 0]
            }
            return this._6k(t, f) && (e.isControlPoint = !0, s > 0 && (e[BB] = h instanceof sY ? h.getByIndex(s - 1) : h[s - 1]), a > s + 1 && (e.nextSegment = h instanceof sY ? h[Pl](s + 1) : h[s + 1], e[$B].points && (e[FB] = e.nextSegment[$a][ch](0)))), !1
          }
          f += 2
        }
      }, this), e && e.isEndPoint && !this[TB](this[JR], e[IB]) ? void 0 : e
    }, _6k: function (t, i) {
      return i == t.points[rh] - 2
    }, startdrag: function (t, i) {
      if (this[JR] && this._n9anEdit && (this._98 = this._fy(t, i), this._98)) {
        this[tN](), t.responded = !0;
        var n = new qr(i, qr[GB], t, this[JR]);
        n.point = this._98, i.onInteractionEvent(n)
      }
    }, onkeyup: function (t, i) {
      this._mousePressed && 16 != !t[vw] && this[JR] && this._98 && this._98[Ey] && this[zB](this._98.delta.x, this._98[Ey].y, i, t, !1)
    }, onkeydown: function (t, i) {
      T(this, $r, FN, arguments), this[mB] && this[JR] && this._98 && t[dw] && this._98[Ey] && this[zB](this._98[Ey].x, this._98[Ey].y, i, t, !0)
    }, _n95: function (t, i, n, e, s) {
      var r = this._98, h = this[JR], a = r.oldPoints, o = r[HB];
      if (r.isEndPoint) {
        var f = r[IB] ? yU[SD].EDGE_FROM_OFFSET : yU[SD].EDGE_TO_OFFSET, c = {x: a[0] + t, y: a[1] + i};
        h[e_](f, c);
        var u = new qr(n, qr.POINT_MOVING, e, h);
        return u[RB] = r, void n[GL](u)
      }
      if (s && r[MB]) {
        var _ = {x: a[a.length - 2] + t, y: a[a[rh] - 1] + i}, d = r[BB], l = r[$B], v = 20 / n.scale, b = Number[Vl],
          y = b, g = b, x = b;
        d && (d = d[$o], b = Math.abs(_.x - d.x), g = Math.abs(_.y - d.y)), l && (l = l.lastPoint, y = Math.abs(_.x - l.x), x = Math.abs(_.y - l.y)), v > b && y > b ? t += d.x - _.x : v > y && b > y && (t += l.x - _.x), v > g && x > g ? i += d.y - _.y : v > x && g > x && (i += l.y - _.y)
      }
      if (r.isControlPoint && Fr(o)) {
        for (var p = o.points.length - 4; p < o[$a][rh];) {
          var E = a[p] + t, m = a[p + 1] + i;
          o.points[p] = E, o.points[p + 1] = m, p += 2
        }
        if (r[$B] && Fr(r[$B])) {
          var w = r.oldNextPoints, E = w[0] + t, m = w[1] + i;
          r[$B][$a][0] = E, r.nextSegment[$a][1] = m
        }
      } else {
        var p = r[YB], E = a[p] + t, m = a[p + 1] + i;
        o[$a][p] = E, o[$a][p + 1] = m
      }
      h.firePathChange();
      var u = new qr(n, qr[UB], e, h);
      u[RB] = r, n[GL](u)
    }, ondrag: function (t, i) {
      if (this[JR] && this._98) {
        var n = this._98, e = this[JR], s = t[fg], r = t[WB], h = i[ef];
        if (s /= h, r /= h, e[Go]) {
          var a = {x: s, y: r};
          Zs(a, -e[Go]), s = a.x, r = a.y
        }
        n[Ey] = {x: s, y: r}, this[zB](s, r, i, t, t[dw])
      }
    }, enddrag: function (t, i) {
      if (this[JR] && this._98) {
        this[fN](), this._k4();
        var n = new qr(i, qr.POINT_MOVE_END, t, this.element);
        n.point = this._98, i.onInteractionEvent(n)
      }
    }, onmousemove: function (t, i) {
      this[JR] && (i[zD] = t.altKey && (this._fy(t, i) || this[JR] == t.getData()) ? "crosshair" : null)
    }
  }, m($r, wr), eY.SELECTION_RECTANGLE_STROKE = 1, eY[qB] = V(3724541951), eY[XB] = V(1430753245), gU[VB] = ZB, gU.RECTANGLE_SELECTION_MODE_CONTAIN = KB, eY.RECTANGLE_SELECTION_MODE = gU[VB];
  var rX = function (t) {
    this[S_] = t, this.topCanvas = t[Md]
  };
  rX[yh] = {
    onstart: function (t, i) {
      this._kn && this.destroy(i)
    }, startdrag: function (t, i) {
      t.responded || (t.responded = !0, this._kn = i.toLogical(t), i[zD] = xg, this._10Id = this[Md][fN](this._10, this).id)
    }, ondrag: function (t, i) {
      if (this._kn) {
        z(t), this[JB] = i[QT](t), this[mT]();
        var n = new qr(i, qr.SELECT_START, t, i[wd]);
        i.onInteractionEvent(n)
      }
    }, enddrag: function (t, i) {
      if (this._kn) {
        this[QB] && (clearTimeout(this._f4Timer), this[QB] = null), this._f4(!0), this[eg](i);
        var n = new qr(i, qr[t$], t, i[wd]);
        i.onInteractionEvent(n)
      }
    }, onpinch: function (t, i) {
      this._kn && this[Hy](t, i)
    }, _10: function (t, i) {
      t[Ko] = eY[qB], t[tf] = eY[XB], t.lineWidth = eY.SELECTION_RECTANGLE_STROKE / i;
      var n = this._kn.x, e = this._kn.y;
      t[hf](n, e, this[JB].x - n, this[JB].y - e), t.fill(), t[Lo]()
    }, invalidate: function () {
      return this.invalidateFlag ? void this[Md][mT]() : (this[qM] = !0, void (this[QB] = setTimeout(this._f4[el](this), 100)))
    }, _f4: function (t) {
      if (this[qM] = !1, this[QB] = null, !this._kn || UH && !t) return void this[Md].invalidate();
      var i = Math.min(this._kn.x, this[JB].x), n = Math.min(this._kn.y, this[JB].y),
        e = Math.abs(this._kn.x - this[JB].x), s = Math.abs(this._kn.y - this[JB].y);
      if (!e || !s) return void this[S_].selectionModel[Qa]();
      var r = [], h = this[S_][ef], a = this[S_][i$];
      if (this[S_][n$](function (t) {
        t._hg && this[S_][Td](t[yc]) && this[e$](i, n, e, s, t, h, a) && r[Io](t.data)
      }[el](this)), this[S_][wd].set(r), this.topCanvas[mT](), !t) {
        var o = new qr(this[S_], qr[s$], null, this[S_].selectionModel);
        this[S_][GL](o)
      }
    }, inRectangle: function (t, i, n, e, s, r, h) {
      var a = s[vf]();
      return ai(t, i, n, e, a.x, a.y, a[ha], a[Ja]) ? !0 : (h = h || eY[r$], h == gU[h$] ? !1 : Gn(t, i, n, e, s, r))
    }, destroy: function (t) {
      this._kn = null, t[zD] = null, this[a$] && (this[Md][tN](this[a$]), delete this[a$], this.topCanvas[mT]())
    }
  };
  var hX = I({
    "super": rX, onstart: null, startdrag: null, ondrag: null, enddrag: null, accept: function (t, i, n) {
      return n.enableRectangleSelectionByRightButton !== !1
    }, oncontextmenu: function (t, i) {
      i[o$] || z(t)
    }, onstart2: function () {
      rX[yh][Py].apply(this, arguments)
    }, startdrag2: function (t, i) {
      t[pN] || (i[o$] && i[o$][f$] instanceof Function && i[o$][f$](), rX[yh][Ny].apply(this, arguments))
    }, ondrag2: function () {
      rX[yh][Fy][bh](this, arguments)
    }, enddrag2: function () {
      rX[yh][Hy][bh](this, arguments)
    }
  }), Qq = Math.PI / 4;
  Gr[yh] = {
    _dv: !1, _dx: !1, onDataPropertyChange: function () {
      this[S_].callLater(function () {
        this._9k()
      }, this)
    }, ondblclick: function (t, i) {
      this[JR] && this[eg](i)
    }, destroy: function (t) {
      t.cursor = null, delete this._n9j, delete this._naody, delete this._98, delete this[wB], delete this._k2, delete this[c$], delete this._dx, delete this._dv, delete this[u$], T(this, Gr, eg, arguments)
    }, _n9j: null, _k2: null, _89: function (t) {
      this[KP] = t;
      var i = this[KP].x, n = this[KP].y, e = this[KP][ha], s = this._n9j.height;
      if (this[JR] instanceof Oq && this[JR].expanded, this._dx) {
        var r = [];
        r.push({x: i, y: n, p: dY[rv], cursor: _$, rotate: 5 * Qq}), r[Io]({
          x: i + e / 2,
          y: n,
          p: dY[av],
          cursor: d$,
          rotate: 6 * Qq
        }), r[Io]({x: i + e, y: n, p: dY.RIGHT_TOP, cursor: Jd, rotate: 7 * Qq}), r.push({
          x: i,
          y: n + s / 2,
          p: dY[hv],
          cursor: l$,
          rotate: 4 * Qq
        }), r[Io]({x: i, y: n + s, p: dY[uv], cursor: Jd, rotate: 3 * Qq}), r[Io]({
          x: i + e,
          y: n + s / 2,
          p: dY.RIGHT_MIDDLE,
          cursor: l$,
          rotate: 0
        }), r[Io]({x: i + e / 2, y: n + s, p: dY[fv], cursor: d$, rotate: 2 * Qq}), r[Io]({
          x: i + e,
          y: n + s,
          p: dY[dv],
          cursor: _$,
          rotate: Qq
        }), this._k2 = r
      }
      this._rotatePoint = this._dv ? {x: i + e / 2, y: n, cursor: oU} : null, this.invalidate()
    }, _e2: function (t, i, n, e) {
      t.beginPath();
      var s = (this[Kd] - 1) / e;
      t[hf](i - s, n - s, 2 * s, 2 * s), t[No] = 1 / e, t[Ec] = [], t[Ko] = vd, t.fillStyle = "rgba(255, 255, 255, 0.8)", t[Lo](), t.fill()
    }, _59: function (t, i, n, e, s, r) {
      s = s || this.handlerSize, r = r || v$, t[rf](), s /= e, t.arc(i, n, s, 0, 2 * Math.PI, !1), t[No] = 1 / e, t[Ec] = [], t[Ko] = vd, t[tf] = r, t[Lo](), t[Do]()
    }, _ho: function (t, i) {
      t -= this[JR].x, i -= this[JR].y;
      var n = {x: t, y: i};
      return this[JR][Go] && Zs(n, -this.element.rotate), n
    }, doDraw: function (t, i) {
      if (this[KP]) {
        if (t[vo](), t.translate(this.element.x, this[JR].y), this[JR].rotate && t.rotate(this[JR][Go]), this[c$]) {
          this._59(t, 0, 0, i, 3, b$);
          var n = this[c$].x, e = this[c$].y - this[y$] / i;
          t.beginPath(), t[of](n, this[c$].y), t.lineTo(n, e), t[No] = 1 / i, t[Ko] = SB, t.stroke(), this._59(t, n, e, i)
        }
        if (this._k2) {
          var s = this[KP].x, r = this[KP].y, h = this._n9j[ha], a = this._n9j[Ja];
          t.beginPath(), t[hf](s, r, h, a), t[No] = 1 / i, t.lineDash = [2 / i, 3 / i], t[Ko] = SB, t.stroke(), l(this._k2, function (n) {
            this._e2(t, n.x, n.y, i)
          }, this)
        }
        t[Eo]()
      }
    }, _9k: function () {
      if (this[jB]() && this[JR]) {
        var t = this[S_].getUI(this[JR]);
        this[JP] = t.body;
        var i = zr(this[JP], this._naody._jk), n = zr(this._naody, this[JP]._85);
        this._insets = new _Y(i.y - n.y, i.x - n.x, n.bottom - i[ea], n.right - i[na]), this._89(n)
      }
    }, _na0: function (t, i) {
      return i[g$](t)
    }, _ncv: function (t, i) {
      return !(t instanceof Oq && t[Fk] || !i[x$](t))
    }, _n9m: function (t, i) {
      return t instanceof Tq && i[JN](t)
    }, onstart: function (t, i) {
      if (!i.editable) return void (this[JR] && this.destroy(i));
      if (!t[pN]) {
        var n = i[A_](t), e = t[Uy]();
        if (e != this.element) {
          if (this.element) {
            if (this._fy(t, i)) return void (t.responded = !0);
            this[eg](i)
          }
          if (e && !e._editting && this[p$](e, i)) {
            var s = this._na0(e, i, n), r = this[E$](e, i, n);
            (s || r) && (this[AB](e, !0), this.addDrawable(), this._dx = s, this._dv = r, this._9k())
          }
        }
      }
    }, onrelease: function (t, i) {
      this[JR] && (this[wB] = !0, this.addDrawable(), i.callLater(function () {
        this._9k()
      }, this))
    }, _98: null, _fy: function (t, i) {
      var n = i[QT](t);
      n = this._ho(n.x, n.y);
      var e = i.scale, s = this[Kd] / e;
      if (this[c$]) {
        var r = this[c$].x, h = this[c$].y - this._rotateHandleLength / e;
        if (oY(n.x, n.y, r, h) < s) return this[c$]
      }
      if (this._k2 && this._k2[rh]) {
        var a;
        return l(this._k2, function (t) {
          return oY(n.x, n.y, t.x, t.y) < s ? (a = t, !1) : void 0
        }, this), a
      }
    }, onmousemove: function (t, i) {
      if (this.element) {
        var n = this._fy(t, i);
        if (!n) return void (i.cursor = null);
        if (n != this._rotatePoint && this[JR].rotate) {
          var e = n[Go] + this[JR][Go];
          return void (i[zD] = Hr(e))
        }
        i[zD] = n[zD]
      }
    }, startdrag: function (t, i) {
      if (this[JR] && (this[tN](), this[wB] && (this._98 = this._fy(t, i), this._98))) {
        if (t[pN] = !0, this._98 == this[c$]) {
          this._98[cN] = i[QT](t), this._98[Go] = this[JR][Go] || 0;
          var n = new qr(i, qr[m$], t, this.element);
          return n.value = this.element[Go], void i[GL](n)
        }
        var n = new qr(i, qr[w$], t, this[JR]);
        n[RB] = this._98, i[GL](n)
      }
    }, _6u: function (t, i, n, e, s, r) {
      var h = this[KP], a = h.x, o = h.y, f = h.width, c = h.height;
      if (r) {
        var u = e != f;
        u ? s = e * c / f : e = s * f / c
      }
      var _ = t[L_][FM], d = e / f, l = s / c, v = -a * d + i, b = -o * l + n;
      _[Dc](function (t) {
        if (t[Fo] != gU[Vp]) {
          var e = t[$a];
          if (e && e[rh]) for (var s = 0, r = e[rh]; r > s; s += 2) {
            var h = e[s], f = e[s + 1];
            e[s] = (h - a) * d + i - v, e[s + 1] = (f - o) * l + n - b
          }
        }
      }), this._n9j.set(i - v, n - b, e, s), t[yD](t.x + v, t.y + b), t[gM]()
    }, _9r: function (t, i, n, e, s) {
      this[KP].set(i, n, e, s), t[KM] = {x: i, y: n, width: e, height: s}
    }, _4k: function (t, i, n, e, s) {
      if (this.element instanceof Oq && this.element.expanded) return this._9r(this[JR], t, i, n, e, s);
      if (this[JR] instanceof kq) return this._6u(this.element, t, i, n, e, s);
      var r = this[JP] instanceof Hq;
      if (!r && s) {
        var h = this._n9j, a = this[JP][T$], o = n != h[ha];
        o ? e = n * a[Ja] / a[ha] : n = e * a.width / a[Ja]
      }
      var f = this.element.anchorPosition, c = new cY(n - this[u$][Ca] - this[u$][na], e - this[u$].top - this[u$][ea]);
      if (c[ha] < 1 && (n = this[u$][Ca] + this._insets.right + 1, c[ha] = 1), c[Ja] < 1 && (e = this._insets.top + this[u$][ea] + 1, c[Ja] = 1), r ? this.element.setStyle(Aq[yS], c) : this[JR][TT] = c, f) {
        var u = ui(f, n, e), _ = u.x + t - (this[JP][uO] || 0), d = u.y + i - (this[JP].offsetY || 0);
        if (this[KP].set(t - _, i - d, n, e), this[JR][Go]) {
          var u = Zs({x: _, y: d}, this.element[Go]);
          _ = u.x, d = u.y
        }
        this[JR].x += _, this[JR].y += d
      } else {
        var _ = this[KP].x * n / this[KP][ha] - t, d = this[KP].y * e / this._n9j[Ja] - i;
        if (this._n9j.set(t + _, i + d, n, e), this[JR][Go]) {
          var u = Zs({x: _, y: d}, this[JR][Go]);
          _ = u.x, d = u.y
        }
        this.element.x -= _, this[JR].y -= d
      }
    }, ondrag: function (t, i) {
      if (this.element && this._98) {
        if (this._98 == this[c$]) {
          var n = i[QT](t), e = yn(n.x, n.y, this[JR].x, this.element.y, this._98[cN].x, this._98[cN].y, !0);
          e += this._98[Go] || 0, t.shiftKey && (e = Math[Bf](e / Math.PI * 4) * Math.PI / 4);
          var s = this[JR].rotate;
          this.element[Go] = e % (2 * Math.PI);
          var r = new qr(i, qr[k$], t, this.element);
          return r.oldValue = s, r[Xu] = this[JR][Go], void i.onInteractionEvent(r)
        }
        var h = t.dx, a = t.dy, o = i.scale;
        if (h /= o, a /= o, this[JR][Go]) {
          var n = {x: h, y: a};
          Zs(n, -this[JR][Go]), h = n.x, a = n.y
        }
        var f = this._98.p, c = this._n9j, u = c.x, _ = c.y, d = c[ha], l = c[Ja];
        f[Jl] == lY ? h >= d ? (u += d, d = h - d || 1) : (u += h, d -= h) : f[Jl] == bY && (-h >= d ? (d = -h - d || 1, u -= d) : d += h), f[Kl] == yY ? a >= l ? (_ += l, l = a - l || 1) : (_ += a, l -= a) : f[Kl] == xY && (-a >= l ? (l = -a - l || 1, _ -= l) : l += a), this._4k(u, _, d, l, t[dw]);
        var r = new qr(i, qr[M$], t, this[JR]);
        r.point = this._98, i[GL](r)
      }
    }, enddrag: function (t, i) {
      if (this[JR] && this._98) {
        if (this._98 == this[c$]) {
          var n = new qr(i, qr.ROTATE_END, t, this[JR]);
          return n[Xu] = this[JR][Go], void i[GL](n)
        }
        var n = new qr(i, qr.RESIZE_END, t, this[JR]);
        n.point = this._98, i.onInteractionEvent(n)
      }
    }
  }, m(Gr, wr), yU[O$] = Gr;
  var aX = function (t) {
    this[S_] = t
  };
  aX[yh] = {
    onstart2: function (t, i) {
      this.onstart(t, i)
    }, onclick: function (t, i) {
      if (!Ti(t)) {
        var n = t.getData();
        if (n && i.isSelectable(n) && (!i.isSelected(n) || 1 != i[wd][rh])) {
          i[I$](n);
          var e = new qr(i, qr.SELECT, t, i[wd]);
          i.onInteractionEvent(e)
        }
      }
    }, onstart: function (t, i) {
      if (!t[pN]) {
        var n = t.getData();
        if (n && !i[Td](n) && (n = null), n && Ti(t)) {
          i[vD](n);
          var e = new qr(i, qr.SELECT, t, i[wd]);
          return void i.onInteractionEvent(e)
        }
        if (!n || !i[wd][S$](n)) {
          n ? (i[I$](n), i[A$](n)) : i.setSelection(null);
          var e = new qr(i, qr[j$], t, i[wd]);
          i[GL](e)
        }
      }
    }, onkeydown: function (t, i) {
      return 27 == t.keyCode ? void i[bD]() : void (Ti(t) && 65 == t[vw] && (i[P$](), F(t)))
    }
  }, eY[C$] = 3e3, eY.TOOLTIP_DELAY = 1e3, Yr[L$] = D$, Yr[R$] = 0, Yr.CURSOR_OFFSET_Y = 15, xi(Kh + Yr.CLASS_NAME, {
    "user-select": ed,
    "background-color": N$,
    overflow: nd,
    "box-shadow": "0 5px 10px rgba(136, 136, 136, 0.5)",
    color: QP,
    "pointer-events": ed,
    border: B$,
    padding: $$,
    display: rR,
    position: cT
  }), Yr.getInstance = function () {
    var t = Yr.instance;
    return t || (t = Yr[F$] = new Yr), t
  }, Yr.show = function (t, i, n) {
    Yr.getInstance()[G$](t, i, n)
  }, Yr[f$] = function () {
    Yr.getInstance().hide()
  }, Yr[yh] = {
    getTooltipElement: function () {
      var t = this._lz;
      return t || (t = i[oo](W_), t.className = Yr[L$], this._lz = t, i[gp].appendChild(t)), t
    }, update: function (t, i) {
      if (this[jB]()) {
        var n = Ud == i;
        t && !n && (t = t[ca](/\n/g, z$));
        var e = this.getTooltipElement();
        n ? e[H$] = t || "" : e[mD] = t || "", Ur(e, this._info.x + Yr[R$], this._info.y + Yr[Y$])
      }
    }, setTooltip: function (t, i) {
      if (!t || !t.content) return this[U$] = null, this._lz && (this._lz[va].display = ed), void (this._9w = Date.now());
      this._9w = null, this[U$] = t;
      var n = this[W$]();
      n[va].display = "", this[q$](this[U$][X$], this[U$][Fo]), isNaN(i) && (i = eY.TOOLTIP_DURATION), i && this.startTimer(this[V$][el](this, !1), i)
    }, _9w: null, _kl: null, stopTimer: function () {
      this._kl && (clearTimeout(this._kl), this._kl = null)
    }, startTimer: function (t, i) {
      this[Z$](), this._kl = setTimeout(function (t) {
        this._kl = null, t()
      }[el](this, t), i)
    }, show: function (t, i, n) {
      return this.isShowing() || this._9w && Date.now() - this._9w < 1e3 ? void this.setTooltip(t, n) : (isNaN(i) && (i = eY[K$]), void (i ? this[J$](this[V$].bind(this, t, n), i) : this[V$](t, n)))
    }, isShowing: function () {
      return this._lz && ed !== this._lz[va][Kw]
    }, hide: function () {
      this.stopTimer(), this.isShowing() && this.setTooltip(!1)
    }
  };
  var oX = function (t) {
    this.graph = t
  };
  oX.prototype = {
    onstart: function (t, i) {
      this.destroy(i)
    }, _i0: null, onmousemove: function (t, i) {
      if (i[Q$]) {
        var n = t[Uy](), e = n ? i.getTooltip(n, t) : null;
        return e ? void Yr[G$]({target: n, content: e, type: n[tF], x: t.pageX, y: t[Pa]}, i[iF], i[nF]) : void Yr[f$]()
      }
    }, destroy: function () {
      Yr.hide()
    }
  };
  var fX = function (t) {
    this.graph = t
  };
  fX[yh] = {
    destroy: function () {
      this[eF] && (this.delayAction = null)
    }, onmousewheel: function (t, i) {
      if (i[sF] !== !1 && t[Ey]) {
        if (yU[SL](t), i.delayedRendering) {
          var n = this.delayAction;
          n || (n = this[eF] = new Wr(function () {
            i.pauseRendering(!1)
          })), i[qD](!0), n.action(100)
        }
        i.zoomByMouseEvent(t, t[Ey], !1)
      }
    }
  };
  var cX = function (t) {
    this[S_] = t
  };
  cX[yh] = {
    onclick: function (t, i) {
      i[rF](t, !Ti(t))
    }
  };
  var uX = function (t) {
    this[S_] = t
  };
  uX.prototype = {
    onclick: function (t, i) {
      i[rF](t, Ti(t))
    }
  }, m(qr, mY), qr.ELEMENT_MOVE_START = hF, qr[aF] = oF, qr[bB] = fF, qr[OD] = cF, qr[kD] = uF, qr[GB] = _F, qr[UB] = dF, qr[lF] = vF, qr[DB] = bF, qr[yF] = gF, qr.RESIZE_START = xF, qr.RESIZING = pF, qr.RESIZE_END = EF, qr[m$] = mF, qr[k$] = wF, qr[TF] = kF, qr[gB] = MF, qr.PAN_END = OF, qr[VM] = IF, qr.EDGE_BUNDLE = SF, qr[j$] = qd, qr[AF] = jF, qr[s$] = PF, qr[t$] = CF, qr.LONG_CLICK = LF, Xr[yh] = {
    _90: function (t) {
      if (this[DF]) switch (t[rl]) {
        case PY[Uv]:
          this[DF]._onElementRemoved(t.data);
          break;
        case PY[tb]:
          this._interactionSupport[RF](t[Ro])
      }
    }, destroy: function () {
      delete this._kp, delete this._4l, this._interactionSupport && (this[DF]._ie(), delete this[DF])
    }, _kp: null, _4l: null, defaultMode: null, _fu: function (t, i, n) {
      this._4l[t] = new Tr(i, n), t == this[fl] && this[DF][NF](i)
    }, addCustomInteraction: function (t) {
      this[DF][BF](t)
    }, removeCustomInteraction: function (t) {
      this[DF]._jrCustomInteractionListener(t)
    }, _mv: function (t) {
      var i = this._4l[t];
      return i ? i : _X[t]
    }
  }, K(Xr[yh], {
    defaultCursor: {
      get: function () {
        return this[$F] ? this[$F][Id] : void 0
      }
    }, currentMode: {
      get: function () {
        return this[FF]
      }, set: function (t) {
        this[FF] != t && (this[FF], this[DF] || (this[DF] = new eU(this._kp)), this._n9urrentMode = t, this[$F] = this._mv(this[FF]), this._kp[zD] = this[Id], this._interactionSupport[NF](this[$F] ? this[$F][GF](this._kp) : []))
      }
    }
  });
  var _X = {};
  eY[zF] = function (t, i, n) {
    var e = new Tr(i, n);
    _X[t] = e
  }, gU[HF] = YF, gU[cl] = Sd, gU[UF] = Yd, gU.INTERACTION_MODE_ZOOMIN = WF, gU.INTERACTION_MODE_ZOOMOUT = qF, gU.INTERACTION_MODE_CREATE_SIMPLE_EDGE = XF, gU[VF] = ZF, gU[KF] = JF, gU[QF] = tG, eY[zF](gU[HF], [aX, sX, fX, nX, tX, oX, hX]), eY[zF](gU[iG], [iX, Pr, aX, sX, fX, nX, oX]), eY[zF](gU[VF], [iX, $r, aX, Or, sX, fX, nX, oX]), eY.registerInteractions(gU.INTERACTION_MODE_CREATE_SHAPE, [iX, Ar, aX, sX, fX, nX, oX]), eY[zF](gU[QF], [jr, aX, sX, fX, nX, oX]), eY[zF](gU[cl], [iX, Gr, $r, aX, eX, sX, fX, nX, tX, oX, hX]), eY[zF](gU.INTERACTION_MODE_SELECTION, [iX, Gr, $r, aX, eX, rX, sX, fX, nX, tX, oX]), eY.registerInteractions(gU[nG], [fX, nX, cX], sU), eY[zF](gU.INTERACTION_MODE_ZOOMOUT, [fX, nX, uX], rU), yU[eG] = sX, yU[sG] = aX, yU[rG] = eX, yU[hG] = fX, yU[aG] = tX, yU[oG] = nX, yU[fG] = oX, yU[cG] = rX, yU[uG] = hX, yU[_G] = $r;
  var dX = function (t) {
    this[S_] = t
  };
  yU[dG] = dX, dX.prototype = {
    getNodeBounds: function (t) {
      return this[S_][lG](t)
    }, isLayoutable: function (t) {
      return this[S_][_B](t) && t[vG] !== !1
    }, getLayoutResult: function () {
    }, updateLocations: function (t, i, n, e, s) {
      if (i === !0) {
        if (this.animate || (this.animate = new qX), n && (this[bG][yG] = n), e && (this[bG][XR] = e), this.animate[gG] = t, s) {
          var r = s, h = this;
          s = function () {
            r[ah](h, t)
          }
        }
        return void this[bG].start(s)
      }
      for (var a in t) {
        var o = t[a], f = o.node;
        f[yD](o.x, o.y)
      }
      s && s.call(this, t)
    }, _fl: function (t) {
      var i, n, e, s = null;
      t && (i = t.byAnimate, s = t[Qu], n = t[yG], e = t[XR]);
      var r = this[xG](t);
      return r ? (this.updateLocations(r, i, n, e, s), r) : !1
    }, doLayout: function (t, i) {
      return this[S_] && i !== !0 ? void this[S_][aD](function () {
        this._fl(t)
      }, this) : this._fl(t)
    }
  };
  var lX = 110, vX = 120, bX = 130, yX = 210, gX = 220, xX = 230, pX = 111, EX = 112, mX = 121, wX = 122, TX = 211,
    kX = 212, MX = 221, OX = 222;
  gU[pG] = lX, gU[EG] = vX, gU.DIRECTION_CENTER = bX, gU.DIRECTION_BOTTOM = yX, gU.DIRECTION_TOP = gX, gU[mG] = xX, gU.DIRECTION_RIGHT_TOP = pX, gU.DIRECTION_RIGHT_BOTTOM = EX, gU.DIRECTION_LEFT_TOP = mX, gU[wG] = wX, gU[TG] = TX, gU.DIRECTION_BOTTOM_RIGHT = kX, gU[kG] = MX, gU[MG] = OX;
  var IX = OG, SX = IG, AX = SG, jX = AG;
  gU[jG] = IX, gU[PG] = AX, gU[CG] = jX, gU[LG] = SX, yU[DG] = Vr;
  var PX = function (t) {
    this.graph = t
  };
  PX.prototype = {
    hGap: 50,
    vGap: 50,
    parentChildrenDirection: yX,
    layoutType: IX,
    defaultSize: {width: 50, height: 60},
    getNodeSize: function (t) {
      if (this.graph._8f._ncc) {
        var i = this[S_][A_](t);
        if (i) return i._fk
      }
      return t.image && t[Ap][Ao] ? {width: t[Ap][Ao][ha], height: t[Ap].bounds.height} : this[RG]
    },
    _n9h: function (t, i) {
      var n = t.id;
      if (!(n in this._9h) && this[NG](t)) {
        var e, s = this.getNodeSize(t);
        e = s instanceof uY ? [-s.x, -s.y] : [s.width / 2, s[Ja] / 2];
        var r = (t[BG], i ? this._9h[i.id] : this[$G]);
        this._9h[n] = new CX(this[FG](t), this[GG](t), this.getLayoutType(t), t[BG], r, t, s[ha], s.height, e)
      }
    },
    getHGap: function (t) {
      return t && R(t[zG]) ? t.hGap : this[zG]
    },
    getVGap: function (t) {
      return t && R(t[HG]) ? t.vGap : this[HG]
    },
    getLayoutType: function (t) {
      return t && t[YG] ? t[YG] : this[YG]
    },
    _9h: null,
    _na3: null,
    _l6: function () {
      this._9h = null, this._na3 = null
    },
    getLayoutResult: function (t) {
      var i, n, e, s, r = this[S_];
      t instanceof Object && (i = t.x, n = t.y, r = t.root || this[S_], e = t[Ao], s = t[UG]), this._9h = {}, this._na3 = new CX, this[$G]._mx(this[zG], this.vGap, this[BG], this.layoutType);
      var h = {}, a = ZX(r, this[WG], this, !1, s);
      return a && (this._na3._fl(i || 0, n || 0, h), e && e.set(this[$G].x, this._na3.y, this[$G][ha], this[$G][Ja])), this._l6(), h
    },
    doLayout: function (t, i) {
      if (R(t)) {
        var n = t, e = 0;
        R(i) && (e = i), t = {x: n, y: e}, i = !0
      }
      return T(this, PX, qG, [t, i])
    }
  }, m(PX, dX);
  var CX = function (t, i, n, e, s, r, h, a, o) {
    this._lt = t || 0, this._lu = i || 0, this.layoutType = n, this[BG] = e, this[XG] = s, s && s._gf(this), this.node = r, this._ec = h, this[VG] = a, this[ZG] = o
  };
  CX[yh] = {
    _mx: function (t, i, n, e) {
      this._lt = t, this._lu = i, this[BG] = n, this.layoutType = e
    }, _8j: function () {
      this._fi = []
    }, _lt: 0, _lu: 0, _fi: null, _ec: 0, _nam: 0, layoutType: null, parentChildrenDirection: null, _gf: function (t) {
      this._fi || (this._fi = []), this._fi[Io](t)
    }, _n91: function (t, i, n, e) {
      var s = new uY;
      return n(this._fi, function (n) {
        n._3d(t, i), s.add(n), e ? t += n[ha] + this._lt : i += n[Ja] + this._lu
      }, this), s
    }, _8l: function (t, i, n, e, s) {
      var r, h = e ? this._lt : this._lu, a = e ? this._lu : this._lt, o = e ? "width" : Ja, f = e ? "height" : ha,
        c = e ? "_ec" : VG, u = e ? "_nam" : KG, _ = e ? "hostDX" : JG, d = e ? "hostDY" : QG, v = new uY, b = 0, y = 0,
        g = [], x = 0, p = 0;
      n(this._fi, function (n) {
        var s = p >= y;
        n[tz] = s ? e ? vX : gX : e ? lX : yX, n._3d(t, i), s ? (g[Io](n), b = Math.max(b, n[o]), y += n[f] + a) : (r || (r = []), r.push(n), x = Math.max(x, n[o]), p += n[f] + a)
      }, this), y -= a, p -= a;
      var E = Math.max(y, p), m = h, w = 0;
      this.node && (s && (m += this[c] + h, E > this[u] ? this[d] = (E - this[u]) / 2 : w = (this[u] - E) / 2), this[_] = b + m / 2 - this[c] / 2);
      var T = 0, k = w;
      return l(g, function (t) {
        e ? t.offset(b - t[o], k) : t[ep](k, b - t[o]), k += a + t[f], v.add(t)
      }, this), r ? (k = w, T = b + m, l(r, function (t) {
        e ? t[ep](T, k) : t[ep](k, T), k += a + t[f], v.add(t)
      }, this), v) : v
    }, offset: function (t, i) {
      this.x += t, this.y += i, this[iz] += t, this[nz] += i, this._6n(t, i)
    }, _nad: function (t, i) {
      return 2 * this.cx - t - i - t
    }, _nac: function (t, i) {
      return 2 * this.cy - t - i - t
    }, _lw: function (t) {
      if (this._fi && 0 != this._fi[rh]) {
        if (t) return this.node && (this[iz] += this[ez](this[iz], this._ec)), void l(this._fi, function (t) {
          t[ep](this[ez](t.x, t[ha]), 0)
        }, this);
        this[sz] && (this.nodeY += this[rz](this.nodeY, this[VG])), l(this._fi, function (t) {
          t[ep](0, this._nac(t.y, t[Ja]))
        }, this)
      }
    }, _6n: function (t, i) {
      this._fi && l(this._fi, function (n) {
        n[ep](t, i)
      }, this)
    }, _3d: function (t, i) {
      return this.x = t || 0, this.y = i || 0, this._fi && 0 != this._fi[rh] ? void this._1b(this.x, this.y, this[YG]) : void (this.node && (this.width = this._ec, this.height = this._nam, this[iz] = this.x, this[nz] = this.y))
    }, _7l: function (t) {
      if (this.node) {
        var i = this[ZG], n = i[0], e = i[1];
        t[this[sz].id] = {
          node: this[sz],
          x: this[iz] + n,
          y: this[nz] + e,
          left: this.nodeX,
          top: this[nz],
          width: this._ec,
          height: this[VG]
        }
      }
      this._fi && l(this._fi, function (i) {
        i._7l(t)
      }, this)
    }, _fl: function (t, i, n) {
      this._3d(t, i), this._7l(n)
    }, _1b: function (t, i, e) {
      var s, r = t, h = i;
      !this[BG] && this[XG] && (this[BG] = this[tz] || this[XG][BG]);
      var a = this.parentChildrenDirection, o = Vr(a);
      if (this[sz]) {
        s = a == bX || a == xX;
        var f = Zr(a);
        s || (o ? t += this._ec + this._lt : i += this[VG] + this._lu)
      }
      var c, u = this.node && this[sz][hz] ? b : l;
      if (e == SX) c = this._8l(t, i, u, !o, s); else {
        var _;
        _ = e == IX ? !o : e == AX, c = this[az](t, i, u, _, s)
      }
      var d = 0, v = 0;
      if (c && !c[gc]() && (d = c[ha], v = c[Ja], this.add(c)), this.node) {
        if (this[iz] = r, this[nz] = h, this.hostDX !== n || this[JG] !== n) this.nodeX += this.hostDX || 0, this[nz] += this[JG] || 0; else {
          var y;
          y = a == yX || a == gX || a == vX || a == lX ? 1 : a == kX || a == OX || a == wX || a == EX ? 0 : 2, o ? (1 == y ? this.nodeY += v / 2 - this[VG] / 2 : 2 == y && (this[nz] += v - this[VG]), h > this[nz] && this[ep](0, h - this[nz])) : (1 == y ? this.nodeX += d / 2 - this._ec / 2 : 2 == y && (this[iz] += d - this._ec), r > this[iz] && this[ep](r - this.nodeX, 0))
        }
        this[XP](this.nodeX, this[nz], this._ec, this[VG]), f && this._lw(o)
      }
    }, node: null, uiBounds: null
  }, m(CX, uY), Jr.prototype = {
    layoutDatas: null, isMovable: function (t) {
      return !this.currentMovingNodes[t.id]
    }, _72: !1, _38: function () {
      this._72 = !0, this[S_]._1d[ol](this._8x, this), this[S_]._13.addListener(this._1z, this)
    }, _1l: function () {
      this._72 = !1, this.graph._1d.removeListener(this._8x, this), this.graph._13[Bv](this._1z, this)
    }, invalidateFlag: !0, invalidateLayoutDatas: function () {
      this[qM] = !0
    }, resetLayoutDatas: function () {
      return this[qM] = !1, this[oz] = Kr[ah](this)
    }, _1z: function (t) {
      qr[dB] == t.kind ? (this[fz] = {}, t[al][Dc](function (t) {
        this[fz][t.id] = t
      }, this)) : qr[bB] == t[rl] && (this[fz] = {})
    }, _8x: function () {
      this[cz]()
    }, isRunning: function () {
      return this[uz] && this.timer._em()
    }, getLayoutResult: function () {
      this[_z](), this[dz]();
      for (var t = this[lz](this[oz].nodeCount || 0, this[oz][vz] || 0), i = 0; t > i && this[yB](!1) !== !1; i++) ;
      var n = this[oz].nodes;
      return this[bz](), n
    }, _me: function () {
      return !1
    }, step: function (t) {
      if (t === !1) return this._me(this.timeStep);
      (this[qM] || !this[oz]) && this.resetLayoutDatas();
      var i = this._me(t), n = this[oz].nodes;
      for (var e in n) {
        var s = n[e], r = s[sz];
        this[gD](r) ? r.setLocation(s.x, s.y) : (s.x = r.x, s.y = r.y, s.vx = 0, s.vy = 0)
      }
      return i
    }, onstop: function () {
      delete this[oz]
    }, start: function (t) {
      if (this[yz]()) return !1;
      this._72 || this._38(), this[gz] || (this[gz] = function (t) {
        return this[yB](t)
      }[el](this)), this[cz](), this[uz] = new lU(this[gz]);
      var i = this;
      return this[uz]._kn(function () {
        i.onstop(), t && t()
      }), !0
    }, stop: function () {
      this.timer && (this[uz]._lm(), this.onstop())
    }, getMaxIterations: function (t) {
      return Math.min(1e3, 3 * t + 10)
    }, minEnergyFunction: function (t, i) {
      return 10 + Math.pow(t + i, 1.4)
    }, resetGraph: function () {
      this._72 || this._38(), this[dz]()
    }, destroy: function () {
      this[_z](), this._1l()
    }
  }, m(Jr, dX);
  var LX = function (t, i, n, e) {
    this.graph = t, R(i) && (this[lv] = i), R(n) && (this.gap = n), R(e) && (this[xz] = e)
  };
  yU[pz] = LX;
  var DX = Ez, RX = mz, NX = wz, BX = Tz;
  gU[kz] = DX, gU.ANGLE_SPACING_REGULAR = RX, gU[Mz] = NX, gU.RADIUS_MODE_VARIABLE = BX, LX.prototype = {
    angleSpacing: DX,
    radiusMode: BX,
    gap: 4,
    radius: 50,
    startAngle: 0,
    _9h: null,
    _na3: null,
    _l6: function () {
      this._9h = null, this[$G] = null
    },
    getLayoutResult: function (t) {
      var i, n = 0, e = 0, s = this[S_];
      t instanceof Object && (n = t.cx || 0, e = t.cy || 0, s = t.root || this.graph, i = t.bounds), this._9h = {}, this._na3 = new GX(this);
      var r = {}, h = KX(s, this._n9h, this);
      return h && (this._na3._fi && 1 == this[$G]._fi.length && (this[$G] = this._na3._fi[0]), this._na3._e6(!0), this._na3._5s(n, e, this[xz], r, i)), this._l6(), r
    },
    _n9h: function (t, i) {
      if (this.isLayoutable(t)) {
        var n = i ? this._9h[i.id] : this._na3;
        this._9h[t.id] = new GX(this, t, n)
      }
    },
    defaultSize: 40,
    getRadius: function () {
      return this.radius
    },
    getNodeSize: function (t) {
      if (this[S_]._8f[Db]) {
        var i = this.graph[A_](t);
        if (i) return (i._fk[ha] + i._fk.height) / 2
      }
      return this.defaultSize
    },
    getGap: function () {
      return this.gap
    },
    _2f: function (t, i, n) {
      return this[Oz](t, i, n) + this[Iz](t, i, n)
    }
  };
  var $X = function (t) {
    var i, n = this._fi[rh], e = 0, s = 0;
    if (l(this._fi, function (t) {
      var n = t._e6();
      1 > n && (n = 1), s += n, n > e && (e = n, i = t)
    }, this), n > 1) {
      var r = 0, h = {}, a = s / n / 3;
      s = 0, l(this._fi, function (t) {
        var i = t._m8;
        a > i && (i = a), h[t.id] = i, s += i
      }, this);
      var o = bq / s;
      l(this._fi, function (i, n) {
        var e = h[i.id], s = e * o;
        0 === n && (r = t ? -s / 2 : -s), i._l2 = r + s / 2, i._l4 = s, r += s
      }, this)
    }
    return [e, i._l4]
  }, FX = function (t) {
    var i = this._8o, n = 2 * Math.PI / i, e = 0, s = t ? 0 : i > 1 ? -n / 2 : 0;
    return l(this._fi, function (t) {
      t._l2 = s % bq, s += n, t._l4 = n;
      var i = t._e6();
      i > e && (e = i)
    }, this), [e, n]
  }, GX = function (t, i, n) {
    this[Sz] = t, i && (this._ma = i, this.id = i.id), n && (n._gf(this), n._m6 = !1, this._l0 = n._l0 + 1)
  }, bq = 2 * Math.PI;
  GX[yh] = {
    _l4: 0, _l2: 0, _kf: 0, _ef: 0, _n9o: 0, _l0: 0, _m6: !0, _m8: 0, _h3: 0, _fi: null, _ma: null, _gf: function (t) {
      this._fi || (this._fi = []), this._fi[Io](t), t[su] = this
    }, _h6: function (t) {
      if (this._l2 = (this._l2 + t) % bq, this._fi) {
        var i = this._fi[rh];
        if (1 == i) return void this._fi[0]._h6(this._l2);
        t = this._l2 + Math.PI, l(this._fi, function (i) {
          i._h6(t)
        }, this)
      }
    }, _8o: 0, _6z: function (t) {
      return this._ma && (this._h3 = this[Sz]._2f(this._ma, this._l0, this._m6) / 2), this._fi ? (this._h3, this._8o = this._fi[rh], this._8o <= 2 || this[Sz].angleSpacing == RX ? FX.call(this, t) : $X[ah](this, t)) : null
    }, _e6: function (t) {
      var i = this._6z(t);
      if (!i) return this._m8 = this._h3;
      var n = i[0], e = i[1], s = this[Sz][Az](this._ma, this._l0);
      if (s < this._h3 && (s = this._h3), this._ef = s, this._h3 + n > s && (s = this._h3 + n), n && this._8o > 1 && e < Math.PI) {
        var r = n / Math.sin(e / 2);
        r > s && (s = r)
      }
      return this._kf = s, this._m8 = s + n, this._ma && this._fi && this[Sz].radiusMode == BX && l(this._fi, function (t) {
        var i = t._m8;
        1 == t._8o && (i /= 2);
        var n = this._h3 + i, e = t._l4;
        if (e && e < Math.PI) {
          var s = Math.sin(e / 2), r = i / s;
          r > i && (i = r)
        }
        n > i && (i = n), t[jz] = i
      }, this), (!this._ma || t) && this._h6(0), this._m8
    }, _5s: function (t, i, n, e, s) {
      if (this._ma && (e[this._ma.id] = {
        x: t,
        y: i,
        node: this._ma
      }, s && s[XP](t - this._h3 / 2, i - this._h3 / 2, this._h3, this._h3)), this._fi) {
        if (!this._ma && 1 == this._fi.length) return void this._fi[0]._5s(t, i, n, e, s);
        n = n || 0;
        var r = this._kf, h = this._ef;
        l(this._fi, function (a) {
          var o = r;
          a[jz] && (o = Math.max(h, a._n9o));
          var f = a._l2 + n, c = t + o * Math.cos(f), u = i + o * Math.sin(f);
          a._5s(c, u, n, e, s)
        }, this)
      }
    }
  }, m(LX, dX);
  var zX = function () {
    w(this, zX, arguments)
  };
  m(zX, Qr);
  var HX = function (t, i) {
    this.node1 = t, this[Pz] = i, t == i ? (this[_u] = !0, this._ku = t._kh) : this._ku = new sY, this._8p = [], this._h9 = eY[Cz]
  };
  eY[Cz] = !0, HX[yh] = {
    node1: null, node2: null, _ku: null, _h9: eY[Cz], _8p: null, _hb: null, agentEdge: null, _ncf: function (t, i, n) {
      this._ku[Dc](function (e) {
        return n && e.$from != n && e.fromAgent != n ? void 0 : t[ah](i, e)
      })
    }, _5t: 0, _5w: 0, _il: function (t, i) {
      return this._ku.add(t) === !1 ? !1 : (i == this.node1 ? this._5t++ : this._5w++, this[Db] ? void this._11(t) : void (this[Db] = !0))
    }, _n9w: function (t, i) {
      return this._ku.remove(t) === !1 ? !1 : (i == this[Lz] ? this._5t-- : this._5w--, this._11(t), void this._ku[Dc](function (t) {
        t[Yk] = !0, t[Ck] = !0
      }, this))
    }, _11: function (t) {
      this[Dz] = !0, this._63 = !0, t[Yk] = !0, t[Ck] = !0
    }, _naq: function () {
      this._63 || (this._63 = !0, this._ku[Dc](function (t) {
        t[Yk] = !0
      }))
    }, isEmpty: function () {
      return this._ku[gc]()
    }, isPositiveOrder: function (t) {
      return this[Lz] == t[xu] || this[Lz] == t[ou]
    }, canBind: function (t) {
      return t && this._63 && this.validate(t), this._ku.length > 1 && this._8p[rh] > 1
    }, _in: function (t) {
      return this._8p[lh](t)
    }, getYOffset: function (t) {
      return this._hb[t.id]
    }, _47: function (t) {
      if (!this[Rz]()) return void (this._hb = {});
      var i = {}, n = this._8p.length;
      if (!(2 > n)) {
        var e = 0, s = this._8p[0];
        i[s.id] = 0;
        for (var r = 1; n > r; r++) {
          s = this._8p[r];
          var h = t[s_](s, Aq[Nz]) || Uq[Aq[Nz]];
          e += h, i[s.id] = e
        }
        if (!this[_u] && e) for (var a = e / 2, r = 0; n > r; r++) s = this._8p[r], i[s.id] -= a;
        this._hb = i
      }
    }, _nce: function (t) {
      return this._h9 == t ? !1 : (this._h9 = t, this._naq(), !0)
    }, reverseExpanded: function () {
      return this[Bz](!this._h9)
    }, _12: function () {
      this[Dz] = !1, this._8p.length = 0;
      var t;
      this._ku.forEach(function (i) {
        if (i[mk]()) {
          if (!this[$z](i)) return t || (t = []), void t.push(i);
          this._8p.push(i)
        }
      }, this), t && (this._8p = t.concat(this._8p))
    }, _e7: function (t) {
      return t == this[ND] || !this[Rz]() || this._h9
    }, validate: function (t) {
      this._63 = !1, this._ku.forEach(function (t) {
        t[Yk] = !1
      }), this[Dz] && this._12();
      var i = this._h9, n = this[Rz](), e = !n || i;
      l(this._8p, function (t) {
        t._$o = !0, t[Fz] = e, e && (t[Ck] = !0)
      }, this), e ? this._9u(null, t) : (this._9u(this._8p[0], t), this.agentEdge._hgInBundle = !0, this[ND].__4i = !0), e && this._47(t)
    }, _9u: function (t, i) {
      if (t != this[ND]) {
        var n = this.agentEdge;
        return this.agentEdge = t, i && i._4e(new wY(this, ND, t, n)), !0
      }
    }
  }, K(HX.prototype, {
    bindableEdges: {
      get: function () {
        return this._8p
      }
    }, edges: {
      get: function () {
        return this._ku._je
      }
    }, length: {
      get: function () {
        return this._ku ? this._ku[rh] : 1
      }
    }, expanded: {
      get: function () {
        return this._h9
      }, set: function (t) {
        return this._h9 == t ? !1 : (this._h9 = t, void this[wT]())
      }
    }
  });
  var YX = function () {
    function t(t, i) {
      this.node = t, this[gp] = i
    }

    function i() {
      this[Gz] = [], this[zz] = 0
    }

    var n = -1e6, e = .8;
    i[yh] = {
      isEmpty: function () {
        return 0 === this[zz]
      }, push: function (i, n) {
        var e = this[Gz][this[zz]];
        e ? (e.node = i, e[gp] = n) : this[Gz][this[zz]] = new t(i, n), ++this[zz]
      }, pop: function () {
        return this[zz] > 0 ? this.stack[--this[zz]] : void 0
      }, reset: function () {
        this[zz] = 0
      }
    };
    var s = [], r = new i, h = function () {
      this[gp] = null, this.quads = [], this.mass = 0, this.massX = 0, this[Hz] = 0, this[Ca] = 0, this.top = 0, this[ea] = 0, this.right = 0, this[Yz] = !1
    }, a = [], o = 0, f = function () {
      var t;
      return a[o] ? (t = a[o], t.quads[0] = null, t[Uz][1] = null, t.quads[2] = null, t[Uz][3] = null, t[gp] = null, t[Wz] = t.massX = t[Hz] = 0, t.left = t[na] = t.top = t[ea] = 0, t[Yz] = !1) : (t = new h, a[o] = t), ++o, t
    }, c = f(), u = function (t, i) {
      var n = Math.abs(t.x - i.x), e = Math.abs(t.y - i.y);
      return 1e-8 > n && 1e-8 > e
    }, _ = function (t) {
      for (r[qz](), r.push(c, t); !r[gc]();) {
        var i = r.pop(), n = i[sz], e = i.body;
        if (n[Yz]) {
          var s = e.x, h = e.y;
          n[Wz] = n[Wz] + e.mass, n[Xz] = n[Xz] + e[Wz] * s, n[Hz] = n.massY + e[Wz] * h;
          var a = 0, o = n[Ca], _ = (n.right + o) / 2, d = n.top, l = (n[ea] + d) / 2;
          if (s > _) {
            a += 1;
            var v = o;
            o = _, _ += _ - v
          }
          if (h > l) {
            a += 2;
            var b = d;
            d = l, l += l - b
          }
          var y = n.quads[a];
          y || (y = f(), y[Ca] = o, y.top = d, y[na] = _, y.bottom = l, n[Uz][a] = y), r[Io](y, e)
        } else if (n[gp]) {
          var g = n[gp];
          if (n[gp] = null, n[Yz] = !0, u(g, e)) {
            if (n.right - n[Ca] < 1e-8) return;
            do {
              var x = Math[zh](), p = (n[na] - n.left) * x, E = (n[ea] - n.top) * x;
              g.x = n[Ca] + p, g.y = n.top + E
            } while (u(g, e))
          }
          r[Io](n, g), r.push(n, e)
        } else n[gp] = e
      }
    }, d = function (t) {
      var i, r, h, a, o = s, f = 1, u = 0, _ = 1;
      for (o[0] = c; f;) {
        var d = o[u], l = d[gp];
        f -= 1, u += 1, l && l !== t ? (r = l.x - t.x, h = l.y - t.y, a = Math[To](r * r + h * h), 0 === a && (r = (Math[zh]() - .5) / 50, h = (Math[zh]() - .5) / 50, a = Math[To](r * r + h * h)), i = n * l.mass * t[Wz] / (a * a), -1e3 > i && (i = -1e3), i /= a, t.fx = t.fx + i * r, t.fy = t.fy + i * h) : (d[Wz] || (d[Wz] = 1), r = d[Xz] / d.mass - t.x, h = d.massY / d[Wz] - t.y, a = Math.sqrt(r * r + h * h), 0 === a && (r = (Math[zh]() - .5) / 50, h = (Math[zh]() - .5) / 50, a = Math[To](r * r + h * h)), (d[na] - d[Ca]) / a < e ? (i = n * d[Wz] * t[Wz] / (a * a), -1e3 > i && (i = -1e3), i /= a, t.fx = t.fx + i * r, t.fy = t.fy + i * h) : (d[Uz][0] && (o[_] = d[Uz][0], f += 1, _ += 1), d.quads[1] && (o[_] = d[Uz][1], f += 1, _ += 1), d[Uz][2] && (o[_] = d[Uz][2], f += 1, _ += 1), d.quads[3] && (o[_] = d.quads[3], f += 1, _ += 1)))
      }
    }, l = function (t, i) {
      n = i;
      var e, s = Number[Vl], r = Number[Vl], h = Number[Vz], a = Number[Vz], u = t, d = u[rh];
      for (e = d; e--;) {
        var l = u[e].x, v = u[e].y;
        s > l && (s = l), l > h && (h = l), r > v && (r = v), v > a && (a = v)
      }
      var b = h - s, y = a - r;
      for (b > y ? a = r + b : h = s + y, o = 0, c = f(), c.left = s, c.right = h, c.top = r, c[ea] = a, e = d; e--;) _(u[e], c)
    };
    return {init: l, update: d}
  }, UX = function (t) {
    t.fx -= t.x * this.attractive, t.fy -= t.y * this[Zz]
  }, WX = function (t) {
    if (0 != t.k) {
      var i = this._nak, n = t[uu], e = t.to, s = e.x - n.x, r = e.y - n.y, h = s * s + r * r, a = Math.sqrt(h) || .1,
        o = (a - i) * t.k * this[Kz];
      o /= a;
      var f = o * s, c = o * r;
      e.fx -= f, e.fy -= c, n.fx += f, n.fy += c
    }
  };
  Qr[yh] = {
    appendNodeInfo: function (t, i) {
      i[Wz] = t[Jz] || 1, i.fx = 0, i.fy = 0, i.vx = 0, i.vy = 0
    }, appendEdgeInfo: function (t, i) {
      i.k = t.layoutElasticity || 1
    }, setMass: function (t, i) {
      t[Jz] = i, this[oz] && this[oz][Qz] && (t = this.layoutDatas[Qz][t.id], t && (t[Wz] = i))
    }, setElasticity: function (t, i) {
      t.layoutElasticity = i, this.layoutDatas && this.layoutDatas[vM] && (t = this[oz].edges[t.id], t && (t.k = i))
    }, _nak: 50, _hm: .5, timeStep: .05, repulsion: 50, attractive: .1, elastic: 3, _mh: 1e3, _k0: function (t) {
      return this._mh + .3 * (t - this._mh)
    }, _me: function (t, i) {
      var n = (Date.now(), this[oz][Qz]);
      for (var e in n) {
        var s = n[e];
        i && (s.x += Math[zh]() - .5, s.y += Math[zh]() - .5), UX.call(this, s)
      }
      var r = this[oz].groups;
      if (r) for (var e in r) {
        var h = r[e], a = h.children, o = 0, f = 0;
        a[Dc](function (t) {
          o += t.x, f += t.y
        }), o /= a[rh], f /= a.length;
        var c = 10 * this[Zz];
        a.forEach(function (t) {
          t.fx -= (t.x - o) * c, t.fy -= (t.y - f) * c
        })
      }
      var u = this[tH];
      u || (u = this[tH] = YX()), u[md](this[oz][iH], -this[nH] * this[nH] * this[nH]);
      for (var e in n) u.update(n[e]);
      if (this[Kz]) {
        var _ = this[oz][vM];
        for (var e in _) WX.call(this, _[e])
      }
      return this._mi(t)
    }, _mi: function (t) {
      var i = this[oz].minEnergy, n = (this[oz][eH], this[oz][Qz]), t = this[sH], e = 0, s = this._hm;
      for (var r in n) {
        var h = n[r], a = h.fx / h[Wz], o = h.fy / h[Wz], f = h.vx += a * t, c = h.vy += o * t;
        h.x += f * t, h.y += c * t, i > e && (e += 2 * (f * f + c * c)), h.fx = 0, h.fy = 0, h.vx *= s, h.vy *= s
      }
      return this[oz][eH] = e, e >= i
    }
  }, m(Qr, Jr), yU[rH] = Qr;
  var qX = function (t) {
    this[gG] = t
  };
  qX.prototype = {
    oldLocations: null, _ek: null, duration: 700, animationType: dU[hH], _7j: function (t) {
      if (this._ek = t, this[aH] = {}, t) for (var i in t) {
        var n = t[i], e = n[sz];
        this.oldLocations[i] = {x: e.x, y: e.y}
      }
    }, setLocation: function (t, i, n) {
      t.setLocation(i, n)
    }, forEach: function (t, i) {
      for (var n in this.locations) {
        var e = this[aH][n], s = this.locations[n];
        t.call(i, e, s)
      }
    }, _kc: function (t) {
      this.forEach(function (i, n) {
        var e = n.node, s = i.x + (n.x - i.x) * t, r = i.y + (n.y - i.y) * t;
        this[yD](e, s, r)
      }, this)
    }, stop: function () {
      this[oH] && this._ncnimate._lm()
    }, start: function (t) {
      this[oH] ? (this._ncnimate._lm(), this._ncnimate._is = this.duration, this._ncnimate._ewType = this[XR], this._ncnimate[fH] = this._onfinish) : this[oH] = new vU(this._kc, this, this[yG], this[XR]), this[oH]._kn(t)
    }
  }, K(qX[yh], {
    locations: {
      get: function () {
        return this._ek
      }, set: function (t) {
        this._ek != t && this._7j(t)
      }
    }
  });
  var XX = function (t) {
    function i(i) {
      var n = !1;
      return i.forEachInEdge(function (i) {
        return t.contains(i) && !i[_u]() ? (n = !0, !1) : void 0
      }), n
    }

    function n(i) {
      var n = !1;
      return i[vl](function (i) {
        return t[B_](i) && !i[_u]() ? (n = !0, !1) : void 0
      }), n
    }

    var e, s = new sY;
    return t[Dc](function (t) {
      t instanceof Tq && (i(t) ? !e && n(t) && (e = t) : s.add(t))
    }), s[gc]() && e && s.add(e), s
  }, VX = function (t, i, n, e, s, r) {
    if (i instanceof CY) return t(i, n, e, s, r), i;
    if (i instanceof Wq) {
      var h = new sY;
      i[tM][Dc](function (t) {
        return i.isVisible(t) ? t._hi() && t._h9 && t.hasChildren() ? void (t[TM] && (t[TM][qM] = !1)) : void h.add(t) : void 0
      }), i = h
    } else if (Array[Vu](i)) i = new sY(i); else if (!(i instanceof sY)) throw new Error(cH);
    return i = XX(i, e), i.forEach(function (i) {
      t(i, n, e, s, r)
    }), i
  }, ZX = function (t, i, n, e, s) {
    return VX(JX, t, i, n, e, s)
  }, KX = function (t, i, n, e, s) {
    return VX(QX, t, i, n, e, s)
  };
  bs[yh][uH] = function (t, i, n, e) {
    ZX(this, t, i, n, e)
  }, bs[yh][_H] = function (t, i, n, e) {
    t instanceof Object && 1 == arguments[rh] && (i = t[Nv]), KX(this, t, i, n, e)
  }, yU[uH] = ZX, yU.forEachByTopoBreadthFirstSearch = KX;
  var JX = function (t, i, n, e, s) {
    function r(t, i, n, e, s, h, a, o) {
      t[ll] = h, e || i[ah](n, t, o, a), th(t, function (o) {
        r(o, i, n, e, s, h, a + 1, t)
      }, o, s, h, n), e && i[ah](n, t, o, a)
    }

    r(t, i, n, e, s, {}, 0)
  }, QX = function (t, i, n, e, s) {
    function r(t, i, n, e, s, h, a) {
      var o, f = t[rh];
      t[Dc](function (t, r) {
        var c = t.v;
        c[ll] = h, e || i.call(n, c, t[dH], a, r, f), th(c, function (t) {
          o || (o = []), t[ll] = h, o[Io]({v: t, _from: c})
        }, c, s, h, n)
      }), o && r(o, i, n, e, s, h, a + 1), e && t[Dc](function (t, e) {
        i[ah](n, t.v, t._from, a, e, f)
      })
    }

    r([{v: t}], i, n, e, s, {}, 0)
  };
  yU[lH] = V, yU.log = ti, yU[wf] = ni, yU[Qh] = ii, yU[vH] = HH, yU[bH] = zH, yU.isWebkit = WH, yU[yH] = qH, yU[gH] = XH, yU[xH] = ZH, yU[pH] = VH, yU[EH] = KH, yU[mH] = Uq, yU[wH] = eY, yU[SD] = Aq, yU[TH] = gU, yU[kH] = sW, yU.Graph = Wq, yU[MH] = Iq, yU[OH] = Gq, yU[IH] = ks, yU.EdgeUI = Ts, yU.LabelUI = Hq, yU[SH] = zq, yU.Shapes = Mq, yU[AH] = iW, yU[jC] = BU, yU[jH] = qr, yU[eT] = mq, yU[PL] = Tq, yU.Edge = wq, yU[PH] = bs, yU[CH] = HX, yU.TreeLayouter = PX, yU[kh] = LH;
  var tV = DH;
  return yU[yw] = RH, yU[pw] = NH, yU.copyright = "Copyright © 2019 Qunee.com", yU.css = bi, yU[BH] = qq, ti = function () {
  }, yU[xw] = $H, yU
}, window, document, undefined);
