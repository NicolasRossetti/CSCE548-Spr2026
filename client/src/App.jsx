import React, { useMemo, useState } from 'react';

const ENTITY_CONFIG = [
  {
    key: 'items',
    label: 'Items',
    singularLabel: 'Item',
    path: '/api/items',
    idField: 'itemId',
    columns: [
      { key: 'itemId', label: 'Item ID', format: 'number' },
      { key: 'name', label: 'Name' },
      { key: 'category', label: 'Category', format: 'title' },
      { key: 'rarity', label: 'Rarity', format: 'title' },
      { key: 'npcSellPrice', label: 'NPC Sell Price', format: 'currency' }
    ]
  },
  {
    key: 'priceSnapshots',
    label: 'Price Snapshots',
    singularLabel: 'Price Snapshot',
    path: '/api/price-snapshots',
    idField: 'snapshotId',
    columns: [
      { key: 'snapshotId', label: 'Snapshot ID', format: 'number' },
      { key: 'itemId', label: 'Item ID', format: 'number' },
      { key: 'buyPrice', label: 'Buy Price', format: 'currency' },
      { key: 'sellPrice', label: 'Sell Price', format: 'currency' },
      { key: 'buyVolume', label: 'Buy Volume', format: 'number' },
      { key: 'sellVolume', label: 'Sell Volume', format: 'number' },
      { key: 'snapshotTime', label: 'Snapshot Time', format: 'datetime' }
    ]
  },
  {
    key: 'orders',
    label: 'Orders',
    singularLabel: 'Order',
    path: '/api/orders',
    idField: 'orderId',
    columns: [
      { key: 'orderId', label: 'Order ID', format: 'number' },
      { key: 'itemId', label: 'Item ID', format: 'number' },
      { key: 'orderType', label: 'Type', format: 'title' },
      { key: 'quantity', label: 'Quantity', format: 'number' },
      { key: 'targetPrice', label: 'Target Price', format: 'currency' },
      { key: 'status', label: 'Status', format: 'title' },
      { key: 'createdAt', label: 'Created At', format: 'datetime' }
    ]
  },
  {
    key: 'trades',
    label: 'Trades',
    singularLabel: 'Trade',
    path: '/api/trades',
    idField: 'tradeId',
    columns: [
      { key: 'tradeId', label: 'Trade ID', format: 'number' },
      { key: 'orderId', label: 'Order ID', format: 'number' },
      { key: 'qtyFilled', label: 'Quantity Filled', format: 'number' },
      { key: 'fillPrice', label: 'Fill Price', format: 'currency' },
      { key: 'fee', label: 'Fee', format: 'currency' },
      { key: 'profit', label: 'Profit', format: 'currency' },
      { key: 'tradeTime', label: 'Trade Time', format: 'datetime' }
    ]
  },
  {
    key: 'notes',
    label: 'Notes',
    singularLabel: 'Note',
    path: '/api/notes',
    idField: 'noteId',
    columns: [
      { key: 'noteId', label: 'Note ID', format: 'number' },
      { key: 'itemId', label: 'Item ID', format: 'number' },
      { key: 'noteText', label: 'Note' },
      { key: 'createdAt', label: 'Created At', format: 'datetime' }
    ]
  }
];

const moneyFormatter = new Intl.NumberFormat('en-US', {
  style: 'currency',
  currency: 'USD',
  minimumFractionDigits: 2,
  maximumFractionDigits: 2
});

const numberFormatter = new Intl.NumberFormat('en-US');

function toTitleCase(value) {
  return String(value)
    .toLowerCase()
    .split(/[_\s]+/)
    .filter(Boolean)
    .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
    .join(' ');
}

function formatValue(value, format) {
  if (value === null || value === undefined || value === '') {
    return 'N/A';
  }

  if (format === 'currency') {
    const parsed = Number(value);
    return Number.isNaN(parsed) ? String(value) : moneyFormatter.format(parsed);
  }

  if (format === 'number') {
    const parsed = Number(value);
    return Number.isNaN(parsed) ? String(value) : numberFormatter.format(parsed);
  }

  if (format === 'datetime') {
    const parsedDate = new Date(value);
    return Number.isNaN(parsedDate.getTime()) ? String(value) : parsedDate.toLocaleString();
  }

  if (format === 'title') {
    return toTitleCase(value);
  }

  return String(value);
}

function describeCount(entity, count) {
  const label = count === 1 ? entity.singularLabel.toLowerCase() : entity.label.toLowerCase();
  return `${count} ${label}`;
}

function DataTable({ entity, records }) {
  return (
    <div className="table-wrap">
      <table>
        <thead>
          <tr>
            {entity.columns.map((column) => (
              <th key={column.key}>{column.label}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {records.map((record, index) => (
            <tr key={`${record[entity.idField] ?? 'row'}-${index}`}>
              {entity.columns.map((column) => (
                <td key={`${column.key}-${index}`}>{formatValue(record[column.key], column.format)}</td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

function DetailsCard({ entity, record }) {
  return (
    <dl className="details-grid">
      {entity.columns.map((column) => (
        <div key={column.key} className="detail-item">
          <dt>{column.label}</dt>
          <dd>{formatValue(record[column.key], column.format)}</dd>
        </div>
      ))}
    </dl>
  );
}

function getSummaryText(entity, result) {
  if (!result) {
    return `No ${entity.label.toLowerCase()} are shown yet.`;
  }

  if (result.type === 'getById') {
    return `Showing ${entity.singularLabel.toLowerCase()} ID ${result.id}.`;
  }

  if (result.type === 'getSubset') {
    return `Showing the first ${result.limit} ${entity.label.toLowerCase()}.`;
  }

  return `Showing all ${describeCount(entity, result.records.length)}.`;
}

function App() {
  const [servicePort, setServicePort] = useState('8080');
  const [portDraft, setPortDraft] = useState('8080');
  const [showConnectionSettings, setShowConnectionSettings] = useState(false);
  const [activeTab, setActiveTab] = useState(ENTITY_CONFIG[0].key);
  const [status, setStatus] = useState('Press "Run All Required GET Calls" to load all required data checks.');
  const [output, setOutput] = useState({});
  const [idInputs, setIdInputs] = useState({});
  const [subsetLimits, setSubsetLimits] = useState({});
  const [runAllLog, setRunAllLog] = useState([]);

  const activeEntity = useMemo(
    () => ENTITY_CONFIG.find((entity) => entity.key === activeTab),
    [activeTab]
  );

  const baseUrl = useMemo(() => `http://localhost:${servicePort}`, [servicePort]);

  function toggleConnectionSettings() {
    setPortDraft(servicePort);
    setShowConnectionSettings((prev) => !prev);
  }

  function savePort() {
    const parsedPort = Number(portDraft);
    if (!Number.isInteger(parsedPort) || parsedPort < 1 || parsedPort > 65535) {
      setStatus('Please enter a valid port number between 1 and 65535.');
      return;
    }

    const newPort = String(parsedPort);
    setServicePort(newPort);
    setShowConnectionSettings(false);
    setStatus(`Connection updated. Using local service on port ${newPort}.`);
  }

  async function fetchJson(url) {
    let response;
    try {
      response = await fetch(url);
    } catch {
      throw new Error('Network error: could not reach the local service. Confirm Spring Boot is running and the selected port is correct.');
    }
    let data = null;
    try {
      data = await response.json();
    } catch {
      data = null;
    }

    if (!response.ok) {
      const message =
        data && typeof data === 'object' && 'message' in data
          ? data.message
          : response.statusText || 'Request failed';
      throw new Error(`${response.status}: ${message}`);
    }

    return data;
  }

  function setEntityOutput(entityKey, payload) {
    setOutput((prev) => ({ ...prev, [entityKey]: payload }));
  }

  async function getAll(entity) {
    setStatus(`Loading all ${entity.label.toLowerCase()}...`);
    try {
      const data = await fetchJson(`${baseUrl}${entity.path}`);
      const records = Array.isArray(data) ? data : [];
      setEntityOutput(entity.key, { type: 'getAll', records });
      setStatus(`Loaded ${describeCount(entity, records.length)}.`);
      return data;
    } catch (error) {
      setStatus(`Could not load ${entity.label.toLowerCase()}: ${error.message}`);
      throw error;
    }
  }

  async function getById(entity, optionalId) {
    const rawId = optionalId ?? idInputs[entity.key];
    const id = Number(rawId);
    if (!Number.isInteger(id) || id < 1) {
      setStatus(`Please enter a valid ${entity.singularLabel.toLowerCase()} ID.`);
      return;
    }

    setStatus(`Looking up ${entity.singularLabel.toLowerCase()} ID ${id}...`);
    try {
      const data = await fetchJson(`${baseUrl}${entity.path}/${id}`);
      setEntityOutput(entity.key, { type: 'getById', record: data, id });
      setStatus(`Loaded ${entity.singularLabel.toLowerCase()} ID ${id}.`);
      return data;
    } catch (error) {
      setStatus(`Could not load ${entity.singularLabel.toLowerCase()} ID ${id}: ${error.message}`);
      throw error;
    }
  }

  async function getSubset(entity, optionalLimit) {
    const rawLimit = optionalLimit ?? subsetLimits[entity.key] ?? 2;
    const limit = Number(rawLimit);
    if (!Number.isInteger(limit) || limit < 1) {
      setStatus(`Please enter a subset size of 1 or more.`);
      return;
    }

    setStatus(`Loading first ${limit} ${entity.label.toLowerCase()}...`);
    try {
      const data = await fetchJson(`${baseUrl}${entity.path}?limit=${limit}`);
      const records = Array.isArray(data) ? data : [];
      setEntityOutput(entity.key, { type: 'getSubset', records, limit });
      setStatus(`Loaded ${describeCount(entity, records.length)} from the subset view.`);
      return data;
    } catch (error) {
      setStatus(`Could not load subset for ${entity.label.toLowerCase()}: ${error.message}`);
      throw error;
    }
  }

  async function runAllGetMethodsForEntity(entity) {
    const logs = [];
    try {
      const allData = await getAll(entity);
      logs.push(`${entity.label}: Loaded full list.`);

      const firstId = Array.isArray(allData) && allData.length > 0 ? allData[0][entity.idField] : null;
      if (firstId != null) {
        setIdInputs((prev) => ({ ...prev, [entity.key]: String(firstId) }));
        await getById(entity, firstId);
        logs.push(`${entity.label}: Loaded ${entity.singularLabel.toLowerCase()} ID ${firstId}.`);
      } else {
        logs.push(`${entity.label}: Skipped ID lookup because no records were found.`);
      }

      await getSubset(entity, 2);
      logs.push(`${entity.label}: Loaded subset (first 2 records).`);
    } catch (error) {
      logs.push(`${entity.label}: Could not complete all required calls (${error.message}).`);
    }
    return logs;
  }

  async function runAllGetMethodsForAllTables() {
    setRunAllLog([]);
    setStatus('Running the required GET checklist for all sections...');

    const allLogs = [];
    for (const entity of ENTITY_CONFIG) {
      const logs = await runAllGetMethodsForEntity(entity);
      allLogs.push(...logs);
      setRunAllLog([...allLogs]);
    }

    setStatus('Finished the required GET checklist for all sections.');
  }

  const entityOutput = output[activeEntity.key];

  return (
    <div className="app">
      <header className="hero">
        <h1>Bazaar Tracker Client</h1>
        <p>Browse marketplace records in plain language: load all records, find one by ID, or view a smaller subset.</p>
      </header>

      <div className="panel">
        <div className="controls-row">
          <button className="primary" onClick={runAllGetMethodsForAllTables}>Run All Required GET Calls</button>
          <button className="secondary" onClick={toggleConnectionSettings}>
            {showConnectionSettings ? 'Hide Connection Settings' : 'Change Connection'}
          </button>
        </div>

        <p className="hint">Current connection: Local service on port {servicePort}.</p>

        {showConnectionSettings && (
          <div className="connection-panel" aria-label="Connection settings">
            <label className="field" htmlFor="servicePort">
              <span>Local Service Port</span>
              <input
                id="servicePort"
                type="number"
                min="1"
                max="65535"
                value={portDraft}
                onChange={(event) => setPortDraft(event.target.value)}
              />
            </label>
            <button onClick={savePort}>Save Port</button>
          </div>
        )}

        <div className="status"><strong>Status:</strong> {status}</div>

        <div className="tab-row" role="tablist" aria-label="Entity tabs">
          {ENTITY_CONFIG.map((entity) => (
            <button
              key={entity.key}
              className={entity.key === activeEntity.key ? 'active' : ''}
              onClick={() => setActiveTab(entity.key)}
              role="tab"
              aria-selected={entity.key === activeEntity.key}
            >
              {entity.label}
            </button>
          ))}
        </div>

        <h2>{activeEntity.label}</h2>
        <div className="entity-controls">
          <button onClick={() => getAll(activeEntity)}>Load All</button>
          <label className="field" htmlFor={`id-${activeEntity.key}`}>
            <span>{activeEntity.singularLabel} ID</span>
            <input
              id={`id-${activeEntity.key}`}
              type="number"
              min="1"
              placeholder="Enter ID"
              value={idInputs[activeEntity.key] ?? ''}
              onChange={(event) => setIdInputs((prev) => ({ ...prev, [activeEntity.key]: event.target.value }))}
            />
          </label>
          <button onClick={() => getById(activeEntity)}>Find by ID</button>
          <label className="field" htmlFor={`limit-${activeEntity.key}`}>
            <span>Subset Size</span>
            <input
              id={`limit-${activeEntity.key}`}
              type="number"
              min="1"
              value={subsetLimits[activeEntity.key] ?? '2'}
              onChange={(event) => setSubsetLimits((prev) => ({ ...prev, [activeEntity.key]: event.target.value }))}
            />
          </label>
          <button onClick={() => getSubset(activeEntity)}>Load Subset</button>
        </div>

        <p className="result-summary">{getSummaryText(activeEntity, entityOutput)}</p>

        {!entityOutput && (
          <p className="empty-state">No data loaded yet. Click one of the buttons above to get started.</p>
        )}

        {entityOutput?.type === 'getById' && entityOutput.record && (
          <DetailsCard entity={activeEntity} record={entityOutput.record} />
        )}

        {(entityOutput?.type === 'getAll' || entityOutput?.type === 'getSubset') && (
          entityOutput.records.length > 0
            ? <DataTable entity={activeEntity} records={entityOutput.records} />
            : <p className="empty-state">No records were returned for this request.</p>
        )}

        <h3>Run-All Checklist</h3>
        <p className="hint">Each line confirms one required GET call completed by the client.</p>
        <ul className="log-list">
          {runAllLog.length === 0 ? <li>No run yet.</li> : runAllLog.map((line, idx) => <li key={`${line}-${idx}`}>{line}</li>)}
        </ul>
      </div>
    </div>
  );
}

export default App;
