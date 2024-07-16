import * as unplugin from 'unplugin';
import { R as ResolvedOptions, S as ServerContext, O as Options } from './types-B3fGzmzY.cjs';
export { D as DEFAULT_OPTIONS, E as EditableTreeNode, T as TreeNode, b as TreeNodeValueParam, d as TreeNodeValueStatic, c as createTreeNodeValue, g as getFileBasedRouteName, a as getPascalCaseRouteName } from './types-B3fGzmzY.cjs';
import 'vue-router';

declare function createRoutesContext(options: ResolvedOptions): {
    scanPages: (startWatchers?: boolean) => Promise<void>;
    writeConfigFiles: () => void;
    setServerContext: (_server: ServerContext) => void;
    stopWatcher: () => void;
    generateRoutes: () => string;
    generateVueRouterProxy: () => string;
    definePageTransform(code: string, id: string): unplugin.Thenable<unplugin.TransformResult>;
};

declare const _default: unplugin.UnpluginInstance<Options | undefined, boolean>;

/**
 * Adds useful auto imports to the AutoImport config:
 * @example
 * ```js
 * import { VueRouterAutoImports } from 'unplugin-vue-router'
 *
 * AutoImport({
 *   imports: [VueRouterAutoImports],
 * }),
 * ```
 */
declare const VueRouterAutoImports: Record<string, Array<string | [importName: string, alias: string]>>;

export { Options, VueRouterAutoImports, createRoutesContext, _default as default };
