import { u as y, v as I, w as G, b as g, j as r, C as F, x as c, y as De, z as S, A as K, B as L, O as de, D as A, F as h, G as l, H as Re, I as Be, J as Je, K as Z, L as me, E as u, N as Ve, k as ce, l as Fe, P as qe, Q as Xe, V as Ye, R as Ee, S as $, T as We, U as M, M as _, W as Ge, X as q, Y as Ke, Z as ve, _ as ze, $ as Ze, a0 as Qe, a1 as et, a2 as tt, a3 as it, a4 as nt, a5 as ot, a6 as Le, a7 as st, a8 as at, a9 as rt, aa as Me, ab as lt, ac as pe, ad as dt, ae as ct } from "./copilot-CjVeA4wK.js";
import { n as R, r as C } from "./state-iYK--DhW.js";
import { e as O, m as he } from "./overlay-monkeypatch-DwFNfocR.js";
import { i as d } from "./icons-Dn0aOY4J.js";
import { e as E } from "./early-project-state-CqEloDes.js";
const pt = 1, be = 36, ht = 18;
function ut(e, t) {
  if (e.length === 0)
    return;
  const i = gt(e, t);
  for (const n in e)
    e[n].style.setProperty("--content-height", `${i[n]}px`);
}
function gt(e, t) {
  const i = e.length, n = e.filter((s) => s.panelInfo && s.panelInfo.expanded).length, o = i - n;
  return e.map((s) => {
    const a = s.panelInfo;
    return a && !a.expanded ? be : (t.offsetHeight - (t.position === "bottom" ? ht : 0) - o * be - i * pt) / n;
  });
}
var ft = Object.defineProperty, mt = Object.getOwnPropertyDescriptor, T = (e, t, i, n) => {
  for (var o = n > 1 ? void 0 : n ? mt(t, i) : t, s = e.length - 1, a; s >= 0; s--)
    (a = e[s]) && (o = (n ? a(t, i, o) : a(o)) || o);
  return n && o && ft(t, i, o), o;
};
const Q = "data-drag-initial-index", B = "data-drag-final-index";
let k = class extends I {
  constructor() {
    super(...arguments), this.position = "right", this.opened = !1, this.keepOpen = !1, this.resizing = !1, this.closingForcefully = !1, this.draggingSectionPanel = null, this.panelCountChanged = G(() => {
      this.refreshSplit();
    }, 100), this.documentMouseUpListener = () => {
      this.resizing && g.emit("user-select", { allowSelection: !0 }), this.resizing = !1, r.setDrawerResizing(!1), this.removeAttribute("resizing");
    }, this.activationAnimationTransitionEndListener = () => {
      this.style.removeProperty("--closing-delay"), this.style.removeProperty("--initial-position"), this.removeEventListener("transitionend", this.activationAnimationTransitionEndListener);
    }, this.resizingMouseMoveListener = (e) => {
      if (!this.resizing)
        return;
      const { x: t, y: i } = e;
      e.stopPropagation(), e.preventDefault(), requestAnimationFrame(() => {
        let n;
        if (this.position === "right") {
          const o = document.body.clientWidth - t;
          this.style.setProperty("--size", `${o}px`), F.saveDrawerSize(this.position, o), n = { width: o };
        } else if (this.position === "left") {
          const o = t;
          this.style.setProperty("--size", `${o}px`), F.saveDrawerSize(this.position, o), n = { width: o };
        } else if (this.position === "bottom") {
          const o = document.body.clientHeight - i;
          this.style.setProperty("--size", `${o}px`), F.saveDrawerSize(this.position, o), n = { height: o };
        }
        c.panels.filter((o) => !o.floating && o.panel === this.position).forEach((o) => {
          c.updatePanel(o.tag, n);
        });
      });
    }, this.sectionPanelDraggingStarted = (e, t) => {
      this.draggingSectionPanel = e, g.emit("user-select", { allowSelection: !1 }), this.draggingSectionPointerStartY = t.clientY, e.toggleAttribute("dragging", !0), e.style.zIndex = "1000", Array.from(this.querySelectorAll("copilot-section-panel-wrapper")).forEach((i, n) => {
        i.setAttribute(Q, `${n}`);
      }), document.addEventListener("mousemove", this.sectionPanelDragging), document.addEventListener("mouseup", this.sectionPanelDraggingFinished);
    }, this.sectionPanelDragging = (e) => {
      if (!this.draggingSectionPanel)
        return;
      const { clientX: t, clientY: i } = e;
      if (!De(this.getBoundingClientRect(), t, i)) {
        this.cleanUpDragging();
        return;
      }
      const n = i - this.draggingSectionPointerStartY;
      this.draggingSectionPanel.style.transform = `translateY(${n}px)`, this.updateSectionPanelPositionsWhileDragging();
    }, this.sectionPanelDraggingFinished = () => {
      if (!this.draggingSectionPanel)
        return;
      g.emit("user-select", { allowSelection: !0 });
      const e = this.getAllPanels().filter(
        (t) => t.hasAttribute(B) && t.panelInfo?.panelOrder !== Number.parseInt(t.getAttribute(B), 10)
      ).map((t) => ({
        tag: t.panelTag,
        order: Number.parseInt(t.getAttribute(B), 10)
      }));
      this.cleanUpDragging(), c.updateOrders(e), document.removeEventListener("mouseup", this.sectionPanelDraggingFinished), document.removeEventListener("mousemove", this.sectionPanelDragging), this.refreshSplit();
    }, this.updateSectionPanelPositionsWhileDragging = () => {
      const e = this.draggingSectionPanel.getBoundingClientRect().height;
      this.getAllPanels().sort((t, i) => {
        const n = t.getBoundingClientRect(), o = i.getBoundingClientRect(), s = (n.top + n.bottom) / 2, a = (o.top + o.bottom) / 2;
        return s - a;
      }).forEach((t, i) => {
        if (t.setAttribute(B, `${i}`), t.panelTag !== this.draggingSectionPanel?.panelTag) {
          const n = Number.parseInt(t.getAttribute(Q), 10);
          n > i ? t.style.transform = `translateY(${-e}px)` : n < i ? t.style.transform = `translateY(${e}px)` : t.style.removeProperty("transform");
        }
      });
    }, this.panelExpandedListener = (e) => {
      this.querySelector(`copilot-section-panel-wrapper[paneltag="${e.detail.panelTag}"]`) && this.refreshSplit();
    };
  }
  static get styles() {
    return [
      S(K),
      L`
        :host {
          --size: 350px;
          --min-size: 20%;
          --max-size: 80%;
          --default-content-height: 300px;
          --transition-duration: var(--duration-2);
          --opening-delay: var(--duration-2);
          --closing-delay: var(--duration-3);
          --hover-size: 18px;
          --initial-position: 0px;
          position: absolute;
          z-index: var(--z-index-drawer);
          transition: translate var(--transition-duration) var(--closing-delay);
        }

        :host([no-transition]),
        :host([no-transition]) .container {
          transition: none;
          -webkit-transition: none;
          -moz-transition: none;
          -o-transition: none;
        }

        :host(:is([position='left'], [position='right'])) {
          width: var(--size);
          min-width: var(--min-size);
          max-width: var(--max-size);
          top: 0;
          bottom: 0;
        }

        :host([position='left']) {
          left: var(--initial-position);
          translate: calc(-100% + var(--hover-size)) 0%;
          padding-right: var(--hover-size);
        }

        :host([position='right']) {
          right: var(--initial-position);
          translate: calc(100% - var(--hover-size)) 0%;
          padding-left: var(--hover-size);
        }

        :host([position='bottom']) {
          height: var(--size);
          min-height: var(--min-size);
          max-height: var(--max-size);
          bottom: var(--initial-position);
          left: 0;
          right: 0;
          translate: 0% calc(100% - var(--hover-size));
          padding-top: var(--hover-size);
        }

        /* The visible container. Needed to have extra space for hover and resize handle outside it. */

        .container {
          display: flex;
          flex-direction: column;
          box-sizing: border-box;
          height: 100%;
          background: var(--background-color);
          -webkit-backdrop-filter: var(--surface-backdrop-filter);
          backdrop-filter: var(--surface-backdrop-filter);
          overflow-y: auto;
          overflow-x: hidden;
          box-shadow: var(--surface-box-shadow-2);
          transition:
            opacity var(--transition-duration) var(--closing-delay),
            visibility calc(var(--transition-duration) * 2) var(--closing-delay);
          opacity: 0;
          /* For accessibility (restored when open) */
          visibility: hidden;
        }

        :host([position='left']) .container {
          border-right: 1px solid var(--surface-border-color);
        }

        :host([position='right']) .container {
          border-left: 1px solid var(--surface-border-color);
        }

        :host([position='bottom']) .container {
          border-top: 1px solid var(--surface-border-color);
        }

        /* Opened state */

        :host(:is([opened], [keepopen])) {
          translate: 0% 0%;
          transition-delay: var(--opening-delay);
          z-index: var(--z-index-opened-drawer);
        }

        :host(:is([opened], [keepopen])) .container {
          transition-delay: var(--opening-delay);
          visibility: visible;
          opacity: 1;
        }

        .drawer-indicator {
          align-items: center;
          border-radius: 9999px;
          box-shadow: inset 0 0 0 1px hsl(0 0% 0% / 0.2);
          color: white;
          display: flex;
          height: 1.75rem;
          justify-content: center;
          overflow: hidden;
          opacity: 1;
          position: absolute;
          transition-delay: 0.5s;
          transition-duration: 0.2s;
          transition-property: opacity;
          width: 1.75rem;
        }

        .drawer-indicator::before {
          animation: 5s swirl linear infinite;
          animation-play-state: running;
          background-image:
            radial-gradient(circle at 50% -10%, hsl(221 100% 55% / 0.6) 0%, transparent 60%),
            radial-gradient(circle at 25% 40%, hsl(303 71% 64%) 0%, transparent 70%),
            radial-gradient(circle at 80% 10%, hsla(262, 38%, 9%, 0.5) 0%, transparent 80%),
            radial-gradient(circle at 110% 50%, hsla(147, 100%, 77%, 1) 20%, transparent 100%);
          content: '';
          inset: 0;
          opacity: 1;
          position: absolute;
          transition: opacity 0.5s;
        }
        :host([attention-required]) .drawer-indicator::before {
          background-image:
            radial-gradient(circle at 50% -10%, hsl(0deg 100% 55% / 60%) 0%, transparent 60%),
            radial-gradient(circle at 25% 40%, hsl(0deg 71% 64%) 0%, transparent 70%),
            radial-gradient(circle at 80% 10%, hsl(0deg 38% 9% / 50%) 0%, transparent 80%),
            radial-gradient(circle at 110% 50%, hsl(0deg 100% 77%) 20%, transparent 100%);
        }
        :host([opened]) .drawer-indicator {
          opacity: 0;
          transition-delay: 0s;
        }

        .drawer-indicator svg {
          height: 0.75rem;
          width: 0.75rem;
          z-index: 1;
        }

        :host([position='right']) .drawer-indicator {
          left: 0.25rem;
          top: calc(50% - 0.875rem);
        }

        :host([position='right']) .drawer-indicator svg {
          margin-inline-start: -0.625rem;
          transform: rotate(-90deg);
        }

        :host([position='left']) .drawer-indicator {
          right: 0.25rem;
          top: calc(50% - 0.875rem);
        }

        :host([position='left']) .drawer-indicator svg {
          margin-inline-end: -0.625rem;
          transform: rotate(90deg);
        }

        :host([position='bottom']) .drawer-indicator {
          left: calc(50% - 0.875rem);
          top: 0.25rem;
        }

        :host([position='bottom']) .drawer-indicator svg {
          margin-top: -0.625rem;
        }

        .resize {
          position: absolute;
          z-index: 10;
          inset: 0;
        }

        :host(:is([position='left'], [position='right'])) .resize {
          width: var(--hover-size);
          cursor: col-resize;
        }

        :host([position='left']) .resize {
          left: auto;
          right: calc(var(--hover-size) * 0.5);
        }

        :host([position='right']) .resize {
          right: auto;
          left: calc(var(--hover-size) * 0.5);
        }

        :host([position='bottom']) .resize {
          height: var(--hover-size);
          bottom: auto;
          top: calc(var(--hover-size) * 0.5);
          cursor: row-resize;
        }

        :host([resizing]) .container {
          /* vaadin-grid (used in the outline) blocks the mouse events */
          pointer-events: none;
        }

        /* Visual indication of the drawer */

        :host::before {
          content: '';
          position: absolute;
          pointer-events: none;
          z-index: -1;
          inset: var(--hover-size);
          transition: opacity var(--transition-duration) var(--closing-delay);
        }

        :host([document-hidden])::before {
          animation: none;
        }

        :host([document-hidden]) .drawer-indicator {
          -webkit-filter: grayscale(100%); /* Chrome, Safari, Opera */
          filter: grayscale(100%);
        }

        :host(:is([opened], [keepopen]))::before {
          transition-delay: var(--opening-delay);
          opacity: 0;
        }
      `
    ];
  }
  connectedCallback() {
    super.connectedCallback(), this.reaction(
      () => c.panels,
      () => this.requestUpdate()
    ), this.reaction(
      () => r.operationInProgress,
      (t) => {
        t === de.DragAndDrop && !this.opened && !this.keepOpen ? this.style.setProperty("pointer-events", "none") : this.style.setProperty("pointer-events", "auto");
      }
    ), this.reaction(
      () => c.getAttentionRequiredPanelConfiguration(),
      () => {
        const t = c.getAttentionRequiredPanelConfiguration();
        t && !t.floating && this.toggleAttribute(A, t.panel === this.position);
      }
    ), this.reaction(
      () => r.active,
      () => {
        if (!r.active || !h.isActivationAnimation() || r.activatedFrom === "restore" || r.activatedFrom === "test")
          return;
        const t = c.getAttentionRequiredPanelConfiguration();
        t && !t.floating && t.panel === this.position || (this.addEventListener("transitionend", this.activationAnimationTransitionEndListener), requestAnimationFrame(() => {
          this.toggleAttribute("no-transition", !0), this.opened = !0, this.style.setProperty("--closing-delay", "var(--duration-1)"), this.style.setProperty("--initial-position", "calc(-1 * (max(var(--size), var(--min-size)) * 1) / 3)"), requestAnimationFrame(() => {
            this.toggleAttribute("no-transition", !1), this.opened = !1;
          });
        }));
      }
    ), document.addEventListener("mouseup", this.documentMouseUpListener);
    const e = F.getDrawerSize(this.position);
    e && this.style.setProperty("--size", `${e}px`), document.addEventListener("mousemove", this.resizingMouseMoveListener), this.addEventListener("mouseenter", this.mouseEnterListener), g.on("document-activation-change", (t) => {
      this.toggleAttribute("document-hidden", !t.detail.active);
    }), g.on("panel-expanded", this.panelExpandedListener), this.reaction(
      () => c.panels.filter(
        (t) => !t.floating && t.panel === this.position
      ).length,
      () => {
        this.panelCountChanged();
      }
    );
  }
  firstUpdated(e) {
    super.firstUpdated(e), requestAnimationFrame(() => this.toggleAttribute("no-transition", !1)), this.resizeElement.addEventListener("mousedown", (t) => {
      t.button === 0 && (this.resizing = !0, r.setDrawerResizing(!0), this.setAttribute("resizing", ""), g.emit("user-select", { allowSelection: !1 }));
    });
  }
  updated(e) {
    super.updated(e), e.has("opened") && this.opened && this.hasAttribute(A) && (this.removeAttribute(A), c.clearAttention());
  }
  disconnectedCallback() {
    super.disconnectedCallback(), document.removeEventListener("mousemove", this.resizingMouseMoveListener), document.removeEventListener("mouseup", this.documentMouseUpListener), this.removeEventListener("mouseenter", this.mouseEnterListener), g.off("panel-expanded", this.panelExpandedListener);
  }
  /**
   * Cleans up attributes/styles etc... for dragging operations
   * @private
   */
  cleanUpDragging() {
    this.draggingSectionPanel && (r.setSectionPanelDragging(!1), this.draggingSectionPanel.style.zIndex = "", Array.from(this.querySelectorAll("copilot-section-panel-wrapper")).forEach((e) => {
      e.style.removeProperty("transform"), e.removeAttribute(B), e.removeAttribute(Q);
    }), this.draggingSectionPanel.removeAttribute("dragging"), this.draggingSectionPanel = null);
  }
  getAllPanels() {
    return Array.from(this.querySelectorAll("copilot-section-panel-wrapper"));
  }
  getAllPanelsOrdered() {
    return this.getAllPanels().sort((e, t) => e.panelInfo && t.panelInfo ? e.panelInfo.panelOrder - t.panelInfo.panelOrder : 0);
  }
  /**
   * Closes the drawer and disables mouse enter event for a while.
   */
  forceClose() {
    this.closingForcefully = !0, this.opened = !1, setTimeout(() => {
      this.closingForcefully = !1;
    }, 0.5);
  }
  mouseEnterListener(e) {
    if (this.closingForcefully || r.sectionPanelResizing)
      return;
    document.querySelector("copilot-main").shadowRoot.querySelector("copilot-drawer-panel[opened]") || (this.refreshSplit(), this.opened = !0);
  }
  render() {
    return l`
      <div class="container">
        <slot></slot>
      </div>
      <div class="resize"></div>
      <div class="drawer-indicator">${d.chevronUp}</div>
    `;
  }
  refreshSplit() {
    ut(this.getAllPanelsOrdered(), this);
  }
};
T([
  R({ reflect: !0, attribute: !0 })
], k.prototype, "position", 2);
T([
  R({ reflect: !0, type: Boolean })
], k.prototype, "opened", 2);
T([
  R({ reflect: !0, type: Boolean })
], k.prototype, "keepOpen", 2);
T([
  O(".container")
], k.prototype, "container", 2);
T([
  O(".resize")
], k.prototype, "resizeElement", 2);
k = T([
  y("copilot-drawer-panel")
], k);
var vt = Object.defineProperty, bt = Object.getOwnPropertyDescriptor, _e = (e, t, i, n) => {
  for (var o = n > 1 ? void 0 : n ? bt(t, i) : t, s = e.length - 1, a; s >= 0; s--)
    (a = e[s]) && (o = (n ? a(t, i, o) : a(o)) || o);
  return n && o && vt(t, i, o), o;
};
let oe = class extends Re {
  constructor() {
    super(...arguments), this.checked = !1;
  }
  static get styles() {
    return L`
      .switch {
        display: inline-flex;
        align-items: center;
        gap: var(--space-100);
      }

      .switch input {
        height: 0;
        opacity: 0;
        width: 0;
      }

      .slider {
        background-color: var(--gray-300);
        border-radius: 9999px;
        cursor: pointer;
        inset: 0;
        position: absolute;
        transition: 0.4s;
        height: 0.75rem;
        position: relative;
        width: 1.5rem;
        min-width: 1.5rem;
      }

      .slider:before {
        background-color: white;
        border-radius: 50%;
        bottom: 1px;
        content: '';
        height: 0.625rem;
        left: 1px;
        position: absolute;
        transition: 0.4s;
        width: 0.625rem;
      }

      input:checked + .slider {
        background-color: var(--selection-color);
      }

      input:checked + .slider:before {
        transform: translateX(0.75rem);
      }

      label:has(input:focus) {
        outline: 2px solid var(--selection-color);
        outline-offset: 2px;
      }
    `;
  }
  render() {
    return l`
      <label class="switch">
        <input
          class="feature-toggle"
          id="feature-toggle-${this.id}"
          type="checkbox"
          ?checked="${this.checked}"
          @change=${(e) => {
      e.preventDefault(), this.checked = e.target.checked, this.dispatchEvent(new CustomEvent("on-change"));
    }} />
        <span class="slider"></span>
        ${this.title}
      </label>
    `;
  }
  //  @change=${(e: InputEvent) => this.toggleFeatureFlag(e, feature)}
};
_e([
  R({ reflect: !0, type: Boolean })
], oe.prototype, "checked", 2);
oe = _e([
  y("copilot-toggle-button")
], oe);
class wt {
  constructor() {
    this.offsetX = 0, this.offsetY = 0;
  }
  draggingStarts(t, i) {
    this.offsetX = i.clientX - t.getBoundingClientRect().left, this.offsetY = i.clientY - t.getBoundingClientRect().top;
  }
  dragging(t, i) {
    const n = i.clientX, o = i.clientY, s = n - this.offsetX, a = n - this.offsetX + t.getBoundingClientRect().width, p = o - this.offsetY, m = o - this.offsetY + t.getBoundingClientRect().height;
    return this.adjust(t, s, p, a, m);
  }
  adjust(t, i, n, o, s) {
    let a, p, m, v;
    const x = document.documentElement.getBoundingClientRect().width, N = document.documentElement.getBoundingClientRect().height;
    return (o + i) / 2 < x / 2 ? (t.style.setProperty("--left", `${i}px`), t.style.setProperty("--right", ""), v = void 0, a = Math.max(0, i)) : (t.style.removeProperty("--left"), t.style.setProperty("--right", `${x - o}px`), a = void 0, v = Math.max(0, x - o)), (n + s) / 2 < N / 2 ? (t.style.setProperty("--top", `${n}px`), t.style.setProperty("--bottom", ""), m = void 0, p = Math.max(0, n)) : (t.style.setProperty("--top", ""), t.style.setProperty("--bottom", `${N - s}px`), p = void 0, m = Math.max(0, N - s)), {
      left: a,
      right: v,
      top: p,
      bottom: m
    };
  }
  anchor(t) {
    const { left: i, top: n, bottom: o, right: s } = t.getBoundingClientRect();
    return this.adjust(t, i, n, s, o);
  }
  anchorLeftTop(t) {
    const { left: i, top: n } = t.getBoundingClientRect();
    return t.style.setProperty("--left", `${i}px`), t.style.setProperty("--right", ""), t.style.setProperty("--top", `${n}px`), t.style.setProperty("--bottom", ""), {
      left: i,
      top: n
    };
  }
}
const P = new wt();
var yt = Object.defineProperty, xt = Object.getOwnPropertyDescriptor, H = (e, t, i, n) => {
  for (var o = n > 1 ? void 0 : n ? xt(t, i) : t, s = e.length - 1, a; s >= 0; s--)
    (a = e[s]) && (o = (n ? a(t, i, o) : a(o)) || o);
  return n && o && yt(t, i, o), o;
};
const we = "https://github.com/JetBrains/JetBrainsRuntime/releases";
function Pt(e, t) {
  if (!t)
    return !0;
  const [i, n, o] = t.split(".").map((m) => parseInt(m)), [s, a, p] = e.split(".").map((m) => parseInt(m));
  if (i < s)
    return !0;
  if (i === s) {
    if (n < a)
      return !0;
    if (n === a)
      return o < p;
  }
  return !1;
}
const ye = "Download complete";
let w = class extends I {
  constructor() {
    super(), this.javaPluginSectionOpened = !1, this.hotswapSectionOpened = !1, this.hotswapTab = "hotswapagent", this.downloadStatusMessages = [], this.downloadProgress = 0, this.onDownloadStatusUpdate = this.downloadStatusUpdate.bind(this), this.reaction(
      () => [r.jdkInfo, r.idePluginState],
      () => {
        r.idePluginState && (!r.idePluginState.ide || !r.idePluginState.active ? this.javaPluginSectionOpened = !0 : (!(/* @__PURE__ */ new Set(["vscode", "intellij"])).has(r.idePluginState.ide) || !r.idePluginState.active) && (this.javaPluginSectionOpened = !1)), r.jdkInfo && Z() !== "success" && (this.hotswapSectionOpened = !0);
      },
      { fireImmediately: !0 }
    );
  }
  connectedCallback() {
    super.connectedCallback(), g.on("set-up-vs-code-hotswap-status", this.onDownloadStatusUpdate);
  }
  disconnectedCallback() {
    super.disconnectedCallback(), g.off("set-up-vs-code-hotswap-status", this.onDownloadStatusUpdate);
  }
  render() {
    const e = {
      intellij: r.idePluginState?.ide === "intellij",
      vscode: r.idePluginState?.ide === "vscode",
      eclipse: r.idePluginState?.ide === "eclipse",
      idePluginInstalled: !!r.idePluginState?.active
    };
    return l`
      <div part="container">${this.renderPluginSection(e)} ${this.renderHotswapSection(e)}</div>
      <div part="footer">
        <vaadin-button
          id="close"
          @click="${() => c.updatePanel(ue.tag, { floating: !1 })}"
          >Close
        </vaadin-button>
      </div>
    `;
  }
  renderPluginSection(e) {
    let t = "";
    e.intellij ? t = "IntelliJ" : e.vscode ? t = "VS Code" : e.eclipse && (t = "Eclipse");
    let i, n;
    e.vscode || e.intellij ? e.idePluginInstalled ? (i = `Plugin for ${t} installed`, n = this.renderPluginInstalledContent()) : (i = `Plugin for ${t} not installed`, n = this.renderPluginIsNotInstalledContent(e)) : e.eclipse ? (i = "Eclipse development workflow is not supported yet", n = this.renderEclipsePluginContent()) : (i = "No IDE found", n = this.renderNoIdePluginContent());
    const o = e.idePluginInstalled ? d.checkCircle : d.alertTriangle;
    return l`
      <details
        part="panel"
        .open=${this.javaPluginSectionOpened}
        @toggle=${(s) => {
      me(() => {
        this.javaPluginSectionOpened = s.target.open;
      });
    }}>
        <summary part="header">
          <span class="icon ${e.idePluginInstalled ? "success" : "warning"}">${o}</span>
          <div>${i}</div>
        </summary>
        <div part="content">${n}</div>
      </details>
    `;
  }
  renderNoIdePluginContent() {
    return l`
      <div>
        <div>We could not detect an IDE</div>
        ${this.recommendSupportedPlugin()}
      </div>
    `;
  }
  renderEclipsePluginContent() {
    return l`
      <div>
        <div>Eclipse workflow environment is not supported yet.</div>
        ${this.recommendSupportedPlugin()}
      </div>
    `;
  }
  recommendSupportedPlugin() {
    return l`<p>
      Please use <a href="https://code.visualstudio.com">Visual Studio Code</a> or
      <a href="https://www.jetbrains.com/idea">IntelliJ IDEA</a> for better development experience
    </p>`;
  }
  renderPluginInstalledContent() {
    return l` <p>You have a running plugin. Enjoy your awesome development workflow!</p> `;
  }
  renderPluginIsNotInstalledContent(e) {
    let t = null, i = "Install from Marketplace";
    return e.intellij ? (t = Xe, i = "Install from JetBrains Marketplace") : e.vscode && (t = Ye, i = "Install from VSCode Marketplace"), l`
      <div>
        <div>Install the Vaadin IDE Plugin to ensure a smooth development workflow</div>
        <p>
          Installing the plugin is not required, but strongly recommended.<br />Some Vaadin Copilot functionality, such
          as undo, will not function optimally without the plugin.
        </p>
        ${t ? l` <div>
              <vaadin-button
                @click="${() => {
      window.open(t, "_blank");
    }}"
                >${i}
                <vaadin-icon icon="vaadin:external-link"></vaadin-icon>
              </vaadin-button>
            </div>` : u}
      </div>
    `;
  }
  renderHotswapSection(e) {
    const { jdkInfo: t } = r;
    if (!t)
      return u;
    const i = Z(), n = Ve();
    let o, s, a;
    return i === "success" ? (o = d.checkCircle, a = "Java Hotswap is enabled") : i === "warning" ? (o = d.alertTriangle, a = "Java Hotswap is not enabled") : i === "error" && (o = d.alertTriangle, a = "Java Hotswap is partially enabled"), this.hotswapTab === "jrebel" ? t.jrebel ? s = this.renderJRebelInstalledContent() : s = this.renderJRebelNotInstalledContent() : e.intellij ? s = this.renderHotswapAgentPluginContent(this.renderIntelliJHotswapHint) : e.vscode ? s = this.renderHotswapAgentPluginContent(this.renderVSCodeHotswapHint) : s = this.renderHotswapAgentNotInstalledContent(e), l` <details
      part="panel"
      .open=${this.hotswapSectionOpened}
      @toggle=${(p) => {
      me(() => {
        this.hotswapSectionOpened = p.target.open;
      });
    }}>
      <summary part="header">
        <span class="icon ${i}">${o}</span>
        <div>${a}</div>
      </summary>
      <div part="content">
        ${n !== "none" ? l`${n === "jrebel" ? this.renderJRebelInstalledContent() : this.renderHotswapAgentInstalledContent()}` : l`
            <div class="tabs" role="tablist">
              <button
                aria-selected="${this.hotswapTab === "hotswapagent" ? "true" : "false"}"
                class="tab"
                role="tab"
                @click=${() => {
      this.hotswapTab = "hotswapagent";
    }}>
                Hotswap Agent
              </button>
              <button
                aria-selected="${this.hotswapTab === "jrebel" ? "true" : "false"}"
                class="tab"
                role="tab"
                @click=${() => {
      this.hotswapTab = "jrebel";
    }}>
                JRebel
              </button>
            </div>
            <div part="content">${s}</div>
            </div>
            </details>
          `}
      </div>
    </details>`;
  }
  renderJRebelNotInstalledContent() {
    return l`
      <div>
        <a href="https://www.jrebel.com">JRebel ${d.share}</a> is a commercial hotswap solution. Vaadin detects the
        JRebel Agent and automatically reloads the application in the browser after the Java changes have been
        hotpatched.
        <p>
          Go to
          <a href="https://www.jrebel.com/products/jrebel/learn" target="_blank" rel="noopener noreferrer">
            https://www.jrebel.com/products/jrebel/learn ${d.share}</a
          >
          to get started
        </p>
      </div>
    `;
  }
  renderHotswapAgentNotInstalledContent(e) {
    const t = [
      this.renderJavaRunningInDebugModeSection(),
      this.renderHotswapAgentJdkSection(e),
      this.renderInstallHotswapAgentJdkSection(e),
      this.renderHotswapAgentVersionSection(),
      this.renderHotswapAgentMissingArgParam(e)
    ];
    return l` <div part="hotswap-agent-section-container">${t}</div> `;
  }
  renderHotswapAgentPluginContent(e) {
    const i = Z() === "success";
    return l`
      <div part="hotswap-agent-section-container">
        <div class="inner-section">
          <span class="hotswap icon ${i ? "success" : "warning"}"
            >${i ? d.checkCircle : d.alertTriangle}</span
          >
          ${e()}
        </div>
      </div>
    `;
  }
  renderIntelliJHotswapHint() {
    return l` <div class="hint">
      <h3>Use 'Debug using Hotswap Agent' launch configuration</h3>
      Vaadin IntelliJ plugin offers launch mode that does not require any manual configuration!
      <p>
        In order to run recommended launch configuration, you should click three dots right next to Debug button and
        select <code>Debug using Hotswap Agent</code> option.
      </p>
    </div>`;
  }
  renderVSCodeHotswapHint() {
    return l` <div class="hint">
      <h3>Use 'Debug (hotswap)'</h3>
      With Vaadin Visual Studio Code extension you can run Hotswap Agent without any manual configuration required!
      <p>Click <code>Debug (hotswap)</code> within your main class to debug application using Hotswap Agent.</p>
    </div>`;
  }
  renderJavaRunningInDebugModeSection() {
    const e = r.jdkInfo?.runningInJavaDebugMode;
    return l`
      <div class="inner-section">
        <details class="inner" .open="${!e}">
          <summary>
            <span class="icon ${e ? "success" : "warning"}"
              >${e ? d.checkCircle : d.alertTriangle}</span
            >
            <div>Run Java in debug mode</div>
          </summary>
          <div class="hint">Start the application in debug mode in the IDE</div>
        </details>
      </div>
    `;
  }
  renderHotswapAgentMissingArgParam(e) {
    const t = r.jdkInfo?.runningWitHotswap && r.jdkInfo?.runningWithExtendClassDef;
    return l`
      <div class="inner-section">
        <details class="inner" .open="${!t}">
          <summary>
            <span class="icon ${t ? "success" : "warning"}"
              >${t ? d.checkCircle : d.alertTriangle}</span
            >
            <div>Enable HotswapAgent</div>
          </summary>
          <div class="hint">
            <ul>
              ${e.intellij ? l`<li>Launch as mentioned in the previous step</li>` : u}
              ${e.intellij ? l`<li>
                    To manually configure IntelliJ, add the
                    <code>-XX:HotswapAgent=fatjar -XX:+AllowEnhancedClassRedefinition -XX:+UpdateClasses</code> JVM
                    arguments when launching the application
                  </li>` : l`<li>
                    Add the
                    <code>-XX:HotswapAgent=fatjar -XX:+AllowEnhancedClassRedefinition -XX:+UpdateClasses</code> JVM
                    arguments when launching the application
                  </li>`}
            </ul>
          </div>
        </details>
      </div>
    `;
  }
  renderHotswapAgentJdkSection(e) {
    const t = r.jdkInfo?.extendedClassDefCapable, i = this.downloadStatusMessages?.[this.downloadStatusMessages.length - 1] === ye;
    return l`
      <div class="inner-section">
        <details class="inner" .open="${!t}">
          <summary>
            <span class="icon ${t ? "success" : "warning"}"
              >${t ? d.checkCircle : d.alertTriangle}</span
            >
            <div>Run using JetBrains Runtime JDK</div>
          </summary>
          <div class="hint">
            JetBrains Runtime provides much better hotswapping compared to other JDKs.
            <ul>
              ${e.intellij && Pt("1.3.0", r.idePluginState?.version) ? l` <li>Upgrade to the latest IntelliJ plugin</li>` : u}
              ${e.intellij ? l` <li>Launch the application in IntelliJ using "Debug using Hotswap Agent"</li>` : u}
              ${e.vscode ? l` <li>
                    <a href @click="${(n) => this.downloadJetbrainsRuntime(n)}"
                      >Let Copilot download and set up JetBrains Runtime for VS Code</a
                    >
                    ${this.downloadProgress > 0 ? l`<vaadin-progress-bar
                          .value="${this.downloadProgress}"
                          min="0"
                          max="1"></vaadin-progress-bar>` : u}
                    <ul>
                      ${this.downloadStatusMessages.map((n) => l`<li>${n}</li>`)}
                      ${i ? l`<h3>Go to VS Code and launch the 'Debug using Hotswap Agent' configuration</h3>` : u}
                    </ul>
                  </li>` : u}
              <li>
                ${e.intellij || e.vscode ? l`If there is a problem, you can manually
                      <a target="_blank" href="${we}">download JetBrains Runtime JDK</a> and set up
                      your debug configuration to use it.` : l`<a target="_blank" href="${we}">Download JetBrains Runtime JDK</a> and set up
                      your debug configuration to use it.`}
              </li>
            </ul>
          </div>
        </details>
      </div>
    `;
  }
  renderInstallHotswapAgentJdkSection(e) {
    const t = r.jdkInfo?.hotswapAgentFound, i = r.jdkInfo?.extendedClassDefCapable;
    return l`
      <div class="inner-section">
        <details class="inner" .open="${!t}">
          <summary>
            <span class="icon ${t ? "success" : "warning"}"
              >${t ? d.checkCircle : d.alertTriangle}</span
            >
            <div>Install HotswapAgent</div>
          </summary>
          <div class="hint">
            Hotswap Agent provides application level support for hot reloading, such as reinitalizing Vaadin @Route or
            @BrowserCallable classes when they are updated
            <ul>
              ${e.intellij ? l`<li>Launch as mentioned in the previous step</li>` : u}
              ${!e.intellij && !i ? l`<li>First install JetBrains Runtime as mentioned in the step above.</li>` : u}
              ${e.intellij ? l`<li>
                    To manually configure IntelliJ, download HotswapAgent and install the jar file as
                    <code>[JAVA_HOME]/lib/hotswap/hotswap-agent.jar</code> in the JetBrains Runtime JDK. Note that the
                    file must be renamed to exactly match this path.
                  </li>` : l`<li>
                    Download HotswapAgent and install the jar file as
                    <code>[JAVA_HOME]/lib/hotswap/hotswap-agent.jar</code> in the JetBrains Runtime JDK. Note that the
                    file must be renamed to exactly match this path.
                  </li>`}
            </ul>
          </div>
        </details>
      </div>
    `;
  }
  renderHotswapAgentVersionSection() {
    if (!r.jdkInfo?.hotswapAgentFound)
      return u;
    const e = r.jdkInfo?.hotswapVersionOk, t = r.jdkInfo?.hotswapVersion, i = r.jdkInfo?.hotswapAgentLocation;
    return l`
      <div class="inner-section">
        <details class="inner" .open="${!e}">
          <summary>
            <span class="icon ${e ? "success" : "warning"}"
              >${e ? d.checkCircle : d.alertTriangle}</span
            >
            <div>Hotswap version requires update</div>
          </summary>
          <div class="hint">
            HotswapAgent version ${t} is in use
            <a target="_blank" href="https://github.com/HotswapProjects/HotswapAgent/releases"
              >Download the latest HotswapAgent</a
            >
            and place it in <code>${i}</code>
          </div>
        </details>
      </div>
    `;
  }
  renderJRebelInstalledContent() {
    return l` <div>JRebel is in use. Enjoy your awesome development workflow!</div> `;
  }
  renderHotswapAgentInstalledContent() {
    return l` <div>Hotswap agent is in use. Enjoy your awesome development workflow!</div> `;
  }
  async downloadJetbrainsRuntime(e) {
    return e.target.disabled = !0, e.preventDefault(), this.downloadStatusMessages = [], ce(`${qe}set-up-vs-code-hotswap`, {}, (t) => {
      t.data.error ? (Fe("Error downloading JetBrains runtime", t.data.error), this.downloadStatusMessages = [...this.downloadStatusMessages, "Download failed"]) : this.downloadStatusMessages = [...this.downloadStatusMessages, ye];
    });
  }
  downloadStatusUpdate(e) {
    const t = e.detail.progress;
    t ? this.downloadProgress = t : this.downloadStatusMessages = [...this.downloadStatusMessages, e.detail.message];
  }
};
w.NAME = "copilot-development-setup-user-guide";
w.styles = L`
    :host {
      --icon-size: 24px;
      --summary-header-gap: 10px;
      --footer-height: calc(50px + var(--space-150));
      color: var(--color);
    }
    :host code {
      background-color: var(--gray-50);
      font-size: var(--font-size-0);
      display: inline-block;
      margin-top: var(--space-100);
      margin-bottom: var(--space-100);
      user-select: all;
    }

    [part='container'] {
      display: flex;
      flex-direction: column;
      gap: var(--space-150);
      padding: var(--space-150);
      box-sizing: border-box;
      height: calc(100% - var(--footer-height));
      overflow: auto;
    }

    [part='footer'] {
      display: flex;
      justify-content: flex-end;
      height: var(--footer-height);
      padding-left: var(--space-150);
      padding-right: var(--space-150);
    }
    [part='hotswap-agent-section-container'] {
      display: flex;
      flex-direction: column;
      gap: var(--space-100);
      position: relative;
    }
    [part='content'] {
      display: flex;
      padding: var(--space-150);
      flex-direction: column;
    }
    div.inner-section div.hint {
      margin-left: calc(var(--summary-header-gap) + var(--icon-size));
      margin-top: var(--space-75);
    }
    details {
      display: flex;
      flex-direction: column;
      box-sizing: border-box;

      & span.icon {
        width: var(--icon-size);
        height: var(--icon-size);
      }
      & span.icon.warning {
        color: var(--lumo-warning-color);
      }
      & span.icon.success {
        color: var(--lumo-success-color);
      }
      & span.hotswap.icon {
        position: absolute;
        top: var(--space-75);
        left: var(--space-75);
      }
      & > summary,
      summary::part(header) {
        display: flex;
        flex-direction: row;
        align-items: center;
        cursor: pointer;
        position: relative;
        gap: var(--summary-header-gap);
        font: var(--font);
      }
      summary::after,
      summary::part(header)::after {
        content: '';
        display: block;
        width: 4px;
        height: 4px;
        border-color: var(--color);
        opacity: var(--panel-toggle-opacity, 0.2);
        border-width: 2px;
        border-style: solid solid none none;
        transform: rotate(var(--panel-toggle-angle, -45deg));
        position: absolute;
        right: 15px;
        top: calc(50% - var(--panel-toggle-offset, 2px));
      }
      &:not([open]) {
        --panel-toggle-angle: 135deg;
        --panel-toggle-offset: 4px;
      }
    }
    details[part='panel'] {
      background: var(--card-bg);
      border: var(--card-border);
      border-radius: 4px;
      user-select: none;

      &:has(summary:hover) {
        border-color: var(--accent-color);
      }

      & > summary,
      summary::part(header) {
        padding: 10px 10px;
        padding-right: 25px;
      }

      summary:hover,
      summary::part(header):hover {
        --panel-toggle-opacity: 0.5;
      }

      input[type='checkbox'],
      summary::part(checkbox) {
        margin: 0;
      }

      &:not([open]):hover {
        background: var(--card-hover-bg);
      }

      &[open] {
        background: var(--card-open-bg);
        box-shadow: var(--card-open-shadow);

        & > summary {
          font-weight: bold;
        }
      }
      .tabs {
        border-bottom: 1px solid var(--border-color);
        box-sizing: border-box;
        display: flex;
        height: 2.25rem;
      }

      .tab {
        background: none;
        border: none;
        border-bottom: 1px solid transparent;
        color: var(--color);
        font: var(--font-button);
        height: 2.25rem;
        padding: 0 0.75rem;
      }

      .tab[aria-selected='true'] {
        color: var(--color-high-contrast);
        border-bottom-color: var(--color-high-contrast);
      }

      .tab-content {
        flex: 1 1 auto;
        gap: var(--space-150);
        overflow: auto;
        padding: var(--space-150);
      }

      h3 {
        margin-top: 0;
      }
    }
  `;
H([
  C()
], w.prototype, "javaPluginSectionOpened", 2);
H([
  C()
], w.prototype, "hotswapSectionOpened", 2);
H([
  C()
], w.prototype, "hotswapTab", 2);
H([
  C()
], w.prototype, "downloadStatusMessages", 2);
H([
  C()
], w.prototype, "downloadProgress", 2);
w = H([
  y(w.NAME)
], w);
const ue = Be({
  header: "Development Workflow",
  tag: Je,
  width: 800,
  height: 800,
  floatingPosition: {
    top: 50,
    left: 50
  },
  individual: !0
}), At = {
  init(e) {
    e.addPanel(ue);
  }
};
window.Vaadin.copilot.plugins.push(At);
c.addPanel(ue);
var It = Object.getOwnPropertyDescriptor, Ct = (e, t, i, n) => {
  for (var o = n > 1 ? void 0 : n ? It(t, i) : t, s = e.length - 1, a; s >= 0; s--)
    (a = e[s]) && (o = a(o) || o);
  return o;
};
let xe = class extends I {
  createRenderRoot() {
    return this;
  }
  connectedCallback() {
    super.connectedCallback(), this.classList.add("custom-menu-item");
  }
  render() {
    const e = Ee(), t = e.status === "warning" || e.status === "error";
    return l`
      <div aria-hidden="true" class="prefix ${t ? e.status : ""}">${d.lightning}</div>
      <div class="content">
        <span class="label">Development Workflow</span>
        <span class="status ${t ? e.status : ""}">${e.message}</span>
      </div>
      <div aria-hidden="true" class="suffix">
        ${t ? l`<div class="dot ${e.status}"></div>` : u}
      </div>
    `;
  }
};
xe = Ct([
  y("copilot-activation-button-development-workflow")
], xe);
var $t = Object.getOwnPropertyDescriptor, St = (e, t, i, n) => {
  for (var o = n > 1 ? void 0 : n ? $t(t, i) : t, s = e.length - 1, a; s >= 0; s--)
    (a = e[s]) && (o = a(o) || o);
  return o;
};
let Pe = class extends I {
  constructor() {
    super(), this.clickListener = this.getClickListener(), this.reaction(
      () => r.userInfo,
      () => {
        this.requestUpdate();
      }
    );
  }
  createRenderRoot() {
    return this;
  }
  connectedCallback() {
    super.connectedCallback(), this.classList.add("custom-menu-item"), this.addEventListener("click", this.clickListener);
  }
  disconnectedCallback() {
    super.disconnectedCallback(), this.removeEventListener("click", this.clickListener);
  }
  render() {
    const e = this.getStatus();
    return l`
      <div class="prefix">${this.renderPortrait()}</div>
      <div class="content">
        <span class="label"> ${this.getUsername()} </span>
        ${e ? l` <span class="warning"> ${e} </span> ` : u}
      </div>
      <div aria-hidden="true" class="suffix">${this.renderDot()}</div>
    `;
  }
  getClickListener() {
    return r.userInfo?.validLicense ? () => window.open("https://vaadin.com/myaccount", "_blank", "noopener") : () => r.setLoginCheckActive(!0);
  }
  getUsername() {
    return r.userInfo?.firstName ? `${r.userInfo.firstName} ${r.userInfo.lastName}` : "Log in";
  }
  getStatus() {
    if (r.userInfo?.validLicense)
      return r.userInfo?.copilotProjectCannotLeaveLocalhost ? "AI Disabled" : void 0;
    if ($.active) {
      const e = Math.round($.remainingTimeInMillis / 864e5);
      return `Preview expires in ${e}${e === 1 ? " day" : " days"}`;
    }
    if ($.expired && !r.userInfo?.validLicense)
      return "Preview expired";
    if (!$.active && !$.expired && !r.userInfo?.validLicense)
      return "No valid license available";
  }
  renderPortrait() {
    return r.userInfo?.portraitUrl ? l`<div
        class="portrait"
        style="background-image: url('https://vaadin.com${r.userInfo.portraitUrl}')"></div>` : u;
  }
  renderDot() {
    return r.userInfo?.validLicense ? u : $.active || $.expired ? l`<div class="dot warning"></div>` : u;
  }
};
Pe = St([
  y("copilot-activation-button-user-info")
], Pe);
function f(e) {
  return Oe("vaadin-menu-bar-item", e);
}
function ee(e) {
  return Oe("vaadin-context-menu-item", e);
}
function Oe(e, t) {
  const i = document.createElement(e);
  if (t.style && (i.className = t.style), t.icon)
    if (typeof t.icon == "string") {
      const n = document.createElement("vaadin-icon");
      n.setAttribute("icon", t.icon), i.append(n);
    } else
      i.append(Ae(t.icon.strings[0]));
  if (t.label) {
    const n = document.createElement("span");
    n.className = "label", n.innerHTML = t.label, i.append(n);
  } else if (t.component) {
    const n = We(t.component) ? t.component : document.createElement(t.component);
    i.append(n);
  }
  if (t.hint) {
    const n = document.createElement("span");
    n.className = "hint", n.innerHTML = t.hint, i.append(n);
  }
  if (t.suffix)
    if (typeof t.suffix == "string") {
      const n = document.createElement("span");
      n.innerHTML = t.suffix, i.append(n);
    } else
      i.append(Ae(t.suffix.strings[0]));
  return i;
}
function Ae(e) {
  if (!e) return null;
  const t = document.createElement("template");
  t.innerHTML = e;
  const i = t.content.children;
  return i.length === 1 ? i[0] : i;
}
function Te(e) {
  return ce("copilot-switch-user", { username: e }, (t) => t.data.error ? (M({ type: _.ERROR, message: "Unable to switch user", details: t.data.error.message }), !1) : !0);
}
var kt = Object.defineProperty, Dt = Object.getOwnPropertyDescriptor, U = (e, t, i, n) => {
  for (var o = n > 1 ? void 0 : n ? Dt(t, i) : t, s = e.length - 1, a; s >= 0; s--)
    (a = e[s]) && (o = (n ? a(t, i, o) : a(o)) || o);
  return n && o && kt(t, i, o), o;
};
const Rt = 8;
function Et() {
  const e = document.createElement("vaadin-text-field");
  return e.label = "Username to Switch To", e.style.width = "100%", e.autocomplete = "off", e.addEventListener("click", async (t) => {
    t.stopPropagation();
  }), e.addEventListener("keydown", async (t) => {
    if (t.stopPropagation(), t.key === "Enter") {
      const i = e.value;
      await Te(i) && (h.addRecentSwitchedUsername(i), window.location.reload());
    }
  }), e;
}
let se = class extends I {
  constructor() {
    super(...arguments), this.username = "";
  }
  connectedCallback() {
    super.connectedCallback(), this.style.display = "contents";
  }
  render() {
    return l`<span style="flex: 1;  display: flex; justify-content: space-between;"
      ><span>${this.username}</span
      ><span
        @click=${(e) => {
      h.removeRecentSwitchedUsername(this.username), e.stopPropagation();
      const t = this.parentElement;
      if (t.style.display = "none", h.getRecentSwitchedUsernames().length === 0) {
        const i = t.parentElement?.firstElementChild;
        i && (i.style.display = "none");
      }
    }}
        >${d.trash}</span
      ></span
    >`;
  }
};
U([
  R({ type: String })
], se.prototype, "username", 2);
se = U([
  y("copilot-switch-user")
], se);
function zt(e) {
  const t = document.createElement("copilot-switch-user");
  return t.username = e, t;
}
let V = class extends I {
  constructor() {
    super(...arguments), this.initialMouseDownPosition = null, this.dragging = !1, this.items = [], this.mouseDownListener = (e) => {
      this.initialMouseDownPosition = { x: e.clientX, y: e.clientY }, P.draggingStarts(this, e), document.addEventListener("mousemove", this.documentDraggingMouseMoveEventListener);
    }, this.documentDraggingMouseMoveEventListener = (e) => {
      if (this.initialMouseDownPosition && !this.dragging) {
        const { clientX: t, clientY: i } = e;
        this.dragging = Math.abs(t - this.initialMouseDownPosition.x) + Math.abs(i - this.initialMouseDownPosition.y) > Rt;
      }
      this.dragging && (this.setOverlayVisibility(!1), P.dragging(this, e));
    }, this.documentMouseUpListener = (e) => {
      if (this.initialMouseDownPosition = null, document.removeEventListener("mousemove", this.documentDraggingMouseMoveEventListener), this.dragging) {
        const t = P.dragging(this, e);
        h.setActivationButtonPosition(t), this.setOverlayVisibility(!0);
      } else
        this.setMenuBarOnClick();
      this.dragging = !1;
    }, this.closeMenuMouseMoveListener = (e) => {
      e.composedPath().some((n) => {
        if (n instanceof HTMLElement) {
          const o = n;
          if (o.localName === this.localName || o.localName === "vaadin-menu-bar-overlay" && o.classList.contains("activation-button-menu"))
            return !0;
        }
        return this.checkPointerIsInRangeInSurroundingRectangle(e);
      }) ? this.closeMenuWithDebounce.clear() : this.closeMenuWithDebounce();
    }, this.closeMenuWithDebounce = G(() => {
      this.closeMenu();
    }, 250), this.checkPointerIsInRangeInSurroundingRectangle = (e) => {
      const i = document.querySelector("copilot-main")?.shadowRoot?.querySelectorAll("vaadin-menu-bar-overlay.activation-button-menu"), n = this.menubar;
      return i ? Array.from(i).some((o) => {
        const s = o.querySelector("vaadin-menu-bar-list-box");
        if (!s)
          return !1;
        const a = s.getBoundingClientRect(), p = n.getBoundingClientRect(), m = Math.min(a.x, p.x), v = Math.min(a.y, p.y), x = Math.max(a.width, p.width), N = a.height + p.height;
        return De(new DOMRect(m, v, x, N), e.clientX, e.clientY);
      }) : !1;
    }, this.dispatchSpotlightActivationEvent = (e) => {
      this.dispatchEvent(
        new CustomEvent("spotlight-activation-changed", {
          detail: e
        })
      );
    }, this.activationBtnClicked = (e) => {
      if (r.active && this.handleAttentionRequiredOnClick()) {
        e?.stopPropagation(), e?.preventDefault();
        return;
      }
      e?.stopPropagation(), this.dispatchEvent(new CustomEvent("activation-btn-clicked"));
    }, this.handleAttentionRequiredOnClick = () => {
      const e = c.getAttentionRequiredPanelConfiguration();
      return e ? e.panel && !e.floating ? (g.emit("open-attention-required-drawer", null), !0) : (c.clearAttention(), !0) : !1;
    }, this.closeMenu = () => {
      this.menubar._close(), document.removeEventListener("mousemove", this.closeMenuMouseMoveListener);
    }, this.setMenuBarOnClick = () => {
      const e = this.shadowRoot.querySelector("vaadin-menu-bar-button");
      e && (e.onclick = this.activationBtnClicked);
    };
  }
  static get styles() {
    return [
      S(K),
      L`
        :host {
          --space: 8px;
          --height: 28px;
          --width: 28px;
          position: absolute;
          top: clamp(var(--space), var(--top), calc(100vh - var(--height) - var(--space)));
          left: clamp(var(--space), var(--left), calc(100vw - var(--width) - var(--space)));
          bottom: clamp(var(--space), var(--bottom), calc(100vh - var(--height) - var(--space)));
          right: clamp(var(--space), var(--right), calc(100vw - var(--width) - var(--space)));
          user-select: none;
          -ms-user-select: none;
          -moz-user-select: none;
          -webkit-user-select: none;
          --indicator-color: var(--red);
          /* Don't add a z-index or anything else that creates a stacking context */
        }

        :host .menu-button {
          min-width: unset;
        }

        :host([document-hidden]) {
          -webkit-filter: grayscale(100%); /* Chrome, Safari, Opera */
          filter: grayscale(100%);
        }

        .menu-button::part(container) {
          overflow: visible;
        }

        .menu-button vaadin-menu-bar-button {
          all: initial;
          display: block;
          position: relative;
          z-index: var(--z-index-activation-button);
          width: var(--width);
          height: var(--height);
          overflow: hidden;
          color: transparent;
          background: hsl(0 0% 0% / 0.25);
          border-radius: 8px;
          box-shadow: 0 0 0 1px hsl(0 0% 100% / 0.1);
          cursor: default;
          -webkit-backdrop-filter: blur(8px);
          backdrop-filter: blur(8px);
          transition:
            box-shadow 0.2s,
            background-color 0.2s;
        }

        /* visual effect when active */

        .menu-button vaadin-menu-bar-button::before {
          all: initial;
          content: '';
          background-image:
            radial-gradient(circle at 50% -10%, hsl(221 100% 55% / 0.6) 0%, transparent 60%),
            radial-gradient(circle at 25% 40%, hsl(303 71% 64%) 0%, transparent 70%),
            radial-gradient(circle at 80% 10%, hsla(262, 38%, 9%, 0.5) 0%, transparent 80%),
            radial-gradient(circle at 110% 50%, hsla(147, 100%, 77%, 1) 20%, transparent 100%);
          animation: 5s swirl linear infinite;
          animation-play-state: paused;
          inset: -6px;
          opacity: 0;
          position: absolute;
          transition: opacity 0.5s;
        }

        /* vaadin symbol */

        .menu-button vaadin-menu-bar-button::after {
          all: initial;
          content: '';
          position: absolute;
          inset: 1px;
          background: url('data:image/svg+xml;utf8,<svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M12.7407 9.70401C12.7407 9.74417 12.7378 9.77811 12.7335 9.81479C12.7111 10.207 12.3897 10.5195 11.9955 10.5195C11.6014 10.5195 11.2801 10.209 11.2577 9.8169C11.2534 9.7801 11.2504 9.74417 11.2504 9.70401C11.2504 9.31225 11.1572 8.90867 10.2102 8.90867H7.04307C5.61481 8.90867 5 8.22698 5 6.86345V5.70358C5 5.31505 5.29521 5 5.68008 5C6.06495 5 6.35683 5.31505 6.35683 5.70358V6.09547C6.35683 6.53423 6.655 6.85413 7.307 6.85413H10.4119C11.8248 6.85413 11.9334 7.91255 11.98 8.4729H12.0111C12.0577 7.91255 12.1663 6.85413 13.5791 6.85413H16.6841C17.3361 6.85413 17.614 6.54529 17.614 6.10641L17.6158 5.70358C17.6158 5.31505 17.9246 5 18.3095 5C18.6943 5 19 5.31505 19 5.70358V6.86345C19 8.22698 18.3763 8.90867 16.9481 8.90867H13.7809C12.8338 8.90867 12.7407 9.31225 12.7407 9.70401Z" fill="white"/><path d="M12.7536 17.7785C12.6267 18.0629 12.3469 18.2608 12.0211 18.2608C11.6907 18.2608 11.4072 18.0575 11.2831 17.7668C11.2817 17.7643 11.2803 17.7619 11.279 17.7595C11.2761 17.7544 11.2732 17.7495 11.2704 17.744L8.45986 12.4362C8.3821 12.2973 8.34106 12.1399 8.34106 11.9807C8.34106 11.4732 8.74546 11.0603 9.24238 11.0603C9.64162 11.0603 9.91294 11.2597 10.0985 11.6922L12.0216 15.3527L13.9468 11.6878C14.1301 11.2597 14.4014 11.0603 14.8008 11.0603C15.2978 11.0603 15.7021 11.4732 15.7021 11.9807C15.7021 12.1399 15.6611 12.2973 15.5826 12.4374L12.7724 17.7446C12.7683 17.7524 12.7642 17.7597 12.7601 17.767C12.7579 17.7708 12.7557 17.7746 12.7536 17.7785Z" fill="white"/></svg>');
          background-size: 100%;
        }

        .menu-button vaadin-menu-bar-button[focus-ring] {
          outline: 2px solid var(--selection-color);
          outline-offset: 2px;
        }

        .menu-button vaadin-menu-bar-button:hover {
          background: hsl(0 0% 0% / 0.8);
          box-shadow:
            0 0 0 1px hsl(0 0% 100% / 0.1),
            0 2px 8px -1px hsl(0 0% 0% / 0.3);
        }

        :host([active]) .menu-button vaadin-menu-bar-button {
          background-color: transparent;
          box-shadow:
            inset 0 0 0 1px hsl(0 0% 0% / 0.2),
            0 2px 8px -1px hsl(0 0% 0% / 0.3);
        }

        :host([active]) .menu-button vaadin-menu-bar-button::before {
          opacity: 1;
          animation-play-state: running;
        }

        :host([attention-required]) {
          animation: bounce 0.5s;
          animation-iteration-count: 2;
        }

        [part='indicator'] {
          top: -6px;
          right: -6px;
          width: 12px;
          height: 12px;
          box-sizing: border-box;
          border-radius: 100%;
          position: absolute;
          display: var(--indicator-display, none);
          background: var(--indicator-color);
          z-index: calc(var(--z-index-activation-button) + 1);
          border: 2px solid var(--indicator-border);
        }

        :host([indicator='warning']) {
          --indicator-display: block;
          --indicator-color: var(--yellow);
        }

        :host([indicator='error']) {
          --indicator-display: block;
          --indicator-color: var(--red);
        }
      `
    ];
  }
  connectedCallback() {
    super.connectedCallback(), this.reaction(
      () => c.attentionRequiredPanelTag,
      () => {
        this.toggleAttribute(A, c.attentionRequiredPanelTag !== null), this.updateIndicator();
      }
    ), this.reaction(
      () => r.active,
      () => {
        this.toggleAttribute("active", r.active);
      },
      { fireImmediately: !0 }
    ), this.addEventListener("mousedown", this.mouseDownListener), document.addEventListener("mouseup", this.documentMouseUpListener);
    const e = h.getActivationButtonPosition();
    e ? (this.style.setProperty("--left", `${e.left}px`), this.style.setProperty("--bottom", `${e.bottom}px`), this.style.setProperty("--right", `${e.right}px`), this.style.setProperty("--top", `${e.top}px`)) : (this.style.setProperty("--bottom", "var(--space)"), this.style.setProperty("--right", "var(--space)")), g.on("document-activation-change", (t) => {
      this.toggleAttribute("document-hidden", !t.detail.active);
    }), this.reaction(
      () => [r.jdkInfo, r.idePluginState],
      () => {
        this.updateIndicator();
      }
    ), this.reaction(
      () => [
        r.active,
        r.idePluginState,
        h.isActivationAnimation(),
        h.isActivationShortcut(),
        h.isSendErrorReportsAllowed(),
        h.isAIUsageAllowed(),
        h.getDismissedNotifications()
      ],
      () => {
        this.generateItems();
      }
    );
  }
  disconnectedCallback() {
    super.disconnectedCallback(), this.removeEventListener("mousedown", this.mouseDownListener), document.removeEventListener("mouseup", this.documentMouseUpListener);
  }
  updateIndicator() {
    if (this.hasAttribute(A)) {
      this.setAttribute("indicator", "error");
      return;
    }
    const e = Ee();
    e.status !== "success" ? this.setAttribute("indicator", e.status) : this.removeAttribute("indicator");
  }
  /**
   * To hide overlay while dragging
   * @param visible
   */
  setOverlayVisibility(e) {
    const t = this.shadowRoot.querySelector("vaadin-menu-bar-button").__overlay;
    e ? (t?.style.setProperty("display", "flex"), t?.style.setProperty("visibility", "visible")) : (t?.style.setProperty("display", "none"), t?.style.setProperty("visibility", "invisible"));
  }
  generateItems() {
    const e = r.active, t = e && !!r.idePluginState?.supportedActions?.find((o) => o === "undo"), i = [];
    if (E.springSecurityEnabled) {
      const o = h.getRecentSwitchedUsernames();
      i.push(
        ...o.map((s) => ({
          component: f({ component: zt(s) }),
          action: async () => {
            await Te(s) && window.location.reload();
          }
        }))
      ), i.length > 0 && i.unshift({
        component: f({ label: "Recently Used Usernames" }),
        disabled: !0
      });
    }
    const n = [
      {
        text: "Vaadin Copilot",
        children: [
          { visible: e, component: f({ component: "copilot-activation-button-user-info" }) },
          { visible: e, component: "hr" },
          {
            component: f({ component: "copilot-activation-button-development-workflow" }),
            action: Ge
          },
          { visible: e, component: "hr" },
          {
            visible: E.springSecurityEnabled,
            component: f({
              icon: d.user,
              label: "Application's User"
            }),
            children: [
              ...i,
              {
                component: f({ component: Et() })
              }
            ]
          },
          {
            component: "hr",
            visible: e
          },
          {
            visible: t,
            component: f({
              icon: d.flipBack,
              label: "Undo",
              hint: q.undo
            }),
            action: () => {
              g.emit("undoRedo", { undo: !0 });
            }
          },
          {
            visible: t,
            component: f({
              icon: d.flipForward,
              label: "Redo",
              hint: q.redo
            }),
            action: () => {
              g.emit("undoRedo", { undo: !1 });
            }
          },
          {
            component: f({
              icon: d.starsAlt,
              label: "Toggle Command Window",
              hint: q.toggleCommandWindow,
              style: "toggle-spotlight"
            }),
            action: () => {
              r.setSpotlightActive(!r.spotlightActive);
            }
          },
          {
            component: "hr",
            visible: e
          },
          {
            visible: e,
            component: f({
              icon: d.settings,
              label: "Settings"
            }),
            children: [
              {
                component: f({
                  icon: d.keyboard,
                  label: "Activation Shortcut",
                  suffix: h.isActivationShortcut() ? '<div aria-hidden="true" class="switch on"></div>' : '<div aria-hidden="true" class="switch off"></div>'
                }),
                keepOpen: !0,
                action: (o) => {
                  h.setActivationShortcut(!h.isActivationShortcut()), te(o, h.isActivationShortcut());
                }
              },
              {
                component: f({
                  icon: d.play,
                  label: "Activation Animation",
                  suffix: h.isActivationAnimation() ? '<div aria-hidden="true" class="switch on"></div>' : '<div aria-hidden="true" class="switch off"></div>'
                }),
                keepOpen: !0,
                action: (o) => {
                  h.setActivationAnimation(!h.isActivationAnimation()), te(o, h.isActivationAnimation());
                }
              },
              {
                component: f({
                  icon: d.starsAlt,
                  label: "AI Usage",
                  hint: He()
                }),
                keepOpen: !0,
                action: (o) => {
                  let s;
                  const a = h.isAIUsageAllowed();
                  a === "ask" ? s = "yes" : a === "no" ? s = "ask" : s = "no", h.setAIUsageAllowed(s), Mt(o);
                }
              },
              {
                component: f({
                  icon: d.alertCircle,
                  label: "Report Errors to Vaadin",
                  suffix: h.isSendErrorReportsAllowed() ? '<div aria-hidden="true" class="switch on"></div>' : '<div aria-hidden="true" class="switch off"></div>'
                }),
                keepOpen: !0,
                action: (o) => {
                  h.setSendErrorReportsAllowed(!h.isSendErrorReportsAllowed()), te(o, h.isSendErrorReportsAllowed());
                }
              },
              { component: "hr" },
              {
                visible: e,
                component: f({
                  icon: d.annotation,
                  label: "Show Welcome Message"
                }),
                keepOpen: !0,
                action: () => {
                  r.setWelcomeActive(!0), r.setSpotlightActive(!0);
                }
              },
              {
                visible: e,
                component: f({
                  icon: d.keyboard,
                  label: "Show Keyboard Shortcuts"
                }),
                action: () => {
                  c.updatePanel("copilot-shortcuts-panel", {
                    floating: !0
                  });
                }
              },
              {
                visible: h.getDismissedNotifications().length > 0,
                component: f({
                  icon: d.annotationX,
                  label: "Clear Dismissed Notifications"
                }),
                action: () => {
                  h.clearDismissedNotifications();
                }
              }
            ]
          },
          { component: "hr" },
          {
            visible: e,
            component: f({
              icon: d.annotation,
              label: "Tell Us What You Think"
              // Label used also in ScreenshotsIT.java
            }),
            action: () => {
              c.updatePanel("copilot-feedback-panel", {
                floating: !0
              });
            }
          },
          {
            component: f({
              icon: d.vaadinLogo,
              label: "Copilot",
              hint: h.isActivationShortcut() ? q.toggleCopilot : void 0,
              suffix: r.active ? '<div aria-hidden="true" class="switch on"></div>' : '<div aria-hidden="true" class="switch off"></div>'
            }),
            action: () => {
              this.activationBtnClicked();
            }
          }
        ]
      }
    ];
    this.items = n.filter(Ke);
  }
  render() {
    return l`
      <vaadin-menu-bar
        class="menu-button"
        .items="${this.items}"
        ._menuItemsChanged=${function(e, t, i) {
      if (!t || !i)
        return;
      const n = this;
      e !== n._oldItems && (n._oldItems = e, n.__renderButtons(e));
    }}
        @item-selected="${(e) => {
      this.handleMenuItemClick(e.detail.value);
    }}"
        ?open-on-hover=${!this.dragging}
        @mouseenter="${() => {
      document.addEventListener("mousemove", this.closeMenuMouseMoveListener, { once: !0 });
    }}"
        overlay-class="activation-button-menu">
      </vaadin-menu-bar>
      <div part="indicator"></div>
    `;
  }
  handleMenuItemClick(e) {
    e.action && (e.action(e), e.keepOpen && Lt());
  }
  firstUpdated() {
    this.setMenuBarOnClick(), he(this.shadowRoot);
  }
};
U([
  O("vaadin-menu-bar")
], V.prototype, "menubar", 2);
U([
  C()
], V.prototype, "dragging", 2);
U([
  C()
], V.prototype, "items", 2);
V = U([
  y("copilot-activation-button")
], V);
function Lt() {
  document.querySelector("copilot-main").shadowRoot.querySelectorAll("vaadin-menu-bar-overlay").forEach((e) => {
    e.positionTarget = void 0;
  });
}
function te(e, t) {
  const i = e.component;
  if (!i || typeof i == "string") {
    console.error("Unable to set switch value for a non-component item");
    return;
  }
  const n = i.querySelector(".switch");
  if (!n) {
    console.error("No element found when setting switch value");
    return;
  }
  t ? (n.classList.remove("off"), n.classList.add("on")) : (n.classList.add("off"), n.classList.remove("on"));
}
function Mt(e) {
  const t = e.component;
  if (!t || typeof t == "string") {
    console.error("Unable to set switch value for a non-component item");
    return;
  }
  const i = t.querySelector(".hint");
  if (!i) {
    console.error("No element found when setting switch value");
    return;
  }
  i.innerText = He();
}
function He() {
  return h.isAIUsageAllowed() === "ask" ? "Always Ask" : h.isAIUsageAllowed() === "no" ? "Disabled" : "Enabled";
}
var _t = Object.defineProperty, Ot = Object.getOwnPropertyDescriptor, j = (e, t, i, n) => {
  for (var o = n > 1 ? void 0 : n ? Ot(t, i) : t, s = e.length - 1, a; s >= 0; s--)
    (a = e[s]) && (o = (n ? a(t, i, o) : a(o)) || o);
  return n && o && _t(t, i, o), o;
};
const b = "resize-dir", ie = "floating-resizing-active";
let D = class extends I {
  constructor() {
    super(...arguments), this.panelTag = "", this.dockingItems = [
      {
        component: ee({
          icon: d.layoutRight,
          label: "Dock right"
        }),
        panel: "right"
      },
      {
        component: ee({
          icon: d.layoutLeft,
          label: "Dock left"
        }),
        panel: "left"
      },
      {
        component: ee({
          icon: d.layoutBottom,
          label: "Dock bottom"
        }),
        panel: "bottom"
      }
    ], this.floatingResizingStarted = !1, this.resizingInDrawerStarted = !1, this.toggling = !1, this.rectangleBeforeResizing = null, this.floatingResizeHandlerMouseMoveListener = (e) => {
      if (!this.panelInfo?.floating || this.floatingResizingStarted || !this.panelInfo?.expanded)
        return;
      const t = this.getBoundingClientRect(), i = Math.abs(e.clientX - t.x), n = Math.abs(t.x + t.width - e.clientX), o = Math.abs(e.clientY - t.y), s = Math.abs(t.y + t.height - e.clientY), a = Number.parseInt(
        window.getComputedStyle(this).getPropertyValue("--floating-offset-resize-threshold"),
        10
      );
      let p = "";
      i < a ? o < a ? (p = "nw-resize", this.setAttribute(b, "top left")) : s < a ? (p = "sw-resize", this.setAttribute(b, "bottom left")) : (p = "col-resize", this.setAttribute(b, "left")) : n < a ? o < a ? (p = "ne-resize", this.setAttribute(b, "top right")) : s < a ? (p = "se-resize", this.setAttribute(b, "bottom right")) : (p = "col-resize", this.setAttribute(b, "right")) : s < a ? (p = "row-resize", this.setAttribute(b, "bottom")) : o < a && (p = "row-resize", this.setAttribute(b, "top")), p !== "" ? (this.rectangleBeforeResizing = this.getBoundingClientRect(), this.style.setProperty("--resize-cursor", p)) : (this.style.removeProperty("--resize-cursor"), this.removeAttribute(b)), this.toggleAttribute(ie, p !== "");
    }, this.floatingResizingMouseDownListener = (e) => {
      if (!this.hasAttribute(ie) || e.button !== 0)
        return;
      e.stopPropagation(), e.preventDefault(), P.anchorLeftTop(this), this.floatingResizingStarted = !0, this.toggleAttribute("resizing", !0);
      const t = this.getResizeDirections(), { clientX: i, clientY: n } = e;
      (t.includes("top") || t.includes("bottom")) && this.style.setProperty("--section-height", null), t.forEach((o) => this.setResizePosition(o, i, n)), r.setSectionPanelResizing(!0);
    }, this.floatingResizingMouseLeaveListener = () => {
      this.panelInfo?.floating && (this.floatingResizingStarted || (this.removeAttribute("resizing"), this.removeAttribute(ie), this.removeAttribute("dragging"), this.style.removeProperty("--resize-cursor"), this.removeAttribute(b)));
    }, this.floatingResizingMouseMoveListener = (e) => {
      if (!this.panelInfo?.floating || !this.floatingResizingStarted)
        return;
      e.stopPropagation(), e.preventDefault();
      const t = this.getResizeDirections(), { clientX: i, clientY: n } = e;
      t.forEach((o) => this.setResizePosition(o, i, n));
    }, this.setFloatingResizeDirectionProps = (e, t, i, n) => {
      i && i > Number.parseFloat(window.getComputedStyle(this).getPropertyValue("--min-width")) && (this.style.setProperty(`--${e}`, `${t}px`), this.style.setProperty("width", `${i}px`));
      const o = window.getComputedStyle(this), s = Number.parseFloat(o.getPropertyValue("--header-height")), a = Number.parseFloat(o.getPropertyValue("--floating-offset-resize-threshold")) / 2;
      n && n > s + a && (this.style.setProperty(`--${e}`, `${t}px`), this.style.setProperty("height", `${n}px`), this.container.style.setProperty("margin-top", "calc(var(--floating-offset-resize-threshold) / 4)"), this.container.style.height = `calc(${n}px - var(--floating-offset-resize-threshold) / 2)`);
    }, this.floatingResizingMouseUpListener = (e) => {
      if (!this.floatingResizingStarted || !this.panelInfo?.floating)
        return;
      e.stopPropagation(), e.preventDefault(), this.floatingResizingStarted = !1, r.setSectionPanelResizing(!1);
      const { width: t, height: i } = this.getBoundingClientRect(), { left: n, top: o, bottom: s, right: a } = P.anchor(this), p = window.getComputedStyle(this.container), m = Number.parseInt(p.borderTopWidth, 10), v = Number.parseInt(p.borderBottomWidth, 10);
      c.updatePanel(this.panelInfo.tag, {
        width: t,
        height: i - (m + v),
        floatingPosition: {
          ...this.panelInfo.floatingPosition,
          left: n,
          top: o,
          bottom: s,
          right: a
        }
      }), this.style.removeProperty("width"), this.style.removeProperty("height"), this.container.style.removeProperty("height"), this.container.style.removeProperty("margin-top"), this.setCssSizePositionProperties(), this.toggleAttribute("dragging", !1);
    }, this.transitionEndEventListener = () => {
      this.toggling && (this.toggling = !1, P.anchor(this));
    }, this.sectionPanelMouseEnterListener = () => {
      this.hasAttribute(A) && (this.removeAttribute(A), c.clearAttention());
    }, this.contentAreaMouseDownListener = () => {
      c.bringToFront(this.panelInfo.tag);
    }, this.documentMouseUpEventListener = () => {
      document.removeEventListener("mousemove", this.draggingEventListener), this.panelInfo?.floating && (this.toggleAttribute("dragging", !1), r.setSectionPanelDragging(!1));
    }, this.panelHeaderMouseDownEventListener = (e) => {
      e.button === 0 && (c.bringToFront(this.panelInfo.tag), !this.hasAttribute(b) && (e.target instanceof HTMLButtonElement && e.target.getAttribute("part") === "title-button" ? this.startDraggingDebounce(e) : this.startDragging(e)));
    }, this.panelHeaderMouseUpEventListener = (e) => {
      e.button === 0 && this.startDraggingDebounce.clear();
    }, this.startDragging = (e) => {
      P.draggingStarts(this, e), document.addEventListener("mousemove", this.draggingEventListener), r.setSectionPanelDragging(!0), this.panelInfo?.floating ? this.toggleAttribute("dragging", !0) : this.parentElement.sectionPanelDraggingStarted(this, e), e.preventDefault(), e.stopPropagation();
    }, this.startDraggingDebounce = G(this.startDragging, 200), this.draggingEventListener = (e) => {
      const t = P.dragging(this, e);
      if (this.panelInfo?.floating && this.panelInfo?.floatingPosition) {
        e.preventDefault();
        const { left: i, top: n, bottom: o, right: s } = t;
        c.updatePanel(this.panelInfo.tag, {
          floatingPosition: {
            ...this.panelInfo.floatingPosition,
            left: i,
            top: n,
            bottom: o,
            right: s
          }
        });
      }
    }, this.setCssSizePositionProperties = () => {
      const e = c.getPanelByTag(this.panelTag);
      if (e && (e.height !== void 0 && (this.panelInfo?.floating || e.panel === "left" || e.panel === "right" ? this.style.setProperty("--section-height", `${e.height}px`) : this.style.removeProperty("--section-height")), e.width !== void 0 && (e.floating || e.panel === "bottom" ? this.style.setProperty("--section-width", `${e.width}px`) : this.style.removeProperty("--section-width")), e.floating && e.floatingPosition && !this.toggling)) {
        const { left: t, top: i, bottom: n, right: o } = e.floatingPosition;
        this.style.setProperty("--left", t !== void 0 ? `${t}px` : "auto"), this.style.setProperty("--top", i !== void 0 ? `${i}px` : "auto"), this.style.setProperty("--bottom", n !== void 0 ? `${n}px` : ""), this.style.setProperty("--right", o !== void 0 ? `${o}px` : "");
        const s = window.getComputedStyle(this);
        parseInt(s.top, 10) < 0 && this.style.setProperty("--top", "0px"), parseInt(s.bottom, 10) < 0 && this.style.setProperty("--bottom", "0px");
      }
    }, this.renderPopupButton = () => {
      if (!this.panelInfo)
        return u;
      let e;
      return this.panelInfo.panel === void 0 ? e = "Close the popup" : e = this.panelInfo.floating ? `Dock ${this.panelInfo.header} to ${this.panelInfo.panel}` : `Open ${this.panelInfo.header} as a popup`, l`
      <vaadin-context-menu .items=${this.dockingItems} @item-selected="${this.changeDockingPanel}">
        <button
          @click="${(t) => this.changePanelFloating(t)}"
          @mousedown="${(t) => t.stopPropagation()}"
          aria-label=${e}
          class="icon"
          part="popup-button"
          title="${e}">
          ${this.getPopupButtonIcon()}
        </button>
      </vaadin-context-menu>
    `;
    }, this.changePanelFloating = (e) => {
      if (this.panelInfo)
        if (e.stopPropagation(), ve(this), this.panelInfo?.floating)
          c.updatePanel(this.panelInfo.tag, { floating: !1 });
        else {
          let t;
          if (this.panelInfo.floatingPosition)
            t = this.panelInfo.floatingPosition;
          else {
            const { left: o, top: s } = this.getBoundingClientRect();
            t = {
              left: o,
              top: s
            };
          }
          let i = this.panelInfo?.height;
          i === void 0 && this.panelInfo.expanded && (i = Number.parseInt(window.getComputedStyle(this).height, 10)), this.parentElement.forceClose(), c.updatePanel(this.panelInfo.tag, {
            floating: !0,
            expanded: !0,
            width: this.panelInfo?.width || Number.parseInt(window.getComputedStyle(this).width, 10),
            height: i,
            floatingPosition: t
          }), c.bringToFront(this.panelInfo.tag);
        }
    }, this.toggleExpand = (e) => {
      this.panelInfo && (e.stopPropagation(), P.anchorLeftTop(this), c.updatePanel(this.panelInfo.tag, {
        expanded: !this.panelInfo.expanded
      }), this.toggling = !0, this.toggleAttribute("expanded", this.panelInfo.expanded), g.emit("panel-expanded", { panelTag: this.panelInfo.tag, expanded: this.panelInfo.expanded }));
    };
  }
  static get styles() {
    return [
      S(K),
      S(ze),
      L`
        * {
          box-sizing: border-box;
        }

        :host {
          flex: none;
          --min-width: 160px;
          --header-height: 36px;
          --content-width: var(--content-width, 100%);
          --floating-border-width: 1px;
          --floating-offset-resize-threshold: 8px;
          cursor: var(--cursor, var(--resize-cursor, default));
          overflow: hidden;
        }

        :host(:not([expanded])) {
          grid-template-rows: auto 0fr;
        }

        [part='header'] {
          align-items: center;
          color: var(--color-high-contrast);
          display: flex;
          flex: none;
          font: var(--font-small-medium);
          gap: var(--space-50);
          justify-content: space-between;
          min-width: 100%;
          padding: var(--space-50);
          user-select: none;
          -webkit-user-select: none;
          width: var(--min-width);
        }

        :host([floating]) {
          --content-height: calc(var(--section-height));
        }

        :host([floating]:not([expanded])) [part='header'] {
          --min-width: unset;
        }

        :host([floating]) [part='header'] {
          transition: border-color var(--duration-2);
        }

        :host([floating]:not([expanded])) [part='header'] {
          border-color: transparent;
        }

        [part='title'] {
          flex: auto;
          margin: 0;
          overflow: hidden;
          text-overflow: ellipsis;
        }

        [part='title']:first-child {
          margin-inline-start: var(--space-100);
        }

        [part='content'] {
          height: calc(var(--content-height) - var(--header-height));
          overflow: auto;
          transition:
            height var(--duration-2),
            width var(--duration-2),
            opacity var(--duration-2),
            visibility calc(var(--duration-2) * 2);
        }

        :host([floating]) [part='drawer-resize'] {
          display: none;
        }

        :host(:not([expanded])) [part='drawer-resize'] {
          display: none;
        }

        :host(:not([floating]):not(:last-child)) {
          border-bottom: 1px solid var(--divider-primary-color);
        }

        :host(:not([expanded])) [part='content'] {
          opacity: 0;
          visibility: hidden;
        }

        :host([floating]:not([expanded])) [part='content'] {
          width: 0;
          height: 0;
        }

        :host(:not([expanded])) [part='content'][style*='width'] {
          width: 0 !important;
        }

        :host([floating]) {
          position: fixed;
          min-width: 0;
          min-height: 0;
          z-index: calc(var(--z-index-floating-panel) + var(--z-index-focus, 0));
          top: clamp(0px, var(--top), calc(100vh - var(--section-height, var(--header-height)) * 0.5));
          left: clamp(calc(var(--section-width) * -0.5), var(--left), calc(100vw - var(--section-width) * 0.5));
          bottom: clamp(
            calc(var(--section-height, var(--header-height)) * -0.5),
            var(--bottom),
            calc(100vh - var(--section-height, var(--header-height)) * 0.5)
          );
          right: clamp(calc(var(--section-width) * -0.5), var(--right), calc(100vw - var(--section-width) * 0.5));
          width: var(--section-width);
          overflow: visible;
        }
        :host([floating]) [part='container'] {
          background: var(--background-color);
          border: var(--floating-border-width) solid var(--surface-border-color);
          -webkit-backdrop-filter: var(--surface-backdrop-filter);
          backdrop-filter: var(--surface-backdrop-filter);
          border-radius: var(--radius-2);
          margin: auto;
          box-shadow: var(--surface-box-shadow-2);
        }
        [part='container'] {
          overflow: hidden;
        }
        :host([floating][expanded]) [part='container'] {
          height: calc(100% - var(--floating-offset-resize-threshold) / 2);
          width: calc(100% - var(--floating-offset-resize-threshold) / 2);
        }

        :host([floating]:not([expanded])) {
          width: unset;
        }

        :host([floating]) .drag-handle {
          cursor: var(--resize-cursor, move);
        }

        :host([floating][expanded]) [part='content'] {
          min-width: var(--min-width);
          min-height: 0;
          width: var(--content-width);
        }

        /* :hover for Firefox, :active for others */

        :host([floating][expanded]) [part='content']:is(:hover, :active) {
          transition: none;
        }

        [part='title-button'] {
          font: var(--font-xxsmall-bold);
          padding: 0;
          text-align: start;
          text-transform: uppercase;
        }

        @media not (prefers-reduced-motion) {
          [part='toggle-button'] svg {
            transition: transform 0.15s cubic-bezier(0.2, 0, 0, 1);
          }
        }

        [part='toggle-button'][aria-expanded='true'] svg {
          transform: rotate(90deg);
        }

        .actions {
          display: none;
        }

        :host([expanded]) .actions {
          display: contents;
        }

        ::slotted(*) {
          box-sizing: border-box;
          display: block;
          /* padding: var(--space-150); */
          width: 100%;
        }

        /*workaround for outline to have a explicit height while floating by default. 
          may be removed after https://github.com/vaadin/web-components/issues/7620 is solved
        */
        :host([floating][expanded][paneltag='copilot-outline-panel']) {
          --grid-default-height: 400px;
        }

        :host([dragging]) {
          opacity: 0.4;
        }

        :host([dragging]) [part='content'] {
          pointer-events: none;
        }

        :host([hiding-while-drag-and-drop]) {
          display: none;
        }

        // dragging in drawer

        :host(:not([floating])) .drag-handle {
          cursor: grab;
        }

        :host(:not([floating])[dragging]) .drag-handle {
          cursor: grabbing;
        }
      `
    ];
  }
  connectedCallback() {
    super.connectedCallback(), this.setAttribute("role", "region"), this.reaction(
      () => c.getAttentionRequiredPanelConfiguration(),
      () => {
        const e = c.getAttentionRequiredPanelConfiguration();
        this.toggleAttribute(A, e?.tag === this.panelTag && e?.floating);
      }
    ), this.addEventListener("mouseenter", this.sectionPanelMouseEnterListener), this.reaction(
      () => r.operationInProgress,
      () => {
        requestAnimationFrame(() => {
          this.toggleAttribute(
            "hiding-while-drag-and-drop",
            r.operationInProgress === de.DragAndDrop && this.panelInfo?.floating && !this.panelInfo.showWhileDragging
          );
        });
      }
    ), this.reaction(
      () => c.floatingPanelsZIndexOrder,
      () => {
        this.style.setProperty("--z-index-focus", `${c.getFloatingPanelZIndex(this.panelTag)}`);
      },
      { fireImmediately: !0 }
    ), this.addEventListener("transitionend", this.transitionEndEventListener), this.addEventListener("mousemove", this.floatingResizeHandlerMouseMoveListener), this.addEventListener("mousedown", this.floatingResizingMouseDownListener), this.addEventListener("mouseleave", this.floatingResizingMouseLeaveListener), document.addEventListener("mousemove", this.floatingResizingMouseMoveListener), document.addEventListener("mouseup", this.floatingResizingMouseUpListener);
  }
  disconnectedCallback() {
    super.disconnectedCallback(), this.removeEventListener("mouseenter", this.sectionPanelMouseEnterListener), this.removeEventListener("mousemove", this.floatingResizeHandlerMouseMoveListener), this.removeEventListener("mousedown", this.floatingResizingMouseDownListener), document.removeEventListener("mousemove", this.floatingResizingMouseMoveListener), document.removeEventListener("mouseup", this.floatingResizingMouseUpListener);
  }
  setResizePosition(e, t, i) {
    const n = this.rectangleBeforeResizing, o = 0, s = window.innerWidth, a = 0, p = window.innerHeight, m = Math.max(o, Math.min(s, t)), v = Math.max(a, Math.min(p, i));
    if (e === "left")
      this.setFloatingResizeDirectionProps(
        "left",
        m,
        n.left - m + n.width
      );
    else if (e === "right")
      this.setFloatingResizeDirectionProps(
        "right",
        m,
        m - n.right + n.width
      );
    else if (e === "top") {
      const x = n.top - v + n.height;
      this.setFloatingResizeDirectionProps("top", v, void 0, x);
    } else if (e === "bottom") {
      const x = v - n.bottom + n.height;
      this.setFloatingResizeDirectionProps("bottom", v, void 0, x);
    }
  }
  willUpdate(e) {
    super.willUpdate(e), e.has("panelTag") && (this.panelInfo = c.getPanelByTag(this.panelTag), this.setAttribute("aria-labelledby", this.panelInfo.tag.concat("-title"))), this.toggleAttribute("floating", this.panelInfo?.floating);
  }
  updated(e) {
    super.updated(e), this.setCssSizePositionProperties();
  }
  firstUpdated(e) {
    super.firstUpdated(e), document.addEventListener("mouseup", this.documentMouseUpEventListener), this.headerDraggableArea.addEventListener("mousedown", this.panelHeaderMouseDownEventListener), this.headerDraggableArea.addEventListener("mouseup", this.panelHeaderMouseUpEventListener), this.toggleAttribute("expanded", this.panelInfo?.expanded), this.toggleAttribute("individual", this.panelInfo?.individual ?? !1), Ze(this), this.setCssSizePositionProperties(), this.contentArea.addEventListener("mousedown", this.contentAreaMouseDownListener), he(this.shadowRoot);
  }
  render() {
    return this.panelInfo ? l`
      <div part="container">
        <div part="header" class="drag-handle">
          ${this.panelInfo.expandable !== !1 ? l` <button
                @mousedown="${(e) => e.stopPropagation()}"
                @click="${(e) => this.toggleExpand(e)}"
                aria-controls="content"
                aria-expanded="${this.panelInfo.expanded}"
                aria-label="Expand ${this.panelInfo.header}"
                class="icon"
                part="toggle-button">
                <span>${d.chevronRight}</span>
              </button>` : u}
          <h2 id="${this.panelInfo.tag}-title" part="title">
            <button
              part="title-button"
              @dblclick="${(e) => {
      this.toggleExpand(e), this.startDraggingDebounce.clear();
    }}">
              ${c.getPanelHeader(this.panelInfo)}
            </button>
          </h2>
          <div class="actions" @mousedown="${(e) => e.stopPropagation()}">${this.renderActions()}</div>
          ${this.renderHelpButton()} ${this.renderPopupButton()}
        </div>
        <div part="content" id="content">
          <slot name="content"></slot>
        </div>
      </div>
    ` : u;
  }
  getPopupButtonIcon() {
    return this.panelInfo ? this.panelInfo.panel === void 0 ? d.x : this.panelInfo.floating ? this.panelInfo.panel === "bottom" ? d.layoutBottom : this.panelInfo.panel === "left" ? d.layoutLeft : this.panelInfo.panel === "right" ? d.layoutRight : u : d.share : u;
  }
  renderHelpButton() {
    return this.panelInfo?.helpUrl ? l` <button
      @click="${() => window.open(this.panelInfo.helpUrl, "_blank")}"
      @mousedown="${(e) => e.stopPropagation()}"
      aria-label="More information about ${this.panelInfo.header}"
      class="icon"
      title="More information about ${this.panelInfo.header}">
      <span>${d.help}</span>
    </button>` : u;
  }
  renderActions() {
    if (!this.panelInfo?.actionsTag)
      return u;
    const e = this.panelInfo.actionsTag;
    return Qe(`<${e}></${e}>`);
  }
  changeDockingPanel(e) {
    const t = e.detail.value.panel;
    if (this.panelInfo?.panel !== t) {
      const i = c.panels.filter((n) => n.panel === t).map((n) => n.panelOrder).sort((n, o) => o - n)[0];
      ve(this), c.updatePanel(this.panelInfo.tag, { panel: t, panelOrder: i + 1 });
    }
    this.panelInfo.floating && this.changePanelFloating(e);
  }
  getResizeDirections() {
    const e = this.getAttribute(b);
    return e ? e.split(" ") : [];
  }
};
j([
  R()
], D.prototype, "panelTag", 2);
j([
  O(".drag-handle")
], D.prototype, "headerDraggableArea", 2);
j([
  O("#content")
], D.prototype, "contentArea", 2);
j([
  O('[part="container"]')
], D.prototype, "container", 2);
j([
  C()
], D.prototype, "dockingItems", 2);
D = j([
  y("copilot-section-panel-wrapper")
], D);
const ae = window.Vaadin.copilot.customComponentHandler;
if (!ae)
  throw new Error("Tried to access custom component handler before it was initialized.");
function Tt(e) {
  r.setOperationWaitsHmrUpdate(e, 3e4);
}
g.on("undoRedo", (e) => {
  const i = { files: Ht(e), uiId: et() }, n = e.detail.undo ? "copilot-plugin-undo" : "copilot-plugin-redo", o = e.detail.undo ? "undo" : "redo";
  tt(o), Tt(de.RedoUndo), ce(n, i, (s) => {
    s.data.performed || (M({
      type: _.INFORMATION,
      message: `Nothing to ${o}`
    }), g.emit("vite-after-update", {}));
  });
});
function Ht(e) {
  if (e.detail.files)
    return e.detail.files;
  const t = ae.getActiveDrillDownContext();
  if (t) {
    const i = ae.getCustomComponentInfo(t);
    if (i)
      return new Array(i.customComponentFilePath);
  }
  return it();
}
var Ut = Object.getOwnPropertyDescriptor, jt = (e, t, i, n) => {
  for (var o = n > 1 ? void 0 : n ? Ut(t, i) : t, s = e.length - 1, a; s >= 0; s--)
    (a = e[s]) && (o = a(o) || o);
  return o;
};
let Ie = class extends I {
  static get styles() {
    return [
      S(K),
      S(ze),
      S(nt),
      L`
        :host {
          --lumo-secondary-text-color: var(--dev-tools-text-color);
          --lumo-contrast-80pct: var(--dev-tools-text-color-emphasis);
          --lumo-contrast-60pct: var(--dev-tools-text-color-secondary);
          --lumo-font-size-m: 14px;

          position: fixed;
          bottom: 2.5rem;
          right: 0rem;
          visibility: visible; /* Always show, even if copilot is off */
          user-select: none;
          z-index: 10000;

          --dev-tools-text-color: rgba(255, 255, 255, 0.8);

          --dev-tools-text-color-secondary: rgba(255, 255, 255, 0.65);
          --dev-tools-text-color-emphasis: rgba(255, 255, 255, 0.95);
          --dev-tools-text-color-active: rgba(255, 255, 255, 1);

          --dev-tools-background-color-inactive: rgba(45, 45, 45, 0.25);
          --dev-tools-background-color-active: rgba(45, 45, 45, 0.98);
          --dev-tools-background-color-active-blurred: rgba(45, 45, 45, 0.85);

          --dev-tools-border-radius: 0.5rem;
          --dev-tools-box-shadow: 0 0 0 1px rgba(255, 255, 255, 0.05), 0 4px 12px -2px rgba(0, 0, 0, 0.4);

          --dev-tools-blue-hsl: 206, 100%, 70%;
          --dev-tools-blue-color: hsl(var(--dev-tools-blue-hsl));
          --dev-tools-green-hsl: 145, 80%, 42%;
          --dev-tools-green-color: hsl(var(--dev-tools-green-hsl));
          --dev-tools-grey-hsl: 0, 0%, 50%;
          --dev-tools-grey-color: hsl(var(--dev-tools-grey-hsl));
          --dev-tools-yellow-hsl: 38, 98%, 64%;
          --dev-tools-yellow-color: hsl(var(--dev-tools-yellow-hsl));
          --dev-tools-red-hsl: 355, 100%, 68%;
          --dev-tools-red-color: hsl(var(--dev-tools-red-hsl));

          /* Needs to be in ms, used in JavaScript as well */
          --dev-tools-transition-duration: 180ms;
        }

        .notification-tray {
          display: flex;
          flex-direction: column-reverse;
          align-items: flex-end;
          margin: 0.5rem;
          flex: none;
        }

        @supports (backdrop-filter: blur(1px)) {
          .notification-tray div.message {
            backdrop-filter: blur(8px);
          }

          .notification-tray div.message {
            background-color: var(--dev-tools-background-color-active-blurred);
          }
        }

        .notification-tray .message {
          pointer-events: auto;
          background-color: var(--dev-tools-background-color-active);
          color: var(--dev-tools-text-color);
          max-width: 40rem;
          box-sizing: border-box;
          border-radius: var(--dev-tools-border-radius);
          margin-top: 0.5rem;
          transition: var(--dev-tools-transition-duration);
          transform-origin: bottom right;
          animation: slideIn var(--dev-tools-transition-duration);
          box-shadow: var(--dev-tools-box-shadow);
          padding-top: 0.25rem;
          padding-bottom: 0.25rem;
        }

        .notification-tray .message.animate-out {
          animation: slideOut forwards var(--dev-tools-transition-duration);
        }

        .notification-tray .message .message-details {
          word-break: break-all;
        }

        .message.information {
          --dev-tools-notification-color: var(--dev-tools-blue-color);
        }

        .message.warning {
          --dev-tools-notification-color: var(--dev-tools-yellow-color);
        }

        .message.error {
          --dev-tools-notification-color: var(--dev-tools-red-color);
        }

        .message {
          display: flex;
          padding: 0.1875rem 0.75rem 0.1875rem 2rem;
          background-clip: padding-box;
        }

        .message.log {
          padding-left: 0.75rem;
        }

        .message-content {
          max-width: 100%;
          margin-right: 0.5rem;
          -webkit-user-select: text;
          -moz-user-select: text;
          user-select: text;
        }

        .message-heading {
          position: relative;
          display: flex;
          align-items: center;
          margin: 0.125rem 0;
        }

        .message .message-details {
          font-weight: 400;
          color: var(--dev-tools-text-color-secondary);
          margin: 0.25rem 0;
          display: flex;
          flex-direction: column;
        }

        .message .message-details[hidden] {
          display: none;
        }

        .message .message-details p {
          display: inline;
          margin: 0;
          margin-right: 0.375em;
          word-break: break-word;
        }

        .message .persist {
          color: var(--dev-tools-text-color-secondary);
          white-space: nowrap;
          margin: 0.375rem 0;
          display: flex;
          align-items: center;
          position: relative;
          -webkit-user-select: none;
          -moz-user-select: none;
          user-select: none;
        }

        .message .persist::before {
          content: '';
          width: 1em;
          height: 1em;
          border-radius: 0.2em;
          margin-right: 0.375em;
          background-color: rgba(255, 255, 255, 0.3);
        }

        .message .persist:hover::before {
          background-color: rgba(255, 255, 255, 0.4);
        }

        .message .persist.on::before {
          background-color: rgba(255, 255, 255, 0.9);
        }

        .message .persist.on::after {
          content: '';
          order: -1;
          position: absolute;
          width: 0.75em;
          height: 0.25em;
          border: 2px solid var(--dev-tools-background-color-active);
          border-width: 0 0 2px 2px;
          transform: translate(0.05em, -0.05em) rotate(-45deg) scale(0.8, 0.9);
        }

        .message .dismiss-message {
          font-weight: 600;
          align-self: stretch;
          display: flex;
          align-items: center;
          padding: 0 0.25rem;
          margin-left: 0.5rem;
          color: var(--dev-tools-text-color-secondary);
        }

        .message .dismiss-message:hover {
          color: var(--dev-tools-text-color);
        }

        .message.log {
          color: var(--dev-tools-text-color-secondary);
        }

        .message:not(.log) .message-heading {
          font-weight: 500;
        }

        .message.has-details .message-heading {
          color: var(--dev-tools-text-color-emphasis);
          font-weight: 600;
        }

        .message-heading::before {
          position: absolute;
          margin-left: -1.5rem;
          display: inline-block;
          text-align: center;
          font-size: 0.875em;
          font-weight: 600;
          line-height: calc(1.25em - 2px);
          width: 14px;
          height: 14px;
          box-sizing: border-box;
          border: 1px solid transparent;
          border-radius: 50%;
        }

        .message.information .message-heading::before {
          content: 'i';
          border-color: currentColor;
          color: var(--dev-tools-notification-color);
        }

        .message.warning .message-heading::before,
        .message.error .message-heading::before {
          content: '!';
          color: var(--dev-tools-background-color-active);
          background-color: var(--dev-tools-notification-color);
        }

        .ahreflike {
          font-weight: 500;
          color: var(--dev-tools-text-color-secondary);
          text-decoration: underline;
          cursor: pointer;
        }

        @keyframes slideIn {
          from {
            transform: translateX(100%);
            opacity: 0;
          }
          to {
            transform: translateX(0%);
            opacity: 1;
          }
        }

        @keyframes slideOut {
          from {
            transform: translateX(0%);
            opacity: 1;
          }
          to {
            transform: translateX(100%);
            opacity: 0;
          }
        }

        @keyframes fade-in {
          0% {
            opacity: 0;
          }
        }

        @keyframes bounce {
          0% {
            transform: scale(0.8);
          }
          50% {
            transform: scale(1.5);
            background-color: hsla(var(--dev-tools-red-hsl), 1);
          }
          100% {
            transform: scale(1);
          }
        }
      `
    ];
  }
  render() {
    return l`<div class="notification-tray">
      ${r.notifications.map((e) => this.renderNotification(e))}
    </div>`;
  }
  renderNotification(e) {
    return l`
      <div
        class="message ${e.type} ${e.animatingOut ? "animate-out" : ""} ${e.details || e.link ? "has-details" : ""}"
        data-testid="message">
        <div class="message-content">
          <div class="message-heading">${e.message}</div>
          <div class="message-details" ?hidden="${!e.details && !e.link}">
            ${ot(e.details)}
            ${e.link ? l`<a class="ahreflike" href="${e.link}" target="_blank">Learn more</a>` : ""}
          </div>
          ${e.dismissId ? l`<div
                class="persist ${e.dontShowAgain ? "on" : "off"}"
                @click=${() => {
      this.toggleDontShowAgain(e);
    }}>
                ${Nt(e)}
              </div>` : ""}
        </div>
        <div
          class="dismiss-message"
          @click=${(t) => {
      Le(e), t.stopPropagation();
    }}>
          Dismiss
        </div>
      </div>
    `;
  }
  toggleDontShowAgain(e) {
    e.dontShowAgain = !e.dontShowAgain, this.requestUpdate();
  }
};
Ie = jt([
  y("copilot-notifications-container")
], Ie);
function Nt(e) {
  return e.dontShowAgainMessage ? e.dontShowAgainMessage : "Do not show this again";
}
M({
  type: _.WARNING,
  message: "Development Mode",
  details: "This application is running in development mode.",
  dismissId: "devmode"
});
const ge = G(async () => {
  await st();
});
g.on("vite-after-update", () => {
  ge();
});
const Ce = window?.Vaadin?.connectionState?.stateChangeListeners;
Ce ? Ce.add((e, t) => {
  e === "loading" && t === "connected" && r.active && ge();
}) : console.warn("Unable to add listener for connection state changes");
g.on("copilot-plugin-state", (e) => {
  r.setIdePluginState(e.detail), e.preventDefault();
});
g.on("copilot-early-project-state", (e) => {
  E.setSpringSecurityEnabled(e.detail.springSecurityEnabled), E.setSpringJpaDataEnabled(e.detail.springJpaDataEnabled), E.setSupportsHilla(e.detail.supportsHilla), E.setUrlPrefix(e.detail.urlPrefix), e.preventDefault();
});
g.on("location-changed", (e) => {
  ge();
});
g.on("copilot-ide-notification", (e) => {
  M({
    type: _[e.detail.type],
    message: e.detail.message,
    dismissId: e.detail.dismissId
  }), e.preventDefault();
});
/**
 * @license
 * Copyright (c) 2017 The Polymer Project Authors. All rights reserved.
 * This code may only be used under the BSD style license found at http://polymer.github.io/LICENSE.txt
 * The complete set of authors may be found at http://polymer.github.io/AUTHORS.txt
 * The complete set of contributors may be found at http://polymer.github.io/CONTRIBUTORS.txt
 * Code distributed by Google as part of the polymer project is also
 * subject to an additional IP rights grant found at http://polymer.github.io/PATENTS.txt
 */
let $e = 0, Ue = 0;
const z = [];
let re = !1;
function Bt() {
  re = !1;
  const e = z.length;
  for (let t = 0; t < e; t++) {
    const i = z[t];
    if (i)
      try {
        i();
      } catch (n) {
        setTimeout(() => {
          throw n;
        });
      }
  }
  z.splice(0, e), Ue += e;
}
const Jt = {
  /**
   * Enqueues a function called at microtask timing.
   *
   * @memberof microTask
   * @param {!Function=} callback Callback to run
   * @return {number} Handle used for canceling task
   */
  run(e) {
    re || (re = !0, queueMicrotask(() => Bt())), z.push(e);
    const t = $e;
    return $e += 1, t;
  },
  /**
   * Cancels a previously enqueued `microTask` callback.
   *
   * @memberof microTask
   * @param {number} handle Handle returned from `run` of callback to cancel
   * @return {void}
   */
  cancel(e) {
    const t = e - Ue;
    if (t >= 0) {
      if (!z[t])
        throw new Error(`invalid async handle: ${e}`);
      z[t] = null;
    }
  }
};
/**
@license
Copyright (c) 2017 The Polymer Project Authors. All rights reserved.
This code may only be used under the BSD style license found at http://polymer.github.io/LICENSE.txt
The complete set of authors may be found at http://polymer.github.io/AUTHORS.txt
The complete set of contributors may be found at http://polymer.github.io/CONTRIBUTORS.txt
Code distributed by Google as part of the polymer project is also
subject to an additional IP rights grant found at http://polymer.github.io/PATENTS.txt
*/
const Se = /* @__PURE__ */ new Set();
class Y {
  /**
   * Creates a debouncer if no debouncer is passed as a parameter
   * or it cancels an active debouncer otherwise. The following
   * example shows how a debouncer can be called multiple times within a
   * microtask and "debounced" such that the provided callback function is
   * called once. Add this method to a custom element:
   *
   * ```js
   * import {microTask} from '@vaadin/component-base/src/async.js';
   * import {Debouncer} from '@vaadin/component-base/src/debounce.js';
   * // ...
   *
   * _debounceWork() {
   *   this._debounceJob = Debouncer.debounce(this._debounceJob,
   *       microTask, () => this._doWork());
   * }
   * ```
   *
   * If the `_debounceWork` method is called multiple times within the same
   * microtask, the `_doWork` function will be called only once at the next
   * microtask checkpoint.
   *
   * Note: In testing it is often convenient to avoid asynchrony. To accomplish
   * this with a debouncer, you can use `enqueueDebouncer` and
   * `flush`. For example, extend the above example by adding
   * `enqueueDebouncer(this._debounceJob)` at the end of the
   * `_debounceWork` method. Then in a test, call `flush` to ensure
   * the debouncer has completed.
   *
   * @param {Debouncer?} debouncer Debouncer object.
   * @param {!AsyncInterface} asyncModule Object with Async interface
   * @param {function()} callback Callback to run.
   * @return {!Debouncer} Returns a debouncer object.
   */
  static debounce(t, i, n) {
    return t instanceof Y ? t._cancelAsync() : t = new Y(), t.setConfig(i, n), t;
  }
  constructor() {
    this._asyncModule = null, this._callback = null, this._timer = null;
  }
  /**
   * Sets the scheduler; that is, a module with the Async interface,
   * a callback and optional arguments to be passed to the run function
   * from the async module.
   *
   * @param {!AsyncInterface} asyncModule Object with Async interface.
   * @param {function()} callback Callback to run.
   * @return {void}
   */
  setConfig(t, i) {
    this._asyncModule = t, this._callback = i, this._timer = this._asyncModule.run(() => {
      this._timer = null, Se.delete(this), this._callback();
    });
  }
  /**
   * Cancels an active debouncer and returns a reference to itself.
   *
   * @return {void}
   */
  cancel() {
    this.isActive() && (this._cancelAsync(), Se.delete(this));
  }
  /**
   * Cancels a debouncer's async callback.
   *
   * @return {void}
   */
  _cancelAsync() {
    this.isActive() && (this._asyncModule.cancel(
      /** @type {number} */
      this._timer
    ), this._timer = null);
  }
  /**
   * Flushes an active debouncer and returns a reference to itself.
   *
   * @return {void}
   */
  flush() {
    this.isActive() && (this.cancel(), this._callback());
  }
  /**
   * Returns true if the debouncer is active.
   *
   * @return {boolean} True if active.
   */
  isActive() {
    return this._timer != null;
  }
}
/**
 * @license
 * Copyright 2017 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */
const J = (e, t) => {
  const i = e._$AN;
  if (i === void 0) return !1;
  for (const n of i) n._$AO?.(t, !1), J(n, t);
  return !0;
}, W = (e) => {
  let t, i;
  do {
    if ((t = e._$AM) === void 0) break;
    i = t._$AN, i.delete(e), e = t;
  } while (i?.size === 0);
}, je = (e) => {
  for (let t; t = e._$AM; e = t) {
    let i = t._$AN;
    if (i === void 0) t._$AN = i = /* @__PURE__ */ new Set();
    else if (i.has(e)) break;
    i.add(e), qt(t);
  }
};
function Vt(e) {
  this._$AN !== void 0 ? (W(this), this._$AM = e, je(this)) : this._$AM = e;
}
function Ft(e, t = !1, i = 0) {
  const n = this._$AH, o = this._$AN;
  if (o !== void 0 && o.size !== 0) if (t) if (Array.isArray(n)) for (let s = i; s < n.length; s++) J(n[s], !1), W(n[s]);
  else n != null && (J(n, !1), W(n));
  else J(this, e);
}
const qt = (e) => {
  e.type == Me.CHILD && (e._$AP ??= Ft, e._$AQ ??= Vt);
};
class Xt extends at {
  constructor() {
    super(...arguments), this._$AN = void 0;
  }
  _$AT(t, i, n) {
    super._$AT(t, i, n), je(this), this.isConnected = t._$AU;
  }
  _$AO(t, i = !0) {
    t !== this.isConnected && (this.isConnected = t, t ? this.reconnected?.() : this.disconnected?.()), i && (J(this, t), W(this));
  }
  setValue(t) {
    if (rt(this._$Ct)) this._$Ct._$AI(t, this);
    else {
      const i = [...this._$Ct._$AH];
      i[this._$Ci] = t, this._$Ct._$AI(i, this, 0);
    }
  }
  disconnected() {
  }
  reconnected() {
  }
}
/**
 * @license
 * Copyright (c) 2016 - 2025 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */
const ke = Symbol("valueNotInitialized");
class Yt extends Xt {
  constructor(t) {
    if (super(t), t.type !== Me.ELEMENT)
      throw new Error(`\`${this.constructor.name}\` must be bound to an element.`);
    this.previousValue = ke;
  }
  /** @override */
  render(t, i) {
    return u;
  }
  /** @override */
  update(t, [i, n]) {
    return this.hasChanged(n) ? (this.host = t.options && t.options.host, this.element = t.element, this.renderer = i, this.previousValue === ke ? this.addRenderer() : this.runRenderer(), this.previousValue = Array.isArray(n) ? [...n] : n, u) : u;
  }
  /** @override */
  reconnected() {
    this.addRenderer();
  }
  /** @override */
  disconnected() {
    this.removeRenderer();
  }
  /** @abstract */
  addRenderer() {
    throw new Error("The `addRenderer` method must be implemented.");
  }
  /** @abstract */
  runRenderer() {
    throw new Error("The `runRenderer` method must be implemented.");
  }
  /** @abstract */
  removeRenderer() {
    throw new Error("The `removeRenderer` method must be implemented.");
  }
  /** @protected */
  renderRenderer(t, ...i) {
    const n = this.renderer.call(this.host, ...i);
    lt(n, t, { host: this.host });
  }
  /** @protected */
  hasChanged(t) {
    return Array.isArray(t) ? !Array.isArray(this.previousValue) || this.previousValue.length !== t.length ? !0 : t.some((i, n) => i !== this.previousValue[n]) : this.previousValue !== t;
  }
}
/**
 * @license
 * Copyright (c) 2017 - 2025 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */
const ne = Symbol("contentUpdateDebouncer");
class fe extends Yt {
  /**
   * A property to that the renderer callback will be assigned.
   *
   * @abstract
   */
  get rendererProperty() {
    throw new Error("The `rendererProperty` getter must be implemented.");
  }
  /**
   * Adds the renderer callback to the dialog.
   */
  addRenderer() {
    this.element[this.rendererProperty] = (t, i) => {
      this.renderRenderer(t, i);
    };
  }
  /**
   * Runs the renderer callback on the dialog.
   */
  runRenderer() {
    this.element[ne] = Y.debounce(
      this.element[ne],
      Jt,
      () => {
        this.element.requestContentUpdate();
      }
    );
  }
  /**
   * Removes the renderer callback from the dialog.
   */
  removeRenderer() {
    this.element[this.rendererProperty] = null, delete this.element[ne];
  }
}
class Wt extends fe {
  get rendererProperty() {
    return "renderer";
  }
}
class Gt extends fe {
  get rendererProperty() {
    return "headerRenderer";
  }
}
class Kt extends fe {
  get rendererProperty() {
    return "footerRenderer";
  }
}
const Zt = pe(Wt), Qt = pe(Gt), ei = pe(Kt);
var ti = Object.defineProperty, ii = Object.getOwnPropertyDescriptor, Ne = (e, t, i, n) => {
  for (var o = n > 1 ? void 0 : n ? ii(t, i) : t, s = e.length - 1, a; s >= 0; s--)
    (a = e[s]) && (o = (n ? a(t, i, o) : a(o)) || o);
  return n && o && ti(t, i, o), o;
};
let le = class extends Re {
  constructor() {
    super(...arguments), this.rememberChoice = !1, this.opened = !1;
  }
  firstUpdated(e) {
    super.firstUpdated(e), he(this.renderRoot);
  }
  render() {
    return l` <vaadin-dialog
      id="ai-dialog"
      no-close-on-outside-click
      no-close-on-esc
      overlay-class="ai-dialog"
      ?opened=${this.opened}
      ${Qt(
      () => l`
          <h2>This Operation Uses AI</h2>
          ${d.starsAlt}
        `
    )}
      ${Zt(
      () => l`
          <p>AI is a third-party service that will receive some of your project code as context for the operation.</p>
          <label>
            <input
              type="checkbox"
              @change=${(e) => {
        this.rememberChoice = e.target.checked;
      }} />Don’t ask again
          </label>
        `
    )}
      ${ei(
      () => l`
          <button @click=${() => this.sendEvent("cancel")}>Cancel</button>
          <button class="primary" @click=${() => this.sendEvent("ok")}>OK</button>
        `
    )}></vaadin-dialog>`;
  }
  sendEvent(e) {
    this.dispatchEvent(
      new CustomEvent("ai-usage-response", {
        detail: { response: e, rememberChoice: this.rememberChoice }
      })
    );
  }
};
Ne([
  R()
], le.prototype, "opened", 2);
le = Ne([
  y("copilot-ai-usage-confirmation-dialog")
], le);
let X;
g.on("copilot-project-compilation-error", (e) => {
  if (e.detail.error) {
    let t;
    if (e.detail.files && e.detail.files.length > 0) {
      const i = r.idePluginState?.supportedActions?.includes("undo") ? l`
            <button
              style="display:flex; padding-left: unset; padding-right: unset; gap: var(--space-100)"
              @click="${(n) => {
        n.preventDefault(), g.emit("undoRedo", { undo: !0, files: e.detail.files?.map((o) => o.path) });
      }}">
              <span class="icon">${d.flipBack}</span> <span>Undo last change</span>
            </button>
          ` : u;
      t = dt(
        l`<div>
          <h4 style="margin-block-start: var(--space-50); margin-block-end: var(--space-50)">
            Following files have compilation errors:
          </h4>
          <div style="border-bottom: 1px solid var(--divider-primary-color); padding-bottom: var(--space-75);">
            ${e.detail.files.map(
          (n) => l`<button
                  style="display: block; padding-left: unset; padding-right: unset;"
                  @click="${() => {
            g.emit("show-in-ide", { javaSource: { absoluteFilePath: n.path } });
          }}">
                  ${n.name}
                </button>`
        )}
          </div>
          ${i}
        </div>`
      );
    } else
      t = "Project contains one or more compilation errors.";
    X = M({
      message: "Compilation error",
      details: t,
      type: _.WARNING,
      delay: 3e4
    });
  } else
    X && Le(X), X = void 0;
});
g.on("copilot-java-after-update", (e) => {
  const t = e.detail.classes.filter((n) => n.redefined).map((n) => n.class).join(", ");
  if (t.length === 0)
    return;
  M({
    type: _.INFORMATION,
    message: `Java changes were hot deployed for ${ct(t)}`,
    dismissId: "java-hot-deploy",
    delay: 5e3
  });
});
