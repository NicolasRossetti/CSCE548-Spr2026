import React, { useMemo, useState } from 'react';

const ENTITY_CONFIG = [
  { key: 'items', label: 'Items', path: '/api/items', idField: 'itemId' },
  { key: 'priceSnapshots', label: 'Price Snapshots', path: '/api/price-snapshots', idField: 'snapshotId' },
  { key: 'orders', label: 'Orders', path: '/api/orders', idField: 'orderId' },
  { key: 'trades', label: 'Trades', path: '/api/trades', idField: 'tradeId' },
  { key: 'notes', label: 'Notes', path: '/api/notes', idField: 'noteId' }
];

function App() {
  const [baseUrl, setBaseUrl] = useState('http://localhost:8090');
  const [activeTab, setActiveTab] = useState(ENTITY_CONFIG[0].key);
  const [status, setStatus] = useState('Ready');
  const [output, setOutput] = useState({});
  const [idInputs, setIdInputs] = useState({});
  const [subsetLimits, setSubsetLimits] = useState({});
  const [runAllLog, setRunAllLog] = useState([]);

  const activeEntity = useMemo(
    () => ENTITY_CONFIG.find((entity) => entity.key === activeTab),
    [activeTab]
  );

  async function fetchJson(url) {
    let response;
    try {
      response = await fetch(url);
    } catch {
      throw new Error('Network error: could not reach API. Confirm API Base URL and that Spring Boot is running.');
    }
    let data = null;
    try {
      data = await response.json();
    } catch {
      data = null;
    }

    if (!response.ok) {
      const message = data ? JSON.stringify(data) : response.statusText;
      throw new Error(`${response.status} ${message}`);
    }

    return data;
  }

  function setEntityOutput(entityKey, payload) {
    setOutput((prev) => ({ ...prev, [entityKey]: payload }));
  }

  async function getAll(entity) {
    setStatus(`Fetching all ${entity.label}...`);
    try {
      const data = await fetchJson(`${baseUrl}${entity.path}`);
      setEntityOutput(entity.key, { type: 'getAll', data });
      setStatus(`Fetched all ${entity.label} (${Array.isArray(data) ? data.length : 0} records).`);
      return data;
    } catch (error) {
      setStatus(`Error in GET all ${entity.label}: ${error.message}`);
      throw error;
    }
  }

  async function getById(entity, optionalId) {
    const rawId = optionalId ?? idInputs[entity.key];
    const id = Number(rawId);
    if (!id || Number.isNaN(id)) {
      setStatus(`Enter a valid ${entity.label} ID first.`);
      return;
    }

    setStatus(`Fetching ${entity.label} by id ${id}...`);
    try {
      const data = await fetchJson(`${baseUrl}${entity.path}/${id}`);
      setEntityOutput(entity.key, { type: 'getById', data });
      setStatus(`Fetched ${entity.label} record id=${id}.`);
      return data;
    } catch (error) {
      setStatus(`Error in GET ${entity.label} by id: ${error.message}`);
      throw error;
    }
  }

  async function getSubset(entity, optionalLimit) {
    const rawLimit = optionalLimit ?? subsetLimits[entity.key] ?? 2;
    const limit = Number(rawLimit);
    if (!limit || Number.isNaN(limit) || limit < 1) {
      setStatus(`Enter a valid subset limit (>=1) for ${entity.label}.`);
      return;
    }

    setStatus(`Fetching subset of ${entity.label} (limit=${limit})...`);
    try {
      const data = await fetchJson(`${baseUrl}${entity.path}?limit=${limit}`);
      setEntityOutput(entity.key, { type: 'getSubset', data });
      setStatus(`Fetched subset of ${entity.label} (limit=${limit}).`);
      return data;
    } catch (error) {
      setStatus(`Error in GET subset ${entity.label}: ${error.message}`);
      throw error;
    }
  }

  async function runAllGetMethodsForEntity(entity) {
    const logs = [];
    try {
      const allData = await getAll(entity);
      logs.push(`${entity.label}: GET all success`);

      const firstId = Array.isArray(allData) && allData.length > 0 ? allData[0][entity.idField] : null;
      if (firstId != null) {
        setIdInputs((prev) => ({ ...prev, [entity.key]: String(firstId) }));
        await getById(entity, firstId);
        logs.push(`${entity.label}: GET by id success (id=${firstId})`);
      } else {
        logs.push(`${entity.label}: GET by id skipped (no records found)`);
      }

      await getSubset(entity, 2);
      logs.push(`${entity.label}: GET subset success (limit=2)`);
    } catch (error) {
      logs.push(`${entity.label}: ${error.message}`);
    }
    return logs;
  }

  async function runAllGetMethodsForAllTables() {
    setRunAllLog([]);
    setStatus('Running all GET methods for all tables...');

    const allLogs = [];
    for (const entity of ENTITY_CONFIG) {
      const logs = await runAllGetMethodsForEntity(entity);
      allLogs.push(...logs);
      setRunAllLog([...allLogs]);
    }

    setStatus('Finished running all GET methods for all tables.');
  }

  const entityOutput = output[activeEntity.key];

  return (
    <div className="app">
      <h1>Bazaar Service Web Client</h1>
      <p>Assignment target: call GET all, GET by id, and GET subset for all 5 tables.</p>

      <div className="panel">
        <div className="controls-row">
          <label htmlFor="baseUrl">API Base URL:</label>
          <input
            id="baseUrl"
            value={baseUrl}
            onChange={(event) => setBaseUrl(event.target.value)}
            style={{ minWidth: 320 }}
          />
          <button onClick={runAllGetMethodsForAllTables}>Run All Required GET Calls</button>
        </div>

        <div className="status"><strong>Status:</strong> {status}</div>

        <div className="tab-row">
          {ENTITY_CONFIG.map((entity) => (
            <button
              key={entity.key}
              className={entity.key === activeEntity.key ? 'active' : ''}
              onClick={() => setActiveTab(entity.key)}
            >
              {entity.label}
            </button>
          ))}
        </div>

        <h3>{activeEntity.label}</h3>
        <div className="entity-controls">
          <button onClick={() => getAll(activeEntity)}>GET All</button>
          <input
            placeholder={`${activeEntity.label} ID`}
            value={idInputs[activeEntity.key] ?? ''}
            onChange={(event) => setIdInputs((prev) => ({ ...prev, [activeEntity.key]: event.target.value }))}
          />
          <button onClick={() => getById(activeEntity)}>GET By ID</button>
          <input
            placeholder="Subset limit"
            value={subsetLimits[activeEntity.key] ?? '2'}
            onChange={(event) => setSubsetLimits((prev) => ({ ...prev, [activeEntity.key]: event.target.value }))}
          />
          <button onClick={() => getSubset(activeEntity)}>GET Subset</button>
        </div>

        <pre>{JSON.stringify(entityOutput ?? { message: 'No data yet for this section.' }, null, 2)}</pre>

        <h3>Run-All Execution Log</h3>
        <ul className="log-list">
          {runAllLog.length === 0 ? <li>No run yet.</li> : runAllLog.map((line, idx) => <li key={`${line}-${idx}`}>{line}</li>)}
        </ul>
      </div>
    </div>
  );
}

export default App;
